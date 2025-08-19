# 布隆过滤器实践

## 方案一：bitmap实现
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Resource
    private RedisUtil redisUtil;

    /**
     * 初始化布隆过滤器
     */
    @PostConstruct
    public void initBloomFilter(){
        List<Long> userIds = baseMapper.selectAllIds(); // 自定义方法
        for (Long id : userIds) {
            redisUtil.setBit(BLOOM_FILTER_KEY, id, true);
        }
    }

    @Override
    public User getUserById(Long id) {
        // 看看布隆过滤器中是否存在结果
        boolean exists = redisUtil.getBit(BLOOM_FILTER_KEY, id);
        if (!exists) {
            return null; // 防止缓存穿透
        }
        // 查询redis中
        String redisKey = RedisConstant.USER + id;
        String json = redisUtil.get(redisKey);
        if (json != null){
            return JSON.parseObject(json, User.class);
        }
        // 查询数据库
        User user = this.getById(id);
        if (user != null){
            // 设置一下缓存
            redisUtil.set(redisKey, JSON.toJSONString(user));
        }
        return user;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        this.baseMapper.insert(user);
        // 添加到布隆过滤器中
        redisUtil.setBit(BLOOM_FILTER_KEY, user.getId(), true);
    }

    /**
     * 判断用户是否已经看过某个内容
     * @param userId 用户id
     * @param contentId 内容id
     * @return 返回结果
     */
    public boolean isAlreadySeen(Long userId, Long contentId) {
        // 1. 获取用户已观看的内容
        String redisKey = RedisConstant.SEEN + userId;
        // 位图中检查某个 contentId 是否已经看过
        return redisUtil.getBit(redisKey, contentId);
    }

    // 标记用户已看过
    public void markAsSeen(Long userId, Long contentId) {
        String redisKey = RedisConstant.SEEN + userId;
        redisUtil.setBit(redisKey, contentId, true);
    }

    @Override
    public List<Long> recommend(Long userId, List<Long> allContentIds) {
        List<Long> toRecommend = new ArrayList<>();
        // 遍历内容id
        for (Long contentId : allContentIds) {
            // 判断是否已经看过
            if (!isAlreadySeen(userId, contentId)) {
                toRecommend.add(contentId);
                // 标记为已看过
                markAsSeen(userId, contentId);
            }
        }
        return toRecommend;
    }
}


```

## 方案二：redisBloom实现
```java
// 创建布隆过滤器
// redisBloomClient.createFilter(BLOOM_FILTER_KEY, 1000000, 0.01);
// 添加元素
// redisBloomClient.add(BLOOM_FILTER_KEY, user.getId().toString());
// 判断元素是否存在
// redisBloomClient.exists(BLOOM_FILTER_KEY, id.toString();
```

### 使用场景
1.给爬虫数据做url去重

2.IP黑名单过滤

3.剔除用户已读内容

package com.org.bloomfilterstudy.service;

import com.org.bloomfilterstudy.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 根据id查询用户
     * @param id id
     * @return  用户
     */
    User getUserById(Long id);

    /**
     * 添加用户
     * @param user 用户
     */
    void addUser(User user);

    /**
     * 过滤用户已经看过的内容
     *
     * @param userId 用户id
     * @param allContentIds 所有内容id
     * @return 推荐内容id
     */
    List<Long> recommend(Long userId, List<Long> allContentIds);
}

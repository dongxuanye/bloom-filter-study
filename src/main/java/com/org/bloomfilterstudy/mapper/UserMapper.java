package com.org.bloomfilterstudy.mapper;

import com.org.bloomfilterstudy.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT id FROM user")
    List<Long> selectAllIds();

}





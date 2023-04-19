package com.xj.usercenter.service;

import com.xj.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 嘻精
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-19 19:43:09
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}

package com.xj.usercenter.service;

import com.xj.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

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
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);
    
    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);
    
    /**
     * 用户接口
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
    
    /**
     * 请求用户注销
     * @param request
     *
     */
    int userLogout(HttpServletRequest request);
}

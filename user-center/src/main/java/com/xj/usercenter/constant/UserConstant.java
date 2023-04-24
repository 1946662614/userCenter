package com.xj.usercenter.constant;

/**
 * @ClassName UserConstant
 * @Description 用户常量
 * @Author 嘻精
 * @Date 2023/4/21 17:46
 * @Version 1.0
 */

public interface UserConstant {
    /**
     * 用户登陆状态
     */
    String USER_LOGIN_STATE = "userLoginState";
    
    // --------- 权限 -------------
    
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
    
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
}

package com.xj.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRegisterRequest
 * @Description 用户登录请求体
 * @Author 嘻精
 * @Date 2023/4/20 22:31
 * @Version 1.0
 */
// 实现Serializable可以防止对象在序列化过程中产生冲突
// @Data
@Data
public class UserLoginRequest implements Serializable {
    // 生成序列化id
    private static final long serialVersionUID = -6915290263441277454L;
    
     private String userAccount;
    
     private String userPassword;
     
}

package com.xj.usercenter.service;
import java.util.Date;

import com.xj.usercenter.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 * @author 嘻精
 */
@SpringBootTest
class UserServiceTest {
    
    @Resource
    private UserService userService;
    
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserName("zhangsan");
        user.setUserAccount("1234567");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("112233");
        user.setEmail("445566");
        user.setUserStatus(0);
        user.setIsDelete(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        //增加断言:测试测试结果是否等于期望结果
        Assertions.assertTrue(result);
    }
    
    @Test
    void userRegister() {
        String userA = "xijj";
        String userPwd = "";
        String checkPwd = "abc";
        long res = userService.userRegister(userA, userPwd, checkPwd);
        Assertions.assertEquals(-1, res);
        userA = "xj";
        res = userService.userRegister(userA, userPwd, checkPwd);
        Assertions.assertEquals(-1, res);
        userA = "xjjj";
        userPwd = "123456";
        res = userService.userRegister(userA, userPwd, checkPwd);
        Assertions.assertEquals(-1, res);
        userA = "x ijj";
        userPwd = "12345678";
        res = userService.userRegister(userA, userPwd, checkPwd);
        Assertions.assertEquals(-1, res);
        userA = "xijj";
        res = userService.userRegister(userA, userPwd, checkPwd);
        
    }
}
package com.xj.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterApplicationTests {
    
    @Test
    void testDegest() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest("abcd".getBytes(StandardCharsets.UTF_8));
        String res = new String(digest);
        System.out.println(res);
    }
    
    @Test
    void contextLoads() {
    }
    
}

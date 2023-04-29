package com.xj.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xj.usercenter.common.ErrorCode;
import com.xj.usercenter.exception.BusinessException;
import com.xj.usercenter.service.UserService;
import com.xj.usercenter.model.domain.User;
import com.xj.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xj.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author 嘻精
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-19 19:43:09
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "xj666";
    
    
    
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验
        // 1.1 账号密码，确认密码不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        // 1.2账号大于4
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号太短");
        }
        // 1.3密码大于8
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码太短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球码太长");
        }
        
        // 1.4账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        // 正则表达式语法
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.5密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.6账户不能重复
        // 通过QueryWrapper比较参数
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        // count需要传入QueryWrapper参数，根据 Wrapper 条件，查询总记录数
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已经注册过了！");
        }
    
        // 1.7星球账户不能重复
        // 通过QueryWrapper比较参数
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        // count需要传入QueryWrapper参数，根据 Wrapper 条件，查询总记录数
        count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号已经注册过了！");
        }
        
        // 2.加密
        // 给加密字段加盐
        // 使用MD5加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        
        // 3.插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        // 判断是否插入成功
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }
    
    /**
     * 登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        // 1.1 账号密码，确认密码不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.2账号大于4
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.3密码大于8
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    
        // 1.4账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        // 正则表达式语法
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.加密
        // 给加密字段加盐
        // 使用MD5加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    
        // 3.查询用户是否存在
        // 通过QueryWrapper比较参数
        // 此时当此用户处于已经被逻辑删除的状态时，如何判断是否该查询
        // mp框架中的一个配置可以自动识别未被逻辑删除的信息
        
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        
        // 只查询第一条数据
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    
        // 4.用户脱敏
        // 利用插件生成setter，其中用户密码，更新时间，是否删除不需要返回
        User safetyUser = getSafetyUser(user);
        // 5.记录用户的登录态
        // session中的attribute实际上是个map
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        
        // 6.返回脱敏后的用户信息
        return safetyUser;
    }
    
    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }
    
    /**
     * 请求用户注销
     * @param request
     * @return 1
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 将用户态为USER_LOGIN_STATE的session移除
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
    
    
}





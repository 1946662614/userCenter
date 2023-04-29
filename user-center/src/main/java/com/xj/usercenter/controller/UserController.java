package com.xj.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xj.usercenter.common.BaseResponse;
import com.xj.usercenter.common.ErrorCode;
import com.xj.usercenter.common.ResultUtils;
import com.xj.usercenter.constant.UserConstant;
import com.xj.usercenter.exception.BusinessException;
import com.xj.usercenter.model.domain.User;
import com.xj.usercenter.model.request.UserLoginRequest;
import com.xj.usercenter.model.request.UserRegisterRequest;
import com.xj.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xj.usercenter.constant.UserConstant.ADMIN_ROLE;

/**
 * @ClassName UserController
 * @Description 用户接口
 * @Author 嘻精
 * @Date 2023/4/20 22:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Resource
    private UserService userService;
    
    @PostMapping("/register")
    // 要@RequestBody注解之后，mvc框架才会将数据与前端传来的参数关联在一起
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 判断是否为空
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在userRegisterRequest中取得数据，再填入userRegister方法
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        // 校验三个参数是否为空，为空直接返回
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            return null;
        }
        // 调用注册方法
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        return new BaseResponse<>(0,result,"ok");
        return ResultUtils.success(result);
    }
    
    @PostMapping("/login")
    // 要@RequestBody注解之后，mvc框架才会将数据与前端传来的参数关联在一起
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 判断是否为空
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在userRegisterRequest中取得数据，再填入userRegister方法
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 校验两个参数是否为空，为空直接返回
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 调用注册方法
        User user = userService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }
    
    @PostMapping("/logout")
    // 要@RequestBody注解之后，mvc框架才会将数据与前端传来的参数关联在一起
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        // 判断是否为空
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 调用注销方法
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    
    /**
     * 获取当前登陆用户信息
     * @param request session
     * @return 当前用户信息
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        // 获取当前用户session
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        // 强转
        User currentUser = (User) userObj;
        // 判断用户是否为空
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 根据id查询数据库
        Long userId = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }
    
    /**
     * 根据用户名进行模糊查询
     * @param userName 用户名
     * @return 查询出的用户
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        // 鉴权，是否为管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 判断用户名是否为空
        if (StringUtils.isNotBlank(userName)) {
            // 调用wrapper查询
            queryWrapper.like("userName", userName);
        }
        // 返回
        List<User> userList = userService.list(queryWrapper);
        // 将返回值中的密码过滤
        List<User> list = userList.stream().map(user -> {
            user.setUserPassword(null);
            // 将脱敏后的用户返回
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(list);
    }
    
    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 鉴权，是否为管理员
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 判断id是否小于等于0
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 返回
        // 框架会自动进行逻辑删除
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }
    
    /**
     * 鉴权，是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 鉴权
        // 仅管理员可删除
        // 此时将用户状态值提到用户常量接口里，并且要用类来调用静态方法
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        // 判断user是否为空以及是否为管理员
        if (user == null || user.getUserRole() != ADMIN_ROLE){
            // 返回一个空数组
            return false;
        }
        return true;
    }
    
    
}

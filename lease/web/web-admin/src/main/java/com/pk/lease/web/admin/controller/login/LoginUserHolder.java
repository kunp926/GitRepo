package com.pk.lease.web.admin.controller.login;

import com.pk.lease.common.login.LoginUser;

public class LoginUserHolder {
    //使用ThreadLocal来为每一个线程提供变量副本，增加线程安全性
    //这里用作存储每一个用户的token，避免解析多次jwt
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
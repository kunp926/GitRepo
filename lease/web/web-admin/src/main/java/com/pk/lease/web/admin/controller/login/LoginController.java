package com.pk.lease.web.admin.controller.login;


import com.pk.lease.common.result.Result;
import com.pk.lease.common.utils.JwtUtil;
import com.pk.lease.web.admin.service.LoginService;
import com.pk.lease.web.admin.service.SystemUserService;
import com.pk.lease.web.admin.vo.login.CaptchaVo;
import com.pk.lease.web.admin.vo.login.LoginVo;
import com.pk.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SystemUserService systemUserService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo result=loginService.getCaptcha();
        return Result.ok(result);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String jwt=loginService.login(loginVo);
        return Result.ok(jwt);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info(@RequestHeader("access_token")String token) {
        Claims claims= JwtUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        SystemUserInfoVo systemUserInfoVo=systemUserService.getLoginUserInfoById(userId);
        return Result.ok(systemUserInfoVo);
    }
}
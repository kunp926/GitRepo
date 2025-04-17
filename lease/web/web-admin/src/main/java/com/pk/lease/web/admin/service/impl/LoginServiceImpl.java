package com.pk.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pk.lease.common.constant.RedisConstant;
import com.pk.lease.common.exception.LeaseException;
import com.pk.lease.common.result.ResultCodeEnum;
import com.pk.lease.common.utils.JwtUtil;
import com.pk.lease.model.entity.SystemUser;
import com.pk.lease.model.enums.BaseStatus;
import com.pk.lease.web.admin.mapper.SystemUserMapper;
import com.pk.lease.web.admin.service.LoginService;
import com.pk.lease.web.admin.vo.login.CaptchaVo;
import com.pk.lease.web.admin.vo.login.LoginVo;
import com.pk.lease.web.admin.vo.system.user.SystemUserInfoVo;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID().toString();
        String code = specCaptcha.text().toLowerCase();

        stringRedisTemplate.opsForValue().set(key,code,RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);

        return new CaptchaVo(specCaptcha.toBase64(), key);
    }

    @Override
    public String login(LoginVo loginVo) {
        if(loginVo.getCaptchaCode()==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND.getCode(),ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND.getMessage());
        }
        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());
        if(code==null){
            throw new LeaseException(ResultCodeEnum. ADMIN_CAPTCHA_CODE_EXPIRED.getCode(),ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED.getMessage());
        }
        if(!code.equalsIgnoreCase(loginVo.getCaptchaCode())){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR.getCode(),ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR.getMessage());
        }
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername,loginVo.getUsername());
        SystemUser systemUser = systemUserMapper.selectOne(queryWrapper);
        if(systemUser==null){
            throw new LeaseException( ResultCodeEnum. ADMIN_ACCOUNT_NOT_EXIST_ERROR.getCode(),ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR.getMessage());
        }
        if(systemUser.getStatus()== BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR.getCode(),ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR.getMessage());
        }
        if(!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR.getCode(),ResultCodeEnum.ADMIN_ACCOUNT_ERROR.getMessage());
        }

        Map<String,Object> claim = new HashMap<>();
        claim.put("username",systemUser.getUsername());
        claim.put("userId",systemUser.getId());
        return JwtUtil.createToken(systemUser.getId(),systemUser.getUsername());
    }

    @Override
    public SystemUserInfoVo getLoginUserInfo(Long userId) {
        SystemUser systemUser = systemUserMapper.selectById(userId);
        SystemUserInfoVo systemUserInfoVo = new SystemUserInfoVo();
        systemUserInfoVo.setName(systemUser.getName());
        systemUserInfoVo.setAvatarUrl(systemUser.getAvatarUrl());
        return systemUserInfoVo;
    }
}

package com.liqun.power.controller;


import com.liqun.power.entity.AccessToken;
import com.liqun.power.entity.AppInfo;
import com.liqun.power.entity.TokenInfo;
import com.liqun.power.entity.UserInfo;
import com.liqun.power.util.ApiResponse;
import com.liqun.power.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * PF-002
 * 使用流程
 * 1.接口调用方(客户端)向接口提供方(服务器)申请接口调用账号，申请成功后，接口提供方会给接口调用方一个appId和一个key参数
 * <p>
 * 2.客户端携带参数appId、timestamp、sign去调用服务器端的API token，其中sign=加密(appId + timestamp + key)
 * <p>
 * 3.客户端拿着api_token 去访问不需要登录就能访问的接口
 * <p>
 * 4.当访问用户需要登录的接口时，客户端跳转到登录页面，通过用户名和密码调用登录接口，登录接口会返回一个usertoken, 客户端拿着usertoken 去访问需要登录才能访问的接口
 *
 * @Author: PowerQun
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接口调用方(客户端)向接口提供方(服务器)申请接口调用账号，申请成功后，接口提供方会给接口调用方一个appId和一个key参数
     *
     * @return
     */
    @GetMapping("/getAppInfo")
    public ApiResponse<AppInfo> getAppInfo() {
        AppInfo appInfo = new AppInfo("1", "12345678954556");
        return ApiResponse.success(appInfo);
    }

    /**
     * API Token(接口令牌): 用于访问不需要用户登录的接口，如登录、注册、一些基本数据的获取等。获取接口令牌需要拿appId、timestamp和sign来换，sign=加密(timestamp+key)
     *
     * @param appId
     * @param timestamp 是客户端调用接口时对应的当前时间戳，时间戳用于防止DoS攻击
     * @param sign      客户端用timestamp，id,key生成sign
     * @return
     */
    @PostMapping("/api_token")
    @ResponseBody
    public ApiResponse<AccessToken> apiToken(String appId, @RequestHeader("timestamp") String timestamp, @RequestHeader("sign") String sign) {
        Assert.isTrue(!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(sign));
        Long requestInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
        Assert.isTrue(requestInterval < 5 * 60 * 1000, "请求过期，请重新请求");
        //根据appId查询数据库获取appSelect  每一个app对应的一个key
        AppInfo appInfo = new AppInfo(appId, "12345678");
        //校验签名
        String signString = timestamp + appId + appInfo.getKey();
        String signature = MD5Util.encode(signString);
        Assert.isTrue(signature.equals(sign), "签名错误");
        //生成一个token保存到redis中
        AccessToken accessToken = this.saveToken(0, appInfo, null);
        return ApiResponse.success(accessToken);
    }

    /**
     * USER Token(用户令牌): 用于访问需要用户登录之后的接口，如：获取我的基本信息、保存、修改、删除等操作。获取用户令牌需要拿用户名和密码来换
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/user_token")
    public ApiResponse<UserInfo> userToken(String username, String password) {
        //根据用户名去数据库查询,password可以使用RSA加密
        UserInfo userInfo = new UserInfo(username, password, "salt");
        String pwd = password + userInfo.getSalt();
        String passwordMD5 = MD5Util.encode(pwd);
        Assert.isTrue(passwordMD5.equals(userInfo.getPassword()), "密码错误");
        //保存token
        AppInfo appInfo = new AppInfo("1", "12345678954556");
        AccessToken accessToken = this.saveToken(1, appInfo, userInfo);
        userInfo.setAccessToken(accessToken);
        return ApiResponse.success(userInfo);

    }

    private AccessToken saveToken(int tokenType, AppInfo appInfo, UserInfo userInfo) {
        //生成token
        String token = UUID.randomUUID().toString();
        //token有效期为2小时
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 2 * 60 * 60);
        Date expireTime = calendar.getTime();
        //保存token
        ValueOperations<String, TokenInfo> valueOperations = redisTemplate.opsForValue();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTokenType(tokenType);
        tokenInfo.setAppInfo(appInfo);
        if (tokenType == 1) {
            tokenInfo.setUserInfo(userInfo);
        }
        valueOperations.set(token, tokenInfo, 2 * 60 * 60, TimeUnit.SECONDS);
        AccessToken accessToken = new AccessToken(token, expireTime);
        return accessToken;
    }


}

package com.liqun.power.interceptor;

import com.liqun.power.annotation.NotRepeatSubmit;
import com.liqun.power.entity.TokenInfo;
import com.liqun.power.util.ApiUtil;
import com.liqun.power.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * nonce：随机值，是客户端随机生成的值，作为参数传递过来，随机值的目的是增加sign签名的多变性。随机值一般是数字和字母的组合，6位长度，随机值的组成和长度没有固定规则。
     * sign: 一般用于参数签名，防止参数被非法篡改，最常见的是修改金额等重要敏感参数
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String timestamp = request.getHeader("timestamp");

        //随机字符串
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        Assert.isTrue(!StringUtils.isEmpty(token) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(nonce) && !StringUtils.isEmpty(sign));

        //获取超时时间
        NotRepeatSubmit notRepeatSubmit = ApiUtil.getNotRepeatSubmit(handler);
        long expireTime = notRepeatSubmit == null ? 5 * 60 * 1000 : notRepeatSubmit.value();

        //请求时间间隔
        long requestInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
        Assert.isTrue(requestInterval < expireTime, "请求超时,请重新请求");

        //验证token是否存在
        ValueOperations<String, TokenInfo> valueOperations = redisTemplate.opsForValue();
        TokenInfo tokenInfo = valueOperations.get(token);
        Assert.notNull(token, "token错误");

        //校验签名  请求参数+token+timestamp+nonce
        String signString = ApiUtil.concatSignString(request) + tokenInfo.getAppInfo().getKey() + timestamp + nonce;
        String signStringMD5 = MD5Util.encode(signString);
        boolean flag = signStringMD5.equals(sign);
        Assert.isTrue(flag, "签名错误");

        //拒绝重复调用
        if (notRepeatSubmit != null) {
            ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
            boolean exits = redisTemplate.hasKey(sign);
            Assert.isTrue(!exits, "请勿重复提交");
            operations.set(sign, 0, expireTime, TimeUnit.SECONDS);
        }
        return super.preHandle(request, response, handler);
    }
}

package com.liqun.power.config;

import com.liqun.power.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/**
 *@Author: PowerQun
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    TokenInterceptor tokenInterceptor;

    /**
     * 获取token请求，不需要拦截
     */
    private static final String[] excludePathPatterns = {"/token/**"};

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**").excludePathPatterns(excludePathPatterns);
    }
}

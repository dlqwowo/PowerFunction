package com.liqun.power.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    /**
     * token类型：0 app ,1 user
     */
    private Integer tokenType;

    /**
     * app 信息
     */
    private AppInfo appInfo;

    /**
     * 用户其他信息
     */
    private UserInfo userInfo;
}

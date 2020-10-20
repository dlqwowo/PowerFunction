package com.liqun.power.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AccessToken {

    /**
     * token
     */
    private String token;

    /**
     * 失效时间
     */
    private Date expireTime;
}

package com.liqun.power.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {

    /**
     * App id
     */
    private String appId;


    /**
     * api 密钥
     */
    private String key;
}

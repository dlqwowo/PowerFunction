package com.liqun.power.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: PowerQun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult {

    private String code;

    private String msg;
}

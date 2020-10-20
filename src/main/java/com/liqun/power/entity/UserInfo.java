package com.liqun.power.entity;


import lombok.Data;

@Data
public class UserInfo {

    private String username;

    private String mobile;

    private String email;

    private String password;

    private String salt;

    private AccessToken accessToken;

    public UserInfo(String _username, String _password, String _salt) {
        this.username = _username;
        this.password = _password;
        this.salt = _salt;
    }
}

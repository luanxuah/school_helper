package com.luanxu.bean;

/**
 * @author: 范建海
 * @createTime: 2017/1/16 20:36
 * @className:  AccessTokenBean
 * @description: 获取调接口Token实体Bean
 * @changed by:
 */
public class AccessTokenBean extends Bean {
    // Token码
    protected String accessToken;

    public AccessTokenBean() {}

    public AccessTokenBean(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokenBean(String status, String msg, String responseMessage, String errorMessage, String accessToken) {
        super(status, msg, responseMessage, errorMessage);
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "AccessTokenBean{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}

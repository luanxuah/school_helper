package com.luanxu.bean;

import java.io.Serializable;

/**
 * @author: 范建海
 * @createTime: 2017/1/16 15:56
 * @className:  Bean
 * @description: 实体Bean基类
 * @changed by:
 */
public class Bean implements Serializable {
    // 1:成功 0:失败
    protected String code;
    // 状态码
    protected String status;
    // 提示信息
    protected String msg;
    protected String token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

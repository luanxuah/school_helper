package com.luanxu.bean;

import java.io.Serializable;

/**
 * 响应报文通用VO类
 * @param <T>
 */
public class GeneralResponseVo<T> implements Serializable {
    private static final long serialVersionUID = -6255127077515431892L;
    /**
     * 服务器端处理结果
     * 
     */
    private String code;
    /**
     * code值对应的含义
     * 
     */
    private String msg;
    /**
     * 登录状态
     * 
     */
    private String status;
    /**
     * token
     * 
     */
    private String token;
    /**
     * 响应数据
     * 
     */
    private T body;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}

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
    // 状态值
    protected String status;
    // 请求返回的相关信息
    protected String msg;
    // 请求返回的相关信息
    protected String responseMessage;
    // 请求错误的返回相关信息
    protected String errorMessage;

    public Bean() {}

    public Bean(String status, String msg, String responseMessage, String errorMessage) {
        this.status = status;
        this.msg = msg;
        this.responseMessage = responseMessage;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

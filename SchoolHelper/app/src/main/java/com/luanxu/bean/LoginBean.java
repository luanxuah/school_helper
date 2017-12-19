package com.luanxu.bean;

/**
 * @author: luanxu
 * @createTime: on 2017/6/27 18:21
 * @description: 登录的实体
 * @changed by:
 */
public class LoginBean extends Bean{
    //用户的id
    private String id;
    //用户的学号
    private String studentNumber;
    //用户的密码
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.luanxu.bean.user;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: luanxu
 * @createTime: on 2017/7/3 18:28
 * @description: 用户的实体
 * @changed by:
 */
@Entity
public class UserBean {
    //用户的id
    @Id
    private String id;
    //学号
    @Property(nameInDb = "STUDENT_NUMBER")
    private String studentNumber;
    //学校id（我自定义的无需关注）
    @Property(nameInDb = "SCHOOL_ID")
    private String schoolId;
    //学校名字
    @Property(nameInDb = "SCHOOL_NAME")
    private String schoolName;
    //学院
    @Property(nameInDb = "COLLEGE")
    private String college;
    //专业
    @Property(nameInDb = "CAREER")
    private String career;
    //年级
    @Property(nameInDb = "GRADE")
    private String grade;
    //学生姓名
    @Property(nameInDb = "NAME")
    private String name;
    //性别
    @Property(nameInDb = "SEX")
    private String sex;
    //民族
    @Property(nameInDb = "RACE")
    private String race;
    //联系电话
    @Property(nameInDb = "CONTACE_NUMBER")
    private String contaceNumber;
    //邮箱
    @Property(nameInDb = "EMAIL")
    private String email;
    //政治面貌
    @Property(nameInDb = "POLOTICE_STATUS")
    private String poloticeStatus;
    //班级名
    @Property(nameInDb = "CLASS_NAME")
    private String className;
    public String getClassName() {
        return this.className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getPoloticeStatus() {
        return this.poloticeStatus;
    }
    public void setPoloticeStatus(String poloticeStatus) {
        this.poloticeStatus = poloticeStatus;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContaceNumber() {
        return this.contaceNumber;
    }
    public void setContaceNumber(String contaceNumber) {
        this.contaceNumber = contaceNumber;
    }
    public String getRace() {
        return this.race;
    }
    public void setRace(String race) {
        this.race = race;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGrade() {
        return this.grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getCareer() {
        return this.career;
    }
    public void setCareer(String career) {
        this.career = career;
    }
    public String getCollege() {
        return this.college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getSchoolName() {
        return this.schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getSchoolId() {
        return this.schoolId;
    }
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
    public String getStudentNumber() {
        return this.studentNumber;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Generated(hash = 1527852180)
    public UserBean(String id, String studentNumber, String schoolId,
            String schoolName, String college, String career, String grade,
            String name, String sex, String race, String contaceNumber,
            String email, String poloticeStatus, String className) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.college = college;
        this.career = career;
        this.grade = grade;
        this.name = name;
        this.sex = sex;
        this.race = race;
        this.contaceNumber = contaceNumber;
        this.email = email;
        this.poloticeStatus = poloticeStatus;
        this.className = className;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
}

package com.luanxu.bean.syllabus;

/**
 * Created by luanxu on 2016/11/27.
 */
public class CourseBean {
    //课程名称
    private String name;
    //上课教室
    private String room;
    //教师
    private String teach;
    //课程编号
    private String id;
    //开始上课节次
    private int start;
    //一共几节课
    private int step;
    //课程的背景
    private int background;

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public CourseBean(String name, String room, int start, int step,
                      String teach, String id, int background) {
        super();
        this.name = name;
        this.room = room;
        this.start = start;
        this.step = step;
        this.teach = teach;

        this.id = id;
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTeach() {
        return teach;
    }

    public void setTeach(String teach) {
        this.teach = teach;
    }


}



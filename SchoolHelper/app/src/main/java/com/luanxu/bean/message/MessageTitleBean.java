package com.luanxu.bean.message;

import java.io.Serializable;

/**
 * @author: LuanXu
 * @createTime:2017/3/3 11:58
 * @className:  MessageTitleBean
 * @Description: 资讯列表频道实体
 */

public class MessageTitleBean  implements Serializable {
    private String title;
    private String id;
    private String select;

    public MessageTitleBean(String title, String id, String select){
        this.title = title;
        this.id = id;
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "MessageTitleBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", select='" + select + '\'' +
                '}';
    }
}

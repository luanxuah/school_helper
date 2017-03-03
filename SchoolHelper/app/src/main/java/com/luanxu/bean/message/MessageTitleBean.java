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

    public MessageTitleBean(String title, String id){
        this.title = title;
        this.id = id;
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

    @Override
    public String toString() {
        return "MessageTitleBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

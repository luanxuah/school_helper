package com.luanxu.bean;

import java.io.Serializable;

/**
 * @author: LiangYx
 * @ClassName: BottomMenuBean
 * @date: 2016年3月21日 下午2:35:32
 * @Description: 封装的底部菜单的实体
 * @Beautify:
 */
public class BottomMenuBean implements Serializable {
 
	private static final long serialVersionUID = -6176408100059466279L;
	
	public String id;
	public String content;
	
	public BottomMenuBean(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public BottomMenuBean() {
		super();
	} 
}

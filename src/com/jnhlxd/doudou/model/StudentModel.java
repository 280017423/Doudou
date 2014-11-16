package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;

/**
 * 
 * description 学生对象
 * 
 * @version 1.0
 * @author zousq
 * 
 */
public class StudentModel extends BaseModel {
	private static final long serialVersionUID = -343050591556595626L;
	private String child_id;
	private String class_id;
	private String headIcon;
	private String name;
	private String signId;

	public String getChild_id() {
		return child_id;
	}

	public void setChild_id(String child_id) {
		this.child_id = child_id;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

}

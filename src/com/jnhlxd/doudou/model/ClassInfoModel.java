package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.annotation.Transient;

public class ClassInfoModel extends BaseModel {
	private static final long serialVersionUID = -4949592679849136598L;
	private String class_id;
	private String class_name;
	@Transient
	private int isCurrentModel; // 是否是当前选择模式，1表示当前模式，0不是当前模式

	public String getClass_id() {
		return null == class_id ? "" : class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return null == class_name ? "" : class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public boolean isCurrentModel() {
		return 1 == isCurrentModel;
	}

	public void setCurrentModel(int isCurrentModel) {
		this.isCurrentModel = isCurrentModel;
	}

}

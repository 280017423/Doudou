package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.annotation.Transient;

public class ClassInfoModel extends BaseModel {
	private static final long serialVersionUID = -4949592679849136598L;
	private String classId;
	private String className;
	@Transient
	private int isCurrentModel; // 是否是当前选择模式，1表示当前模式，0不是当前模式

	public String getClassId() {
		return null == classId ? "" : classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return null == className ? "" : className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isCurrentModel() {
		return 1 == isCurrentModel;
	}

	public void setCurrentModel(int isCurrentModel) {
		this.isCurrentModel = isCurrentModel;
	}

}

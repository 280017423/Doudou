package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.annotation.Transient;

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
	public static final String KEY_CLASS_ID = "CLASS_ID";
	// 未打卡
	public static final int SIGN_TYPE_NOT_SIGN = 1;
	// 已打卡，但是未提交
	public static final int SIGN_TYPE_SIGNING = 2;
	// 已提交
	public static final int SIGN_TYPE_SIGNED = 3;
	private String childId;
	private String classId;
	private String headIcon;
	private String name;
	private String signId;
	@Transient
	private int signModelStatus = SIGN_TYPE_NOT_SIGN; // 指有没有存储到数据库
	@Transient
	private int signMode;

	public String getChildId() {
		return null == childId ? "" : childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getClassId() {
		return null == classId ? "" : classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getHeadIcon() {
		return null == headIcon ? "" : headIcon;
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
		return null == signId ? "" : signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public int getSignModelStatus() {
		return signModelStatus;
	}

	public void setSignModelStatus(int signModelStatus) {
		this.signModelStatus = signModelStatus;
	}

	public int getSignMode() {
		return signMode;
	}

	public void setSignMode(int signMode) {
		this.signMode = signMode;
	}

	@Override
	public String toString() {
		return "StudentModel [name=" + name + ", signModelStatus=" + signModelStatus + ", signMode=" + signMode + "]";
	}

}

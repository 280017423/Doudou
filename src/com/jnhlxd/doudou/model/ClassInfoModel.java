package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;

public class ClassInfoModel extends BaseModel {
	private static final long serialVersionUID = -4949592679849136598L;
	private int class_id;
	private String class_name;

	// 不能删除，ORM建表要用无参构造函数
	public ClassInfoModel() {
	}

	public ClassInfoModel(int keyId, String keyName) {
		setKeyId(keyId);
		setKeyName(keyName);
	}

	public int getKeyId() {
		return class_id;
	}

	public void setKeyId(int keyId) {
		this.class_id = keyId;
	}

	public String getKeyName() {
		return class_name;
	}

	public void setKeyName(String keyName) {
		this.class_name = keyName;
	}

}

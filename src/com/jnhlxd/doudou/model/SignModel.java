package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;

/**
 * 
 * description 考勤model，用来发送数据到服务器
 * 
 * @version 1.0
 * @author zousq
 * 
 */
public class SignModel extends BaseModel {

	public static final String SIGN_ID = "SIGN_ID";
	public static final String STATUS = "STATUS";
	public static final String SIGN_TIME = "SIGN_TIME";
	public static final String SIGN_MODE = "sign_Mode";
	public static final int SIGN_STATUS_NO_SENDED = 0;
	public static final int SIGN_STATUS_SENDED = 1;
	private static final long serialVersionUID = 5119585879353786153L;
	private String signId;
	private String signTime;
	private int signMode;
	private int status; // 已发送和未发送到服务器

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public int getSignMode() {
		return signMode;
	}

	public void setSignMode(int signMode) {
		this.signMode = signMode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

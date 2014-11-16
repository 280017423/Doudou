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
	public static final String SIGN_TIME = "SIGN_TIME";
	private static final long serialVersionUID = 5119585879353786153L;
	private String signId;
	private String signTime;
	private int signMode;

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

	@Override
	public String toString() {
		return "PunchModel [SignId=" + signId + ", SignTime=" + signTime + "]";
	}

}

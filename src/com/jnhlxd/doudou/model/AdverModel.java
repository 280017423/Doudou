package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;

public class AdverModel extends BaseModel {
	private static final long serialVersionUID = 543025377731340205L;
	private String img;
	private String mc;
	private String url;

	public String getImg() {
		return null == img ? "" : img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMc() {
		return null == mc ? "" : mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getUrl() {
		return null == url ? "" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

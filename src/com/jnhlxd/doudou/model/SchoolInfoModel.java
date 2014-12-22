package com.jnhlxd.doudou.model;

import com.qianjiang.framework.orm.BaseModel;

public class SchoolInfoModel extends BaseModel {

	private static final long serialVersionUID = 5023244831243099222L;
	private String name;
	private String address;
	private String tel;
	private String domain;
	private String schoolId;

	public String getName() {
		return null == name ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return null == address ? "" : address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

}

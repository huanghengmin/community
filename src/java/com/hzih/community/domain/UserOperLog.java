package com.hzih.community.domain;

import java.util.Date;

/**
 * 用户操作审计表
 * 
 * @author collin.code@gmail.com
 * @hibernate.class table="user_oper_log"
 */
public class UserOperLog {
	/**
	 * @hibernate.id column="Id" generator-class="identity"
	 *               type="java.lang.Long"
	 */
	Long id;

	/**
	 * 审计时间
	 *
	 * @hibernate.property column="log_time" type="java.util.Date"
	 */
	Date logTime;


	/**
	 *
	 */
	String logInfo;

	/**
	 *
	 */
	String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

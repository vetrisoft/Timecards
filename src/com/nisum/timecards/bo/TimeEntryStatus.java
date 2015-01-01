package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TMST_STATUS_MSTR_T")
public class TimeEntryStatus implements Serializable {

	private static final long serialVersionUID = 2316402700451343266L;

	@Id
	@Column(name = "STATUS_TYPE_CD")
	private String statusTypeCode;

	@Column(name = "STATUS_DESC")
	private String statusTypeDesc;

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public String getStatusTypeCode() {
		return statusTypeCode;
	}

	public void setStatusTypeCode(String statusTypeCode) {
		this.statusTypeCode = statusTypeCode;
	}

	public String getStatusTypeDesc() {
		return statusTypeDesc;
	}

	public void setStatusTypeDesc(String statusTypeDesc) {
		this.statusTypeDesc = statusTypeDesc;
	}

	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}

	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}

	public Date getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

}

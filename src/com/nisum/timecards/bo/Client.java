package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CLIENT_MSTR_T")
public class Client implements Serializable {

	private static final long serialVersionUID = -1332209758819937178L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLIENT_ID")
	private Integer clientId;

	@Column(name = "CLIENT_NAME")
	private String clientName;
	
	@Column(name = "CLIENT_LOCATION")
	private String clientLocation;
	
	@Column(name = "CLIENT_MANAGER")
	private String clientManager;

	@Column(name = "CLIENT_MAIL_ID")
	private String clientMailId;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public String getClientLocation() {
		return clientLocation;
	}

	public void setClientLocation(String clientLocation) {
		this.clientLocation = clientLocation;
	}

	public String getClientManager() {
		return clientManager;
	}

	public void setClientManager(String clientManager) {
		this.clientManager = clientManager;
	}

	public String getClientMailId() {
		return clientMailId;
	}

	public void setClientMailId(String clientMailId) {
		this.clientMailId = clientMailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
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
@Table(name = "CLIENT_HLDYS_MSTR_T")
public class ClientHolidays implements Serializable {

	private static final long serialVersionUID = 3897251629943532177L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLIENT_HLDY_ID")
	private Integer clientHolidayId;

	@Column(name = "CLIENT_ID")
	private Integer clientId;

	@Column(name = "HOLIDAY_ON")
	private Date holidayOn;

	@Column(name = "HOLIDAY_REASON")
	private String holidayOccasion;

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public Integer getClientHolidayId() {
		return clientHolidayId;
	}

	public void setClientHolidayId(Integer clientHolidayId) {
		this.clientHolidayId = clientHolidayId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Date getHolidayOn() {
		return holidayOn;
	}

	public void setHolidayOn(Date holidayOn) {
		this.holidayOn = holidayOn;
	}

	public String getHolidayOccasion() {
		return holidayOccasion;
	}

	public void setHolidayOccasion(String holidayOccasion) {
		this.holidayOccasion = holidayOccasion;
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

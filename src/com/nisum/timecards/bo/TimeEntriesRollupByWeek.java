package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(TimeEntriesRollupByWeekPK.class)
@Table(name = "EMP_TIME_ENTR_ROLLUP_BY_WEEK_T")
public class TimeEntriesRollupByWeek implements Serializable {

	private static final long serialVersionUID = -3840588798941167274L;

	@Id
	private Integer employeeId;

	@Id
	private Date timeEntryWeekEndDate;

	@Column(name = "WORK_ORDER_ID")
	private String workOrderId;

	@Column(name = "TOTAL_HOURS_FOR_WEEK")
	private Double totalHoursForWeek;

	@Column(name = "STATUS_TYPE_CD")
	private String hoursStatusTypeCode;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "HOURS_UPDATED")
	private String hoursUpdatedInd = "N";

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getTimeEntryWeekEndDate() {
		return timeEntryWeekEndDate;
	}

	public void setTimeEntryWeekEndDate(Date timeEntryWeekEndDate) {
		this.timeEntryWeekEndDate = timeEntryWeekEndDate;
	}

	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Double getTotalHoursForWeek() {
		return totalHoursForWeek;
	}

	public void setTotalHoursForWeek(Double totalHoursForWeek) {
		this.totalHoursForWeek = totalHoursForWeek;
	}

	public String getHoursStatusTypeCode() {
		return hoursStatusTypeCode;
	}

	public void setHoursStatusTypeCode(String hoursStatusTypeCode) {
		this.hoursStatusTypeCode = hoursStatusTypeCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getHoursUpdatedInd() {
		return hoursUpdatedInd;
	}

	public void setHoursUpdatedInd(String hoursUpdatedInd) {
		this.hoursUpdatedInd = hoursUpdatedInd;
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

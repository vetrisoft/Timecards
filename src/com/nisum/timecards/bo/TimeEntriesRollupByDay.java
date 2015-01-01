package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(TimeEntriesRollupByDayPK.class)
@Table(name = "EMP_TIME_ENTR_ROLLUP_BY_DAY_T")
public class TimeEntriesRollupByDay implements Serializable {

	private static final long serialVersionUID = -2763891458270812636L;

	@Id
	private Integer employeeId;

	@Id
	private Date timeEntryDate;

	@Column(name = "WORK_ORDER_ID")
	private String workOrderId;

	@Column(name = "HOURS_SUBMITTED")
	private Double hoursSubmitted;

	@Column(name = "STATUS_TYPE_CD")
	private String hoursStatusTypeCode;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "HOURS_UPDATED")
	private String hoursUpdatedInd = "N";

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "NON_BILLABLE_HOURS")
	private Double nonBillableHours;

	@Column(name = "NON_BILLABLE_REASON")
	private String nonBillableReason;

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

	public Date getTimeEntryDate() {
		return timeEntryDate;
	}

	public void setTimeEntryDate(Date timeEntryDate) {
		this.timeEntryDate = timeEntryDate;
	}

	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Double getHoursSubmitted() {
		return hoursSubmitted;
	}

	public void setHoursSubmitted(Double hoursSubmitted) {
		this.hoursSubmitted = hoursSubmitted;
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

	public Double getNonBillableHours() {
		return nonBillableHours;
	}

	public void setNonBillableHours(Double nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}

	public String getNonBillableReason() {
		return nonBillableReason;
	}

	public void setNonBillableReason(String nonBillableReason) {
		this.nonBillableReason = nonBillableReason;
	}

}

package com.nisum.timecards.dto;

import java.io.Serializable;
import java.util.Date;

public class TimeCardDayReportDTO implements Serializable  {
	
	private Integer employeeId;
	private String comments;
	private Date timeEntryDayDate;
	private Double hours;
	private String status;
	private Double nonBillableHours;
	private String nonBillableReson;
	
	public Date getTimeEntryDayDate() {
		return timeEntryDayDate;
	}
	public void setTimeEntryDayDate(Date timeEntryDayDate) {
		this.timeEntryDayDate = timeEntryDayDate;
		if (null != timeEntryDayDate) {
			this.timeEntryDayDate = new Date(timeEntryDayDate.getTime());
		}
	}
	public Double getHours() {
		return hours;
	}
	public void setHours(Double hours) {
		this.hours = hours;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	public Double getNonBillableHours() {
		return nonBillableHours;
	}
	public void setNonBillableHours(Double nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}
	public String getNonBillableReson() {
		return nonBillableReson;
	}
	public void setNonBillableReson(String nonBillableReson) {
		this.nonBillableReson = nonBillableReson;
	}
	
}

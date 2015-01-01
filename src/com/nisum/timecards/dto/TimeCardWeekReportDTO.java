package com.nisum.timecards.dto;

import java.io.Serializable;
import java.util.Date;

public class TimeCardWeekReportDTO implements Serializable{
	private String workOrderId;
	private Integer employeeId;
	private String firstName;
	private String lastName;
	private Double totalHoursForWeek;
	private Date timeEntryWeekEndDate;
	private String hoursStatusTypeCode;
	private String comments;
	private String statusTypeDesc;
	private String fullName;
	private String weekEndDate;
	private String statusTypeCode;

	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Double getTotalHoursForWeek() {
		return totalHoursForWeek;
	}

	public void setTotalHoursForWeek(Double totalHoursForWeek) {
		this.totalHoursForWeek = totalHoursForWeek;
	}

	public Date getTimeEntryWeekEndDate() {
		return timeEntryWeekEndDate;
	}

	public void setTimeEntryWeekEndDate(Date timeEntryWeekEndDate) {
		this.timeEntryWeekEndDate = timeEntryWeekEndDate;
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

	public String getStatusTypeDesc() {
		return statusTypeDesc;
	}

	public void setStatusTypeDesc(String statusTypeDesc) {
		this.statusTypeDesc = statusTypeDesc;
	}

	public String getWeekEndDate() {
		return weekEndDate;
	}

	public void setWeekEndDate(String weekEndDate) {
		this.weekEndDate = weekEndDate;
	}

	public String getFullName() {

		if (fullName != null) {
			return fullName;
		}
		return getFirstName() + " " + getLastName();
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStatusTypeCode() {
		return statusTypeCode;
	}

	public void setStatusTypeCode(String statusTypeCode) {
		this.statusTypeCode = statusTypeCode;
	}

	@Override
	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("workOrderId = " + workOrderId + ",");
		strBuffer.append("employeeId= " + employeeId + ",");
		strBuffer.append("firstName=" + firstName + ",");
		strBuffer.append("lastName= " + lastName + ",");
		strBuffer.append("totalHoursForWeek= " + totalHoursForWeek + ",");
		strBuffer.append("timeEntryWeekEndDate= " + timeEntryWeekEndDate + ",");
		strBuffer.append("hoursStatusTypeCode= " + hoursStatusTypeCode + ",");
		strBuffer.append("comments= " + comments + ",");
		strBuffer.append("statusTypeDesc= " + statusTypeDesc);
		return strBuffer.toString();
	}

}

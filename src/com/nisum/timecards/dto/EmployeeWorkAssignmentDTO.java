package com.nisum.timecards.dto;

import java.io.Serializable;
import java.util.Date;

public class EmployeeWorkAssignmentDTO implements Serializable {
	private int employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String assignmentId;
	private Date assignmentStartDate;
	private Date assignmentEndDate;

	public EmployeeWorkAssignmentDTO(String assignmentId, String employeeFirstName, String employeeLastName) {
		this.assignmentId = assignmentId;
		this.employeeFirstName = employeeFirstName;
		this.employeeLastName = employeeLastName;
	}

	public void setAssignmentStartAndEndDates(Date timeEntryDate) {
		// if last stored value is after the new date passed in, update it with new date passed in
		if (this.assignmentStartDate == null || this.assignmentStartDate.after(timeEntryDate)) {
			this.assignmentStartDate = timeEntryDate;
		}
		// if last stored value is before the new date passed in, update it with new date passed in
		if (this.assignmentEndDate == null || this.assignmentEndDate.before(timeEntryDate)) {
			this.assignmentEndDate = timeEntryDate;
		}
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public Date getAssignmentStartDate() {
		return assignmentStartDate;
	}

	public Date getAssignmentEndDate() {
		return assignmentEndDate;
	}
}

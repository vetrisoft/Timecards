package com.nisum.timecards.controller;

public class FilterForm {
	private String[] months = { "3", "6", "9", "12" };
	private String empName;
	private String monthOption;
	private String[] status;
	private String[] employeeId;
	private String filterStatus;
	private String[] comments;

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String[] getMonths() {
		return months;
	}

	public void setMonths(String[] months) {
		this.months = months;
	}

	public String getMonthOption() {
		return monthOption;
	}

	public void setMonthOption(String monthOption) {
		this.monthOption = monthOption;
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public String[] getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String[] employeeId) {
		this.employeeId = employeeId;
	}

	public String getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}

	public String[] getComments() {
		return comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}
}

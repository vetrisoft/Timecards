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
@Table(name = "EMP_TIME_ENTRIES_STG_T")
public class EmployeeTimeEntriesStaging implements Serializable {

	private static final long serialVersionUID = 184901665386415881L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIME_ENTRY_STG_ID")
	private Integer timeEntryStgId;

	@Column(name = "TIME_IMPORT_SEQ_ID")
	private Integer timeImportSeqId;

	@Column(name = "WORK_ORDER_ID")
	private String workOrderId;

	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	@Column(name = "TIME_ENTRY_DT")
	private Date timeEntryDate;

	@Column(name = "HOURS_SUBMITTED")
	private Double hoursSubmitted;

	@Column(name = "APPROVER_ID")
	private String approverId;

	@Column(name = "APPROVER_EMAIL")
	private String approverEmail;

	@Column(name = "TIME_ENTRY_PROC_IND")
	private String timeEntryProcInd = "N";

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public Integer getTimeEntryStgId() {
		return timeEntryStgId;
	}

	public void setTimeEntryStgId(Integer timeEntryStgId) {
		this.timeEntryStgId = timeEntryStgId;
	}

	public Integer getTimeImportSeqId() {
		return timeImportSeqId;
	}

	public void setTimeImportSeqId(Integer timeImportSeqId) {
		this.timeImportSeqId = timeImportSeqId;
	}

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

	public Date getTimeEntryDate() {
		return timeEntryDate;
	}

	public void setTimeEntryDate(Date timeEntryDate) {
		this.timeEntryDate = timeEntryDate;
	}

	public Double getHoursSubmitted() {
		return hoursSubmitted;
	}

	public void setHoursSubmitted(Double hoursSubmitted) {
		this.hoursSubmitted = hoursSubmitted;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverEmail() {
		return approverEmail;
	}

	public void setApproverEmail(String approverEmail) {
		this.approverEmail = approverEmail;
	}

	public String getTimeEntryProcInd() {
		return timeEntryProcInd;
	}

	public void setTimeEntryProcInd(String timeEntryProcInd) {
		this.timeEntryProcInd = timeEntryProcInd;
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

package com.nisum.timecards.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIME_ENTRY_IMPORT_LOG_T")
public class TimeEntryImportLog implements java.io.Serializable {

	private static final long serialVersionUID = -2329187955638313249L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIME_IMPORT_SEQ_ID", unique = true, nullable = false)
	private Integer id;

	@Column(name = "TIME_IMPORT_FILE_NAME")
	private String fileName;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIME_IMPORT_START_AT")
	private Date importStartDateTime;

	@Column(name = "TIME_IMPORT_COMPLETE_AT")
	private Date importCompletionDateTime;

	@Column(name = "CRT_USER_ID")
	private String createdUserId;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CRT_DTTM")
	private Date createdDateTime;

	@Column(name = "LST_UPDT_USER_ID")
	private String lastUpdatedUserId;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LST_UPDT_DTTM")
	private Date lastUpdatedDateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getImportStartDateTime() {
		return importStartDateTime;
	}

	public void setImportStartDateTime(Date importStartDateTime) {
		this.importStartDateTime = importStartDateTime;
	}

	public Date getImportCompletionDateTime() {
		return importCompletionDateTime;
	}

	public void setImportCompletionDateTime(Date importCompletionDateTime) {
		this.importCompletionDateTime = importCompletionDateTime;
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

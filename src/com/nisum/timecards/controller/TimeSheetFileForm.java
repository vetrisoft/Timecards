package com.nisum.timecards.controller;

import org.springframework.web.multipart.MultipartFile;

public class TimeSheetFileForm {

	private MultipartFile file;

	private String startDate;

	private String endDate;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}

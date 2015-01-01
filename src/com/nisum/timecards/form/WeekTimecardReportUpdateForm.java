package com.nisum.timecards.form;

import java.util.List;

import com.nisum.timecards.dto.TimeCardDayReportDTO;

public class WeekTimecardReportUpdateForm {
	
	List<TimeCardDayReportDTO> weekTimeCardReport;
	private String weekEndDate;

	public List<TimeCardDayReportDTO> getWeekTimeCardReport() {
		return weekTimeCardReport;
	}

	public void setWeekTimeCardReport(List<TimeCardDayReportDTO> weekTimeCardReport) {
		this.weekTimeCardReport = weekTimeCardReport;
	}

	public String getWeekEndDate() {
		return weekEndDate;
	}

	public void setWeekEndDate(String weekEndDate) {
		this.weekEndDate = weekEndDate;
	}
	
}

package com.nisum.timecards.service;

import java.util.Date;
import java.util.List;

import com.nisum.timecards.bo.NonBillableReasons;
import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;

public interface ReportsService {
	
	public List<Date> getAllTimeCardsWeekEndDates(String noOfMonths);
	public List<TimeCardWeekReportDTO> getAllTimeCards(String employeeName, String noOfMonths, String statusType);
	public boolean updateStatus(List<TimeCardWeekReportDTO> timeCardWeekReportDTOs);
	public List<TimeCardDayReportDTO> getDayTimeCards(String employeeId, String weekEndDate);
	public List<NonBillableReasons> getNonBillableReasons();
	public boolean updateDayTimecard(List<TimeCardDayReportDTO> timeCardDayReportDTOs, String weekEndDate, String userName);
	public String getEmployeName(Integer employeeId);

}

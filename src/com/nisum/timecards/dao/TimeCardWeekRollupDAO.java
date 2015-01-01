package com.nisum.timecards.dao;

import java.util.Date;
import java.util.List;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.NonBillableReasons;
import com.nisum.timecards.bo.TimeEntriesRollupByDay;
import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;

public interface TimeCardWeekRollupDAO {

	List<TimeCardWeekReportDTO> loadAllWeekTimeCardsData(String employeeName, String noOfMonths, String statusType);

	boolean updateTimeCardStatus(List<TimeCardWeekReportDTO> timeCardWeekReportDTOs, String userName);

	List<Date> getAllWeekendDates(String noOfMonths);

	List<TimeCardDayReportDTO> getDayTimeCards(String employeeId, String weekStartDate, String weekEndDate);

	List<NonBillableReasons> getNonBillableReasons();

	boolean updateDayTimecard(List<TimeEntriesRollupByDay> timeCardDayReportDTOs);

	TimeEntriesRollupByDay getTimeEntriesRollupByDay(Integer empId, Date submittedHoursDate);

	Employee getEmployeeDetails(Integer employeeId);
}
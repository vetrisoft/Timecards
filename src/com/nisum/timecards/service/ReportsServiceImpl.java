package com.nisum.timecards.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.NonBillableReasons;
import com.nisum.timecards.bo.TimeEntriesRollupByDay;
import com.nisum.timecards.dao.TimeCardWeekRollupDAO;
import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;
import com.nisum.timecards.util.DateFormatterUtil;

@Service
public class ReportsServiceImpl implements ReportsService {

	@Autowired
	private TimeCardWeekRollupDAO timeCardWeekRollupDAO;
	
	@Autowired
	private DateFormatterUtil dateUtil;

	@Override
	public List<TimeCardWeekReportDTO> getAllTimeCards(String employeeName, String noOfMonths, String statusType) {
		return timeCardWeekRollupDAO.loadAllWeekTimeCardsData(employeeName, noOfMonths, statusType);

	}

	public TimeCardWeekRollupDAO getReportsDAO() {
		return timeCardWeekRollupDAO;
	}

	public void setReportsDAO(TimeCardWeekRollupDAO reportsDAO) {
		this.timeCardWeekRollupDAO = reportsDAO;
	}

	@Override
	public boolean updateStatus(List<TimeCardWeekReportDTO> timeCardWeekReportDTOs) {
		return timeCardWeekRollupDAO.updateTimeCardStatus(timeCardWeekReportDTOs, null);
	}

	@Override
	public List<Date> getAllTimeCardsWeekEndDates(String noOfMonths) {
		return timeCardWeekRollupDAO.getAllWeekendDates(noOfMonths);
	}

	@Override
	public List<TimeCardDayReportDTO> getDayTimeCards(String employeeId,
			String weekEndDate) {
		String weekStartDate = null;
		Date parseDate = dateUtil.parseDate(weekEndDate, "yyyy-M-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate);
		calendar.add(Calendar.DATE, -6);
		weekStartDate = dateUtil.formatDate(calendar.getTime(), "yyyy-M-dd");
		return timeCardWeekRollupDAO.getDayTimeCards(employeeId, weekStartDate, weekEndDate);
	}

	@Override
	public List<NonBillableReasons> getNonBillableReasons() {
		return timeCardWeekRollupDAO.getNonBillableReasons();
	}

	@Override
	public boolean updateDayTimecard(List<TimeCardDayReportDTO> timeCardDayReportDTOs, String weekEndDate, String userName) {
		boolean result = false;
		List<TimeEntriesRollupByDay> tiRollupByDays = new ArrayList<TimeEntriesRollupByDay>();
		for (TimeCardDayReportDTO timeCardDayReportDTO : timeCardDayReportDTOs) {
			TimeEntriesRollupByDay currentDayTimecard = timeCardWeekRollupDAO.getTimeEntriesRollupByDay(timeCardDayReportDTO.getEmployeeId(), timeCardDayReportDTO.getTimeEntryDayDate());
			if (null != currentDayTimecard) {
				currentDayTimecard.setNonBillableHours(timeCardDayReportDTO.getNonBillableHours());
				if (!StringUtils.isBlank(timeCardDayReportDTO.getNonBillableReson())) {
					currentDayTimecard.setNonBillableReason(timeCardDayReportDTO.getNonBillableReson());
				} else {
					currentDayTimecard.setNonBillableReason(null);
				}
				currentDayTimecard.setComments(timeCardDayReportDTO.getComments());
				currentDayTimecard.setLastUpdatedUserId(userName);
				Calendar cal = Calendar.getInstance();
				currentDayTimecard.setLastUpdatedDateTime(cal.getTime());
				tiRollupByDays.add(currentDayTimecard);
				result = timeCardWeekRollupDAO.updateDayTimecard(tiRollupByDays);
			}
		}
		TimeCardWeekReportDTO timeCardWeekReportDto = getUpdateWeekTimeCardData(tiRollupByDays, weekEndDate);
		ArrayList<TimeCardWeekReportDTO> timeCardWeekReportDTOs = new ArrayList<TimeCardWeekReportDTO>();
		timeCardWeekReportDTOs.add(timeCardWeekReportDto);
		timeCardWeekRollupDAO.updateTimeCardStatus(timeCardWeekReportDTOs, userName);
		
		return result;
	}

	private TimeCardWeekReportDTO getUpdateWeekTimeCardData(List<TimeEntriesRollupByDay> tiRollupByDays, String weekEndDate) {
		TimeCardWeekReportDTO timeCardWeekReportDTO = new TimeCardWeekReportDTO();
		StringBuffer comments = new StringBuffer();
		Integer employeeId = null;
		String userId = "";
		String updatedDate = "";
		if (null != tiRollupByDays && tiRollupByDays.size() > 0) {
			userId = tiRollupByDays.get(0).getLastUpdatedUserId();
			updatedDate = dateUtil.formatDate(tiRollupByDays.get(0).getLastUpdatedDateTime(), "yyyy-M-dd");
			for (TimeEntriesRollupByDay timeEntriesRollupByDay : tiRollupByDays) {
				String dayCommnet = timeEntriesRollupByDay.getComments();
				Double nonHours = timeEntriesRollupByDay.getNonBillableHours();
				String nonBillReason = timeEntriesRollupByDay.getNonBillableReason();
				if (StringUtils.isBlank(nonBillReason)) {
					nonBillReason = "  ";
				}
				if (null != timeEntriesRollupByDay.getNonBillableHours() || null != timeEntriesRollupByDay.getNonBillableReason()) {
					comments.append("\n");
					comments.append(dateUtil.formatDate(timeEntriesRollupByDay.getTimeEntryDate(), "yyyy-M-dd") + " -- ");
					if (null == nonHours) {
						comments.append("   "  + " -- ");
					} else {
						comments.append(nonHours  + " -- ");
					}
					comments.append(nonBillReason  + " -- ");
					comments.append(dayCommnet);
				}
				
				if (null == employeeId) {
					employeeId = timeEntriesRollupByDay.getEmployeeId();
				}
			}
			comments.append("\n -- By " +userId + " on " + updatedDate);
		}
		boolean isNeedToUpdateStatus = isNeededUpdateWeekTimecardStatus(tiRollupByDays);
		if (isNeedToUpdateStatus) {
			timeCardWeekReportDTO.setStatusTypeCode("JS");
		} else {
			timeCardWeekReportDTO.setStatusTypeCode("BN");
		}
		timeCardWeekReportDTO.setEmployeeId(employeeId);
		timeCardWeekReportDTO.setWeekEndDate(weekEndDate);
		timeCardWeekReportDTO.setComments(comments.toString());
		return timeCardWeekReportDTO;
	}

	public DateFormatterUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateFormatterUtil dateUtil) {
		this.dateUtil = dateUtil;
	}
	
	private boolean isNeededUpdateWeekTimecardStatus(List<TimeEntriesRollupByDay> tiRollupByDays) {
		boolean isNeedToUpdateStatus = true;
		for (TimeEntriesRollupByDay timeEntriesRollupByDay : tiRollupByDays) {
			Double submittedHours = timeEntriesRollupByDay.getHoursSubmitted();
			Double nonBillableHours = timeEntriesRollupByDay.getNonBillableHours();
			if (null == nonBillableHours) {
				nonBillableHours = new Double(0);
			}
			Double totalHours =  submittedHours + nonBillableHours;
			if (totalHours < 8) {
				isNeedToUpdateStatus = false;
				break;
			}
		}
		return isNeedToUpdateStatus;
	}

	@Override
	public String getEmployeName(Integer employeeId) {
		Employee employee =  timeCardWeekRollupDAO.getEmployeeDetails(employeeId);
		String employeeName = employee.getFirstName() + " " +employee.getLastName();
		return employeeName;
	}
	
	
}

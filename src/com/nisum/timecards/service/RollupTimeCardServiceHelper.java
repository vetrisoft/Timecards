package com.nisum.timecards.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.EmployeeTimeEntriesStaging;
import com.nisum.timecards.bo.EmployeeWorkAssignment;
import com.nisum.timecards.bo.TimeEntriesRollupByDay;
import com.nisum.timecards.bo.TimeEntriesRollupByWeek;
import com.nisum.timecards.util.TimeCardConstants;

@Service
@SuppressWarnings("unchecked")
public class RollupTimeCardServiceHelper {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	// returns the date on week end (SATURDAY) for the given date
	public Date getWeekEndDateForGivenWorkingDay(Date workingDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(workingDay.getTime());
		cal.add(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK));
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return new Date(cal.getTimeInMillis());
	}

	public void rollupEmployeeTimeEntries(Employee employee, List<EmployeeWorkAssignment> employeeAssignmentList,
	        Map<Integer, Set<Date>> clientWorkingDaysMap,
	        Map<Integer, Map<Date, Integer>> clientIdWeekEndDateWorkingDaysMap, Date rollupStartDate,
	        Date rollupEndDate, String loggedInUser) {
		Integer employeeId = employee.getEmployeeId();
		EmployeeWorkAssignment someAssignment = employeeAssignmentList.get(0);
		String workOrderId = someAssignment.getWorkOrderId();

		Map<Date, Double> totalHoursSubmittedForWeek = new TreeMap<Date, Double>();

		// Set<Date> clientWorkingDaysDuringGivenAssignmentPeriod = getClientWorkingDaysDuringRollupPeriod(clientId,
		// rollupStartDate, rollupEndDate);
		Integer clientId = someAssignment.getClientId();
		Set<Date> clientWorkingDaysDuringGivenAssignmentPeriod = clientWorkingDaysMap.get(clientId);
		Set<Date> timeEntriesSubmittedDates = new HashSet<Date>();
		List<EmployeeTimeEntriesStaging> yetToBeRolledUpTimeEntriesList = getTimeEntriesToBeRolledUpForEmployee(
		        employeeId, rollupStartDate, rollupEndDate);
		List<EmployeeTimeEntriesStaging> processedStagingTimeEntriesList = new ArrayList<EmployeeTimeEntriesStaging>();

		for (EmployeeTimeEntriesStaging eachDayEntry : yetToBeRolledUpTimeEntriesList) {
			// TODO: this hack is to kind of convert the time returned from DB into application server time zone
			Date timeEntryDate = new Date(eachDayEntry.getTimeEntryDate().getTime());
			boolean regularWorkingDay = clientWorkingDaysDuringGivenAssignmentPeriod.contains(timeEntryDate);
			saveEmployeeTimeEntryForDay(employeeId, eachDayEntry, timeEntryDate, totalHoursSubmittedForWeek,
			        regularWorkingDay, loggedInUser);
			timeEntriesSubmittedDates.add(timeEntryDate);
			eachDayEntry.setTimeEntryProcInd(TimeCardConstants.HOURS_PROC_IND_YES);
			eachDayEntry.setLastUpdatedDateTime(new Date());
			eachDayEntry.setLastUpdatedUserId(loggedInUser);
			processedStagingTimeEntriesList.add(eachDayEntry);
		}
		// filter the days where time entries are submitted days from client working days
		clientWorkingDaysDuringGivenAssignmentPeriod.removeAll(timeEntriesSubmittedDates);
		// enter zero hours if time entries are not submitted for any client working day
		enterZeroHoursForAnyWorkingDayMissingTimeEntry(employeeId, workOrderId,
		        clientWorkingDaysDuringGivenAssignmentPeriod, totalHoursSubmittedForWeek, loggedInUser);
		// roll-up the daily hours submitted to the week
		rollupTimeEntriesForWeek(employeeId, workOrderId, clientId, totalHoursSubmittedForWeek,
		        clientIdWeekEndDateWorkingDaysMap, loggedInUser);
		hibernateTemplate.saveOrUpdateAll(processedStagingTimeEntriesList);
	}

	private List<EmployeeTimeEntriesStaging> getTimeEntriesToBeRolledUpForEmployee(Integer employeeId,
	        Date rollupStartDate, Date rollupEndDate) {
		DetachedCriteria timeEntriesLookupCriteria = DetachedCriteria.forClass(EmployeeTimeEntriesStaging.class);
		Criterion employeeIdCriteria = Restrictions.eq("employeeId", employeeId);
		Criterion processedIndCriteria = Restrictions.eq("timeEntryProcInd", TimeCardConstants.HOURS_PROC_IND_NO);

		Criterion timeEntrysStartingFrom = Restrictions.ge("timeEntryDate", rollupStartDate);
		DateTime rollupEndDateTime = new DateTime(rollupEndDate).withTime(0, 0, 0, 0);
		// increment the roll-up end date by 1 as we want to get records for the last day as well
		rollupEndDateTime.plusDays(1);
		Date timeEntriesTillDate = new Date(rollupEndDateTime.getMillis());
		Criterion timeEntriesTill = Restrictions.lt("timeEntryDate", timeEntriesTillDate);
		timeEntriesLookupCriteria.add(employeeIdCriteria).add(processedIndCriteria).add(timeEntrysStartingFrom)
		        .add(timeEntriesTill);

		return hibernateTemplate.findByCriteria(timeEntriesLookupCriteria);
	}

	private void saveEmployeeTimeEntryForDay(Integer employeeId, EmployeeTimeEntriesStaging eachDayEntry,
	        Date timeEntryDate, Map<Date, Double> totalHoursSubmittedForWeek, boolean regularWorkingDay,
	        String loggedInUser) {
		Date timeEntryWeekEndDate = getWeekEndDateForGivenWorkingDay(timeEntryDate);
		if (!totalHoursSubmittedForWeek.containsKey(timeEntryWeekEndDate)) {
			System.out.println("Creating entry for - " + timeEntryWeekEndDate);
			totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, Double.valueOf(0));
		}
		Double hoursPerWeekPostedSoFar = totalHoursSubmittedForWeek.get(timeEntryWeekEndDate);
		System.out.println(employeeId + ", week - " + timeEntryWeekEndDate + ", noOfHoursSoFar = "
		        + hoursPerWeekPostedSoFar);
		TimeEntriesRollupByDay timeEntryForDay = getTimeEntriesIfAlreadyExists(employeeId, timeEntryDate);
		String timeEntryWorkOrderId = eachDayEntry.getWorkOrderId();
		Double hoursSubmittedNow = eachDayEntry.getHoursSubmitted();
		Date sysDate = new Date();
		if (timeEntryForDay == null) {
			timeEntryForDay = new TimeEntriesRollupByDay();
			timeEntryForDay.setEmployeeId(employeeId);
			timeEntryForDay.setTimeEntryDate(timeEntryDate);
			timeEntryForDay.setWorkOrderId(timeEntryWorkOrderId);
			timeEntryForDay.setHoursSubmitted(hoursSubmittedNow);
			timeEntryForDay.setHoursStatusTypeCode(getTimeEntryStatusForTheDay(hoursSubmittedNow, regularWorkingDay));
			timeEntryForDay.setCreatedUserId(loggedInUser);
			timeEntryForDay.setLastUpdatedUserId(loggedInUser);
			timeEntryForDay.setCreatedDateTime(sysDate);
			timeEntryForDay.setLastUpdatedDateTime(sysDate);
			hibernateTemplate.save(timeEntryForDay);
			totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, hoursPerWeekPostedSoFar + hoursSubmittedNow);
			System.out.println(employeeId + ", week - " + timeEntryWeekEndDate + ", new total = "
			        + totalHoursSubmittedForWeek.get(timeEntryWeekEndDate));
		} else {
			boolean hoursUpdatedForTheDay = false;
			Double hoursSubmittedEarlier = timeEntryForDay.getHoursSubmitted();
			// if same work order but hours differ
			if (timeEntryForDay.getWorkOrderId().equalsIgnoreCase(timeEntryWorkOrderId)) {
				if (Double.compare(hoursSubmittedEarlier, hoursSubmittedNow) != 0) {
					hoursUpdatedForTheDay = true;
					timeEntryForDay.setHoursSubmitted(hoursSubmittedNow);
					hoursPerWeekPostedSoFar = hoursPerWeekPostedSoFar - hoursSubmittedEarlier;
				}
			} else if (Double.compare(hoursSubmittedNow, Double.valueOf(0.0)) != 0) {
				timeEntryForDay.setHoursSubmitted(hoursSubmittedEarlier + hoursSubmittedNow);
				if (Double.compare(hoursSubmittedEarlier, Double.valueOf(0.0)) == 0) {
					// use workOrderId from entry where hours submitted are NOT zero
					timeEntryForDay.setWorkOrderId(timeEntryWorkOrderId);
				}
				hoursUpdatedForTheDay = true;
			}

			if (hoursUpdatedForTheDay) {
				timeEntryForDay.setHoursStatusTypeCode(getTimeEntryStatusForTheDay(timeEntryForDay.getHoursSubmitted(),
				        regularWorkingDay));
				timeEntryForDay.setHoursUpdatedInd(TimeCardConstants.HOURS_UPDATED_YES);
				timeEntryForDay.setLastUpdatedUserId(loggedInUser);
				timeEntryForDay.setLastUpdatedDateTime(sysDate);
				hibernateTemplate.update(timeEntryForDay);
				totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, hoursPerWeekPostedSoFar + hoursSubmittedNow);
				System.out.println(employeeId + ", week - " + timeEntryWeekEndDate + ", new total = "
				        + totalHoursSubmittedForWeek.get(timeEntryWeekEndDate));
			}
		}
	}

	private TimeEntriesRollupByDay getTimeEntriesIfAlreadyExists(Integer employeeId, Date timeEntryDate) {
		TimeEntriesRollupByDay rollupByDay = new TimeEntriesRollupByDay();
		rollupByDay.setEmployeeId(employeeId);
		rollupByDay.setTimeEntryDate(timeEntryDate);
		// this null hack is to override default value of 'N' for this indicator which will introduce unnecessary filter
		rollupByDay.setHoursUpdatedInd(null);

		List<TimeEntriesRollupByDay> currentRollupByDayList = hibernateTemplate.findByExample(rollupByDay);
		return currentRollupByDayList.isEmpty() ? null : currentRollupByDayList.get(0);
	}

	private String getTimeEntryStatusForTheDay(Double hoursSubmitted, boolean regularWorkingDay) {
		if (!regularWorkingDay) {
			return hoursSubmitted > Double.valueOf(0.0) ? TimeCardConstants.TIME_ENTRY_STATUS_ABOVE_NORMAL
			        : TimeCardConstants.TIME_ENTRY_STATUS_NORMAL;
		}
		String timeEntryStatus;
		if (Double.compare(hoursSubmitted, Double.valueOf(TimeCardConstants.HOURS_PER_DAY)) < 0) {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL;
		} else if (Double.compare(hoursSubmitted, Double.valueOf(TimeCardConstants.HOURS_PER_DAY)) > 0) {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_ABOVE_NORMAL;
		} else {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_NORMAL;
		}
		return timeEntryStatus;
	}

	private void enterZeroHoursForAnyWorkingDayMissingTimeEntry(Integer employeeId, String workOrderId,
	        Set<Date> workingDaysForAssignment, Map<Date, Double> totalHoursSubmittedForWeek, String loggedInUser) {
		if (!workingDaysForAssignment.isEmpty()) {
			for (Date workingDay : workingDaysForAssignment) {
				Date timeEntryWeekEndDate = getWeekEndDateForGivenWorkingDay(workingDay);
				if (!totalHoursSubmittedForWeek.containsKey(timeEntryWeekEndDate)) {
					System.out.println("Entering ZERO HOUR entry for - " + timeEntryWeekEndDate);
					totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, Double.valueOf(0.0));
				}
				Double hoursSubmitted = Double.valueOf(0.0);
				TimeEntriesRollupByDay timeEntryForDay = getTimeEntriesIfAlreadyExists(employeeId, workingDay);
				if (timeEntryForDay == null) {
					timeEntryForDay = new TimeEntriesRollupByDay();
					timeEntryForDay.setEmployeeId(employeeId);
					timeEntryForDay.setTimeEntryDate(workingDay);
					timeEntryForDay.setWorkOrderId(workOrderId);
					timeEntryForDay.setHoursSubmitted(hoursSubmitted);
					timeEntryForDay.setHoursStatusTypeCode(getTimeEntryStatusForTheDay(hoursSubmitted, true));
					timeEntryForDay.setCreatedUserId(loggedInUser);
					timeEntryForDay.setLastUpdatedUserId(loggedInUser);
					Date sysDate = new Date();
					timeEntryForDay.setCreatedDateTime(sysDate);
					timeEntryForDay.setLastUpdatedDateTime(sysDate);
					hibernateTemplate.save(timeEntryForDay);
				} else {
					hoursSubmitted = timeEntryForDay.getHoursSubmitted();
				}
				Double hoursPerWeekPostedSoFar = totalHoursSubmittedForWeek.get(timeEntryWeekEndDate);
				totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, hoursPerWeekPostedSoFar + hoursSubmitted);
				System.out.println(employeeId + ", week - " + timeEntryWeekEndDate + ", new total = "
				        + totalHoursSubmittedForWeek.get(timeEntryWeekEndDate));
			}
		}
	}

	private void rollupTimeEntriesForWeek(Integer employeeId, String workOrderId, Integer clientId,
	        Map<Date, Double> totalHoursSubmittedForWeek,
	        Map<Integer, Map<Date, Integer>> clientIdWeekEndDateWorkingDaysMap, String loggedInUser) {
		Map<Date, Integer> weekEndDateTotalWorkingDaysMap = clientIdWeekEndDateWorkingDaysMap.get(clientId);
		for (Entry<Date, Double> eachWeekHours : totalHoursSubmittedForWeek.entrySet()) {
			Date weekEndDate = eachWeekHours.getKey();
			Double hoursSubmittedForTheWeek = eachWeekHours.getValue();
			TimeEntriesRollupByWeek timeEntriesForWeekEnding = getTimeEntriesForWeekEndingIfAlreadyExists(employeeId,
			        weekEndDate);
			Date sysDate = new Date();
			if (timeEntriesForWeekEnding == null) {
				String timeEntryStatusForTheWeek = getTimeEntryStatusForTheWeek(hoursSubmittedForTheWeek,
				        weekEndDateTotalWorkingDaysMap.get(weekEndDate));
				timeEntriesForWeekEnding = new TimeEntriesRollupByWeek();
				timeEntriesForWeekEnding.setEmployeeId(employeeId);
				timeEntriesForWeekEnding.setWorkOrderId(workOrderId);
				timeEntriesForWeekEnding.setTimeEntryWeekEndDate(weekEndDate);
				timeEntriesForWeekEnding.setHoursStatusTypeCode(timeEntryStatusForTheWeek);
				timeEntriesForWeekEnding.setTotalHoursForWeek(hoursSubmittedForTheWeek);
				timeEntriesForWeekEnding.setCreatedUserId(loggedInUser);
				timeEntriesForWeekEnding.setLastUpdatedUserId(loggedInUser);
				timeEntriesForWeekEnding.setCreatedDateTime(sysDate);
				timeEntriesForWeekEnding.setLastUpdatedDateTime(sysDate);
				hibernateTemplate.save(timeEntriesForWeekEnding);
			} else if (Double.compare(timeEntriesForWeekEnding.getTotalHoursForWeek(), hoursSubmittedForTheWeek) != 0) {
				String timeEntryStatusForTheWeek = getTimeEntryStatusForTheWeek(hoursSubmittedForTheWeek,
				        weekEndDateTotalWorkingDaysMap.get(weekEndDate));
				timeEntriesForWeekEnding.setTotalHoursForWeek(hoursSubmittedForTheWeek);
				timeEntriesForWeekEnding.setHoursStatusTypeCode(timeEntryStatusForTheWeek);
				timeEntriesForWeekEnding.setHoursUpdatedInd(TimeCardConstants.HOURS_UPDATED_YES);
				timeEntriesForWeekEnding.setLastUpdatedUserId(loggedInUser);
				timeEntriesForWeekEnding.setLastUpdatedDateTime(sysDate);
				hibernateTemplate.update(timeEntriesForWeekEnding);
			}
		}
	}

	private TimeEntriesRollupByWeek getTimeEntriesForWeekEndingIfAlreadyExists(Integer employeeId, Date weekEndDate) {
		TimeEntriesRollupByWeek rollupByWeek = new TimeEntriesRollupByWeek();
		rollupByWeek.setEmployeeId(employeeId);
		rollupByWeek.setTimeEntryWeekEndDate(weekEndDate);
		// this null hack is to override default value of 'N' for this indicator
		// which will introduce unnecessary filter
		rollupByWeek.setHoursUpdatedInd(null);

		List<TimeEntriesRollupByWeek> currentRollupByDayList = hibernateTemplate.findByExample(rollupByWeek);
		return currentRollupByDayList.isEmpty() ? null : currentRollupByDayList.get(0);
	}

	private String getTimeEntryStatusForTheWeek(Double hoursSubmitted, int noOfWorkingDaysInTheWeek) {
		String timeEntryStatus;
		int expectedWeekTotalHours = noOfWorkingDaysInTheWeek * TimeCardConstants.HOURS_PER_DAY;
		if (hoursSubmitted < Double.valueOf(expectedWeekTotalHours)) {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL;
		} else if (hoursSubmitted > Double.valueOf(expectedWeekTotalHours)) {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_ABOVE_NORMAL;
		} else {
			timeEntryStatus = TimeCardConstants.TIME_ENTRY_STATUS_NORMAL;
		}
		return timeEntryStatus;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTempl) {
		this.hibernateTemplate = hibernateTempl;
	}

}

package com.nisum.timecards.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.nisum.timecards.bo.Client;
import com.nisum.timecards.bo.ClientHolidays;
import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.EmployeeTimeEntriesStaging;
import com.nisum.timecards.bo.EmployeeWorkAssignment;
import com.nisum.timecards.bo.TimeEntriesRollupByDay;
import com.nisum.timecards.bo.TimeEntriesRollupByWeek;
import com.nisum.timecards.util.TimeCardConstants;

@Service
@SuppressWarnings("unchecked")
public class RollupTimeCardService {

	private final HibernateTemplate hibernateTemplate;
	private final RollupTimeCardServiceHelper rollupTimeCardServiceHelper;

	private List<Client> clientList;
	private List<Employee> employeeList;
	private Map<Integer, Set<Date>> clientWorkingDaysMap;
	private Map<Integer, Set<Date>> clientHolidaysMap;

	private Map<Integer, Map<Date, Integer>> clientIdWeekEndDateWorkingDaysMap;
	private Map<Integer, List<EmployeeWorkAssignment>> employeeIdWorkAssignmentList;

	@Autowired
	public RollupTimeCardService(RollupTimeCardServiceHelper rollupServiceHelper, HibernateTemplate hibernateTempl) {
		this.rollupTimeCardServiceHelper = rollupServiceHelper;
		this.hibernateTemplate = hibernateTempl;
	}

	public void rollupTimeCards(Integer importTimeCardSeqId, Date startDate, Date endDate) {
		loadConfigurationData(startDate, endDate);
		rollupTimeEntries(importTimeCardSeqId);
	}

	private void loadConfigurationData(Date startDate, Date endDate) {
		loadAllClients();
		loadAllEmployeesAndActiveAssignments(startDate, endDate);
		determineClientWorkingDays(startDate, endDate);
		loadClientHolidaysBetween(startDate, endDate);
		determineNoOfWorkingDaysInEachWeekForAllClients(startDate, endDate);
	}

	private void loadAllClients() {
		clientList = hibernateTemplate.find("from Client");
	}

	private void loadAllEmployeesAndActiveAssignments(Date startDate, Date endDate) {
		employeeList = hibernateTemplate.find("from Employee");
		employeeIdWorkAssignmentList = new TreeMap<Integer, List<EmployeeWorkAssignment>>();
		for (Employee employee : employeeList) {
			Integer employeeId = employee.getEmployeeId();
			// EmployeeWorkAssignment workAssignment = new EmployeeWorkAssignment();
			// workAssignment.setEmployeeId(employeeId);
			// List<EmployeeWorkAssignment> employeeAssignments = hibernateTemplate.findByExample(workAssignment);
			// employeeIdWorkAssignmentList.put(employeeId, employeeAssignments);

			// employee id equals condition
			Criterion employeeIdCriterion = Expression.eq("employeeId", employeeId);

			// assignment end date is greater than or equals to roll-up start date
			Criterion endDateCriteria = Expression.ge("assignmentEndDate", startDate);
			// assignment end date is not set (means it is still active)
			// Criterion isNull = Restrictions.isNull("assignmentEndDate");
			// assignment end date can be either undefined OR greater than equals to roll-up start date
			// Criterion assignmentEndCriterion = Restrictions.disjunction().add(greaterThanRollupPeriodStartDate);

			// final criteria - employee id equals AND (assignment end date is null OR greater than equals roll-up start
			// date)
			DetachedCriteria activeAssignmentsCriteria = DetachedCriteria.forClass(EmployeeWorkAssignment.class);
			activeAssignmentsCriteria.add(employeeIdCriterion).add(endDateCriteria);

			List<EmployeeWorkAssignment> employeeAssignments = hibernateTemplate
			        .findByCriteria(activeAssignmentsCriteria);
			employeeIdWorkAssignmentList.put(employeeId, employeeAssignments);
		}
	}

	// private void determineClientWorkingDays(Date startDate, Date endDate) {
	// clientWorkingDaysMap = new TreeMap<Integer, Set<Date>>();
	//
	// // get the regular working days during this roll-up period EXCLUDING SATURDAYs and SUNDAYs
	// Set<Date> normalWorkingDays = determineNormalWorkingDays(startDate, endDate);
	//
	// // get any client holidays during this roll-up period since working days are driven by client holidays rather
	// // than NISUM holidays
	// Map<Integer, Set<Date>> clientHolidaysMap = loadClientHolidaysBetween(startDate, endDate);
	//
	// // get final working days for each Client AFTER filtering any client specific holidays
	// for (Client client : clientList) {
	// Integer clientId = client.getClientId();
	// Set<Date> clientWorkingDays;
	// if (clientHolidaysMap.containsKey(clientId)) {
	// // if client has any holidays, filter those days from working days
	// clientWorkingDays = new HashSet<Date>(normalWorkingDays);
	// Set<Date> clientHolidays = clientHolidaysMap.get(clientId);
	// for (Date holidayOn : clientHolidays) {
	// if (clientWorkingDays.contains(holidayOn)) {
	// clientWorkingDays.remove(holidayOn);
	// } else {
	// System.out.println("Client working days list is missing the holiday on -" + holidayOn);
	// }
	// }
	// } else {
	// clientWorkingDays = normalWorkingDays;
	// }
	//
	// clientWorkingDaysMap.put(clientId, clientWorkingDays);
	// }
	// }

	private void determineClientWorkingDays(Date startDate, Date endDate) {
		clientWorkingDaysMap = new TreeMap<Integer, Set<Date>>();
		// get the regular working days during this roll-up period EXCLUDING SATURDAYs and SUNDAYs
		Set<Date> regularWorkingDays = new HashSet<Date>();
		DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
		DateTime aDayInRollupPeriod = new DateTime(startDate, defaultTimeZone).withTimeAtStartOfDay();
		DateTime endDateTime = new DateTime(endDate, defaultTimeZone).withTimeAtStartOfDay();

		while (aDayInRollupPeriod.getMillis() <= endDateTime.getMillis()) {
			if (aDayInRollupPeriod.getDayOfWeek() != DateTimeConstants.SATURDAY
			        && aDayInRollupPeriod.getDayOfWeek() != DateTimeConstants.SUNDAY) {
				regularWorkingDays.add(aDayInRollupPeriod.toDate());
			}
			aDayInRollupPeriod = aDayInRollupPeriod.plusDays(1);
		}
		for (Client client : clientList) {
			clientWorkingDaysMap.put(client.getClientId(), regularWorkingDays);
		}
	}

	// private Set<Date> determineNormalWorkingDays(Date startDate, Date endDate) {
	// Set<Date> normalWorkingDays = new HashSet<Date>();
	// DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
	// DateTime startDateTime = new DateTime(startDate, defaultTimeZone).withTimeAtStartOfDay();
	// DateTime endDateTime = new DateTime(endDate, defaultTimeZone).withTimeAtStartOfDay();
	//
	// DateTime tempDateTime = new DateTime(startDateTime);
	// while (tempDateTime.compareTo(endDateTime) <= 0) {
	// if (tempDateTime.getDayOfWeek() != DateTimeConstants.SATURDAY
	// && tempDateTime.getDayOfWeek() != DateTimeConstants.SUNDAY) {
	// normalWorkingDays.add(tempDateTime.toDate());
	// }
	// tempDateTime = tempDateTime.plusDays(1);
	// }
	// return normalWorkingDays;
	// }

	private Map<Integer, Set<Date>> loadClientHolidaysBetween(Date startDate, Date endDate) {
		clientHolidaysMap = new TreeMap<Integer, Set<Date>>();
		// criteria to get any holidays between roll-up start and end dates for ALL clients
		DetachedCriteria holidaysCriteria = DetachedCriteria.forClass(ClientHolidays.class).add(
		        Restrictions.between("holidayOn", startDate, endDate));
		List<ClientHolidays> holidaysList = hibernateTemplate.findByCriteria(holidaysCriteria);

		for (ClientHolidays holiday : holidaysList) {
			Integer clientId = holiday.getClientId();
			if (!clientHolidaysMap.containsKey(clientId)) {
				clientHolidaysMap.put(clientId, new HashSet<Date>());
			}
			Set<Date> clientHolidays = clientHolidaysMap.get(clientId);
			clientHolidays.add(new Date(new DateTime(holiday.getHolidayOn()).withTime(0, 0, 0, 0).getMillis()));
		}
		return clientHolidaysMap;
	}

	private void determineNoOfWorkingDaysInEachWeekForAllClients(Date startDate, Date endDate) {
		clientIdWeekEndDateWorkingDaysMap = new TreeMap<Integer, Map<Date, Integer>>();
		for (Client client : clientList) {
			Integer clientId = client.getClientId();
			Set<Date> workingDaysSet = clientWorkingDaysMap.get(clientId);
			Set<Date> holidaysSet = clientHolidaysMap.get(clientId);
			if (holidaysSet == null) {
				holidaysSet = new HashSet<Date>();
			}
			clientIdWeekEndDateWorkingDaysMap.put(clientId,
			        getWeekEndDateNoOfWorkingDaysMap(workingDaysSet, holidaysSet));
		}
	}

	private Map<Date, Integer> getWeekEndDateNoOfWorkingDaysMap(Set<Date> weekDaysSet, Set<Date> holidaysSet) {
		Map<Date, Integer> weekEndDateNoOfWorkingDaysMap = new TreeMap<Date, Integer>();
		Iterator<Date> workingDaysIterator = weekDaysSet.iterator();
		while (workingDaysIterator.hasNext()) {
			Date weekDay = workingDaysIterator.next();
			if (!holidaysSet.contains(weekDay)) {
				Date weekEndDateForGivenWorkingDay = rollupTimeCardServiceHelper
				        .getWeekEndDateForGivenWorkingDay(weekDay);
				if (!weekEndDateNoOfWorkingDaysMap.containsKey(weekEndDateForGivenWorkingDay)) {
					weekEndDateNoOfWorkingDaysMap.put(weekEndDateForGivenWorkingDay, 0);
				}
				Integer noOfWorkingDays = weekEndDateNoOfWorkingDaysMap.get(weekEndDateForGivenWorkingDay);
				weekEndDateNoOfWorkingDaysMap.put(weekEndDateForGivenWorkingDay, ++noOfWorkingDays);
			}
		}
		return weekEndDateNoOfWorkingDaysMap;
	}

	// private Map<Date, Integer> getWeekEndDateNoOfWorkingDaysMap(Set<Date> clientWorkingDays,
	// Date rollupPeriodStartDate, Date rollupPeriodEndDate) {
	// Map<Date, Integer> weekEndDateNoOfWorkingDaysMap = new TreeMap<Date, Integer>();
	// DateTimeZone defaultTimeZone = DateTimeZone.getDefault();
	// DateTime aDayInRollupPeriod = new DateTime(rollupPeriodStartDate, defaultTimeZone).withTimeAtStartOfDay();
	// DateTime rollupEndDate = new DateTime(rollupPeriodEndDate, defaultTimeZone).withTimeAtStartOfDay();
	// while (aDayInRollupPeriod.getMillis() <= rollupEndDate.getMillis()) {
	// if(clientHolidaysMap.containsKey(null)) {
	//
	// }
	// if (clientWorkingDays.contains(new Date(aDayInRollupPeriod.getMillis()))) {
	// Date weekEndDateForGivenWorkingDay = rollupTimeCardServiceHelper
	// .getWeekEndDateForGivenWorkingDay(new Date(aDayInRollupPeriod.getMillis()));
	// if (!weekEndDateNoOfWorkingDaysMap.containsKey(weekEndDateForGivenWorkingDay)) {
	// weekEndDateNoOfWorkingDaysMap.put(weekEndDateForGivenWorkingDay, 0);
	// }
	// Integer noOfWorkingDays = weekEndDateNoOfWorkingDaysMap.get(weekEndDateForGivenWorkingDay);
	// weekEndDateNoOfWorkingDaysMap.put(weekEndDateForGivenWorkingDay, ++noOfWorkingDays);
	// }
	// aDayInRollupPeriod = aDayInRollupPeriod.plusDays(1);
	// }
	// return weekEndDateNoOfWorkingDaysMap;
	// }

	private void rollupTimeEntries(Integer importTimeCardSeqId) {
		String loggedInUser = getLoggedInUser();
		for (Employee employee : employeeList) {
			Integer employeeId = employee.getEmployeeId();
			List<EmployeeWorkAssignment> employeeAssignmentList = employeeIdWorkAssignmentList.get(employeeId);
			if (employeeAssignmentList.isEmpty()) {
				System.out.println("No assignments found for employee - " + employeeId + ", skipping...");
				continue;
			}
			EmployeeWorkAssignment someAssignment = employeeAssignmentList.get(0);
			String workOrderId = someAssignment.getWorkOrderId();

			Map<Date, Double> totalHoursSubmittedForWeek = new TreeMap<Date, Double>();

			Integer clientId = someAssignment.getClientId();
			Set<Date> clientWorkingDaysDuringGivenAssignmentPeriod = clientWorkingDaysMap.get(clientId);
			Set<Date> timeEntriesSubmittedDates = new HashSet<Date>();
			List<EmployeeTimeEntriesStaging> yetToBeRolledUpTimeEntriesList = getTimeEntriesToBeRolledUpForEmployee(
			        employeeId, importTimeCardSeqId);

			for (EmployeeTimeEntriesStaging eachDayEntry : yetToBeRolledUpTimeEntriesList) {
				// TODO: this hack is to kind of convert the time returned from DB into application server time zone
				Date timeEntryDate = new Date(eachDayEntry.getTimeEntryDate().getTime());
				boolean regularWorkingDay = clientWorkingDaysDuringGivenAssignmentPeriod.contains(timeEntryDate);
				saveEmployeeTimeEntryForDay(employeeId, eachDayEntry, timeEntryDate, totalHoursSubmittedForWeek,
				        regularWorkingDay, loggedInUser);
				timeEntriesSubmittedDates.add(timeEntryDate);
			}
			Set<Date> workingDaysMissingTimeEntries = new HashSet<Date>(clientWorkingDaysDuringGivenAssignmentPeriod);
			workingDaysMissingTimeEntries.removeAll(timeEntriesSubmittedDates);

			// enter zero hours if time entries are not submitted for any client working day
			if (!workingDaysMissingTimeEntries.isEmpty()) {
				enterZeroHoursForAnyWorkingDayMissingTimeEntry(employeeId, workOrderId, workingDaysMissingTimeEntries,
				        totalHoursSubmittedForWeek, loggedInUser);
			}
			Map<Date, Integer> weekEndDateNoOfWorkingDaysMap = clientIdWeekEndDateWorkingDaysMap.get(clientId);
			// roll-up the daily hours submitted to the week
			rollupTimeEntriesForWeek(employeeId, workOrderId, loggedInUser, totalHoursSubmittedForWeek,
			        weekEndDateNoOfWorkingDaysMap);
		}
	}

	private String getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		return currentUser.getUsername();
	}

	private List<EmployeeTimeEntriesStaging> getTimeEntriesToBeRolledUpForEmployee(Integer employeeId,
	        Integer importTimeCardSeqId) {
		DetachedCriteria employeeTimeEntriesCriteria = DetachedCriteria.forClass(EmployeeTimeEntriesStaging.class);
		employeeTimeEntriesCriteria.add(Restrictions.eq("employeeId", employeeId));
		employeeTimeEntriesCriteria.add(Restrictions.eq("timeImportSeqId", importTimeCardSeqId));
		return hibernateTemplate.findByCriteria(employeeTimeEntriesCriteria);
	}

	private void saveEmployeeTimeEntryForDay(Integer employeeId, EmployeeTimeEntriesStaging eachDayEntry,
	        Date timeEntryDate, Map<Date, Double> totalHoursSubmittedForWeek, boolean regularWorkingDay,
	        String loggedInUser) {
		Date timeEntryWeekEndDate = rollupTimeCardServiceHelper.getWeekEndDateForGivenWorkingDay(timeEntryDate);
		if (!totalHoursSubmittedForWeek.containsKey(timeEntryWeekEndDate)) {
			totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, Double.valueOf(0));
		}
		Double hoursPerWeekPostedSoFar = totalHoursSubmittedForWeek.get(timeEntryWeekEndDate);
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
			}
			totalHoursSubmittedForWeek.put(timeEntryWeekEndDate, hoursPerWeekPostedSoFar + hoursSubmittedNow);
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
	        Set<Date> workingDaysMissingTimeEntry, Map<Date, Double> totalHoursSubmittedForWeek, String loggedInUser) {
		for (Date workingDay : workingDaysMissingTimeEntry) {
			Date timeEntryWeekEndDate = rollupTimeCardServiceHelper.getWeekEndDateForGivenWorkingDay(workingDay);
			if (!totalHoursSubmittedForWeek.containsKey(timeEntryWeekEndDate)) {
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
		}
	}

	private void rollupTimeEntriesForWeek(Integer employeeId, String workOrderId, String loggedInUser,
	        Map<Date, Double> totalHoursSubmittedForWeek, Map<Date, Integer> weekEndDateTotalWorkingDaysMap) {
		for (Entry<Date, Double> eachWeekHours : totalHoursSubmittedForWeek.entrySet()) {
			Date weekEndDate = eachWeekHours.getKey();
			TimeEntriesRollupByWeek timeEntriesForWeekEnding = getTimeEntriesForWeekEndingIfAlreadyExists(employeeId,
			        weekEndDate);
			Integer noOfWorkingDaysInTheWeek = weekEndDateTotalWorkingDaysMap.get(weekEndDate);
			Double hoursSubmittedForTheWeek = eachWeekHours.getValue();
			String timeEntryStatusForTheWeek = getTimeEntryStatusForTheWeek(hoursSubmittedForTheWeek,
			        noOfWorkingDaysInTheWeek);
			Date sysDate = new Date();
			if (timeEntriesForWeekEnding == null) {
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
		// this null hack is to override default value of 'N' for this indicator which will introduce unnecessary filter
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
}

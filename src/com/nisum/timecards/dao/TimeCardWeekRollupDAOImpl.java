package com.nisum.timecards.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.NonBillableReasons;
import com.nisum.timecards.bo.TimeEntriesRollupByDay;
import com.nisum.timecards.bo.TimeEntriesRollupByWeek;
import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;
import com.nisum.timecards.util.DateFormatterUtil;
import com.nisum.timecards.util.TimeCardConstants;

@SuppressWarnings("unchecked")
@Repository
public class TimeCardWeekRollupDAOImpl implements TimeCardWeekRollupDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private DateFormatterUtil dateUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public List<TimeCardWeekReportDTO> loadAllWeekTimeCardsData(String employeeName, String noOfMonths,
	        String statusType) {

		String empNameLikeCriteria = "";
		String statusCriteria = "";
		String dateCriteria = "";
		if (statusType != null && !StringUtils.equalsIgnoreCase("all", statusType)) {
			statusCriteria = " AND EMPROLLUPWEEK.STATUS_TYPE_CD = '" + statusType + "'";
		}

		// if (StringUtils.isBlank(noOfMonths)) {
		// dateCriteria = " AND (DATE_SUB(NOW(), INTERVAL 3 MONTH) <= EMPROLLUPWEEK.TE_FOR_WEEK_ENDING)";
		// } else
		if (noOfMonths != null && !StringUtils.equalsIgnoreCase("all", noOfMonths)) {
			dateCriteria = " AND (DATE_SUB(NOW(), INTERVAL " + noOfMonths
			        + " MONTH) <= EMPROLLUPWEEK.TE_FOR_WEEK_ENDING)";
		}

		if (StringUtils.isNotBlank(employeeName)) {
			empNameLikeCriteria = " AND (EMP.FIRST_NAME LIKE '%" + employeeName + "%' or EMP.LAST_NAME LIKE '%"
			        + employeeName + "%') ";
		}
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String orderByClause = " ORDER BY EMP.FIRST_NAME, EMP.LAST_NAME, EMPROLLUPWEEK.TE_FOR_WEEK_ENDING";
		String selectQuery = "SELECT EMPROLLUPWEEK.WORK_ORDER_ID, EMP.EMPLOYEE_ID, EMP.FIRST_NAME, "
		        + "EMP.LAST_NAME, EMPROLLUPWEEK.TOTAL_HOURS_FOR_WEEK, EMPROLLUPWEEK.TE_FOR_WEEK_ENDING, "
		        + "EMPROLLUPWEEK.STATUS_TYPE_CD, EMPROLLUPWEEK.COMMENTS, TMSTSTATUS.STATUS_DESC "
		        + "FROM EMP_TIME_ENTR_ROLLUP_BY_WEEK_T EMPROLLUPWEEK, EMPLOYEE_MSTR_T EMP, "
		        + "TMST_STATUS_MSTR_T TMSTSTATUS WHERE EMPROLLUPWEEK.EMPLOYEE_ID = EMP.EMPLOYEE_ID AND "
		        + "EMPROLLUPWEEK.STATUS_TYPE_CD = TMSTSTATUS.STATUS_TYPE_CD " + empNameLikeCriteria + dateCriteria
		        + statusCriteria + orderByClause;

		System.out.println("selectQuery::::::::::::"+selectQuery.toString());
		Query query = session.createSQLQuery(selectQuery);

		List list = query.list();

		List<TimeCardWeekReportDTO> timeCardList = new ArrayList<TimeCardWeekReportDTO>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TimeCardWeekReportDTO timeCardWeekReportDTO = new TimeCardWeekReportDTO();
			Object[] object = (Object[]) iterator.next();
			if (null != object[0]) {
				timeCardWeekReportDTO.setWorkOrderId(object[0].toString());
			}
			if (null != object[1]) {
				timeCardWeekReportDTO.setEmployeeId(new Integer(object[1].toString()));
			}
			if (null != object[2]) {
				timeCardWeekReportDTO.setFirstName(object[2].toString());
			}
			if (null != object[3]) {
				timeCardWeekReportDTO.setLastName(object[3].toString());
			}
			if (null != object[4]) {
				timeCardWeekReportDTO.setTotalHoursForWeek(new Double(object[4].toString()));
			}
			if (null != object[5]) {
				timeCardWeekReportDTO.setTimeEntryWeekEndDate((Date) object[5]);
			}
			if (null != object[6]) {
				timeCardWeekReportDTO.setHoursStatusTypeCode(object[6].toString());
			}
			if (null != object[7]) {
				timeCardWeekReportDTO.setComments(object[7].toString());
			}
			if (null != object[8]) {
				timeCardWeekReportDTO.setStatusTypeDesc(object[8].toString());
			}
			timeCardList.add(timeCardWeekReportDTO);
		}

		session.close();
		return timeCardList;
	}

	@Override
	public boolean updateTimeCardStatus(List<TimeCardWeekReportDTO> timeCardWeekReportDTOs, String userName) {
		for (TimeCardWeekReportDTO timeCardWeekReportDTO : timeCardWeekReportDTOs) {
			TimeEntriesRollupByWeek timeEntriesRollupByWeek = new TimeEntriesRollupByWeek();
			timeEntriesRollupByWeek.setEmployeeId(timeCardWeekReportDTO.getEmployeeId());
			timeEntriesRollupByWeek.setTimeEntryWeekEndDate(dateUtil.parseDate(timeCardWeekReportDTO.getWeekEndDate(),
			        "yyyy-M-dd"));
			List<TimeEntriesRollupByWeek> currrentTimeEntriesRollupByWeek = hibernateTemplate
			        .findByExample(timeEntriesRollupByWeek);
			if (null != currrentTimeEntriesRollupByWeek && currrentTimeEntriesRollupByWeek.size() > 0) {
				timeEntriesRollupByWeek = currrentTimeEntriesRollupByWeek.get(0);
			}
			if (!StringUtils.isBlank(timeCardWeekReportDTO.getStatusTypeCode())) {
				timeEntriesRollupByWeek.setHoursStatusTypeCode(timeCardWeekReportDTO.getStatusTypeCode());
			}
			timeEntriesRollupByWeek.setLastUpdatedUserId(userName);
			Calendar cal = Calendar.getInstance();
			timeEntriesRollupByWeek.setLastUpdatedDateTime(cal.getTime());
			String comments = timeCardWeekReportDTO.getComments();
			if (timeEntriesRollupByWeek.getComments() != null) {
				comments = comments + "\n" + timeEntriesRollupByWeek.getComments();
			}
			if (comments.length() > TimeCardConstants.COMMENTS_MAX_LENGTH) {
				comments = comments.substring(0, TimeCardConstants.COMMENTS_MAX_LENGTH);
			}
			timeEntriesRollupByWeek.setComments(comments);
			hibernateTemplate.saveOrUpdate(timeEntriesRollupByWeek);
		}
		return true;
	}

	@Override
	public List<Date> getAllWeekendDates(String noOfMonths) {
		String dateCriteria = "";
		// if (StringUtils.isEmpty(noOfMonths)) {
		// dateCriteria = " WHERE (DATE_SUB(NOW(), INTERVAL 3 MONTH) <= empRollupWeek.TE_FOR_WEEK_ENDING)";
		// } else

		if (noOfMonths != null && !StringUtils.equalsIgnoreCase("all", noOfMonths)) {
			dateCriteria = " WHERE (DATE_SUB(NOW(), INTERVAL " + noOfMonths
			        + " MONTH) <= EMPROLLUPWEEK.TE_FOR_WEEK_ENDING)";
		}
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String getAllWeekEndsQuery = "SELECT DISTINCT DATE(EMPROLLUPWEEK.TE_FOR_WEEK_ENDING) FROM EMP_TIME_ENTR_ROLLUP_BY_WEEK_T EMPROLLUPWEEK ";
		Query query = session.createSQLQuery(getAllWeekEndsQuery + dateCriteria + " ORDER BY TE_FOR_WEEK_ENDING");
		List<Date> weekendList = query.list();
		session.close();
		return weekendList;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public List<TimeCardDayReportDTO> getDayTimeCards(String employeeId, String weekStartDate, String weekEndDate) {

		String selectQuery = "SELECT EMP.EMPLOYEE_ID, EMP.TIME_ENTRY_DT, EMP.HOURS_SUBMITTED,EMP.STATUS_TYPE_CD, EMP.COMMENTS,"
		        + " EMP.NON_BILLABLE_HOURS ,EMP.NON_BILLABLE_REASON FROM EMP_TIME_ENTR_ROLLUP_BY_DAY_T EMP WHERE EMP.EMPLOYEE_ID =  "
		        + employeeId
		        + " AND EMP.TIME_ENTRY_DT >= '"
		        + weekStartDate
		        + "' AND EMP.TIME_ENTRY_DT <= '"
		        + weekEndDate + "' ORDER BY EMP.TIME_ENTRY_DT";

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(selectQuery);

		List list = query.list();

		List<TimeCardDayReportDTO> timeCardList = new ArrayList<TimeCardDayReportDTO>();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TimeCardDayReportDTO timeCardDayReportDTO = new TimeCardDayReportDTO();
			Object[] object = (Object[]) iterator.next();
			if (null != object[0]) {
				timeCardDayReportDTO.setEmployeeId(new Integer(object[0].toString()));
			}
			if (null != object[1]) {
				timeCardDayReportDTO.setTimeEntryDayDate((Date) object[1]);
			}
			if (null != object[2]) {
				timeCardDayReportDTO.setHours(new Double(object[2].toString()));
			}
			if (null != object[3]) {
				timeCardDayReportDTO.setStatus(object[3].toString());
			}
			if (null != object[4]) {
				timeCardDayReportDTO.setComments(object[4].toString());
			}
			if (null != object[5]) {
				timeCardDayReportDTO.setNonBillableHours(new Double(object[5].toString()));
			}
			if (null != object[6]) {
				timeCardDayReportDTO.setNonBillableReson(object[6].toString());
			}

			timeCardList.add(timeCardDayReportDTO);
		}
		session.close();
		return timeCardList;
	}

	@Override
	public List<NonBillableReasons> getNonBillableReasons() {
		return hibernateTemplate.loadAll(NonBillableReasons.class);
	}

	@Override
	public boolean updateDayTimecard(List<TimeEntriesRollupByDay> timeCardDayReportDTOs) {
		hibernateTemplate.saveOrUpdateAll(timeCardDayReportDTOs);
		return true;
	}

	@Override
	public TimeEntriesRollupByDay getTimeEntriesRollupByDay(Integer empId, Date submittedHoursDate) {
		TimeEntriesRollupByDay timeEntriesRollupByDay = null;
		TimeEntriesRollupByDay timeCardDayDTO = new TimeEntriesRollupByDay();
		timeCardDayDTO.setEmployeeId(empId);
		timeCardDayDTO.setTimeEntryDate(submittedHoursDate);
		List<TimeEntriesRollupByDay> currentDayTimecardList = hibernateTemplate.findByExample(timeCardDayDTO);
		if (null != currentDayTimecardList && currentDayTimecardList.size() > 0) {
			timeEntriesRollupByDay = currentDayTimecardList.get(0);
		}
		return timeEntriesRollupByDay;
	}

	@Override
	public Employee getEmployeeDetails(Integer employeeId) {
		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		Employee employeeDetails = (Employee) hibernateTemplate.get(Employee.class, employeeId);
		return employeeDetails;
	}

	public DateFormatterUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateFormatterUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

}

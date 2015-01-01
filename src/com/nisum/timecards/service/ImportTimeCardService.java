package com.nisum.timecards.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.bo.EmployeeTimeEntriesStaging;
import com.nisum.timecards.bo.EmployeeWorkAssignment;
import com.nisum.timecards.bo.TimeEntryImportLog;
import com.nisum.timecards.dto.EmployeeWorkAssignmentDTO;
import com.nisum.timecards.dto.TimeCardReportDTO;
import com.nisum.timecards.util.POIFileReader;
import com.nisum.timecards.util.TimeCardConstants;

@Service
@SuppressWarnings("unchecked")
public class ImportTimeCardService {

	private final POIFileReader fileReader;
	private final HibernateTemplate hibernateTemplate;

	@Autowired
	public ImportTimeCardService(POIFileReader fileReader, HibernateTemplate hibernateTempl) {
		this.fileReader = fileReader;
		this.hibernateTemplate = hibernateTempl;
	}

	public Map<String, Object> importTimeCards(MultipartFile file, Integer clientId) throws Exception {
		System.out.println("inside the importTimeCards!!");
		String loggedInUser = getLoggedInUser();
		TimeEntryImportLog timeEntryImportLog = logTimeEntriesImportStartTime(file.getOriginalFilename(), loggedInUser);
		dumpTimeEntriesIntoStagingTable(file, timeEntryImportLog.getId(), clientId, loggedInUser);
		logTimeEntriesImportEndTime(timeEntryImportLog, loggedInUser);
		Map<String, Object> timeEntryImportDetails = new HashMap<String, Object>();
		timeEntryImportDetails.put(TimeCardConstants.TIME_IMPORT_SEQ_ID, timeEntryImportLog.getId());
		timeEntryImportDetails.put(TimeCardConstants.ROLLUP_PERIOD_START, getTimeEntriesStartDate(file));
		timeEntryImportDetails.put(TimeCardConstants.ROLLUP_PERIOD_END, getTimeEntriesEndDate(file));
		return timeEntryImportDetails;
	}

	private TimeEntryImportLog logTimeEntriesImportStartTime(String fileName, String loggedInUser) {
		System.out.println("inside the logTimeEntriesImportStartTime!!!");
		TimeEntryImportLog log = new TimeEntryImportLog();
		log.setFileName(fileName);
		log.setCreatedUserId(loggedInUser);
		log.setLastUpdatedUserId(loggedInUser);
		Date sysDate = new Date();
		log.setCreatedDateTime(sysDate);
		log.setLastUpdatedDateTime(sysDate);
		hibernateTemplate.save(log);
		hibernateTemplate.refresh(log);
		return log;
	}

	private String getLoggedInUser() {
		System.out.println("inside the getLoggedInUser!!");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		return currentUser.getUsername();
	}

	private Date getTimeEntriesStartDate(MultipartFile file) throws Exception {
		return fileReader.getRollupPeriodStartDate(file);
	}

	private Date getTimeEntriesEndDate(MultipartFile file) throws Exception {
		return fileReader.getRollupPeriodEndDate(file);
	}

	private void dumpTimeEntriesIntoStagingTable(MultipartFile file, Integer timeImportSeqId, Integer clientId,
	        String loggedInUser) {
		System.out.println("Inside the dumpTimeEntriesIntoStagingTable!!!.");
		List<TimeCardReportDTO> timecardReportDtoList = null;
		try {
			timecardReportDtoList = fileReader.getData(file);
		} catch (Exception e) {
			System.out.println("Aborting the Dumping contents of the file into Staging table.." + e.getMessage());
			e.printStackTrace();
			return;
		}

		Map<String, EmployeeWorkAssignmentDTO> assignmentDetailsMap = constructEmployeeAndAssignmentDetailsMap(timecardReportDtoList);
		createEmployeeIfNotAlreadyCreated(assignmentDetailsMap, loggedInUser);
		insertOrUpdateEmployeeWorkAssignments(assignmentDetailsMap, clientId, loggedInUser);
		createStagingTimeEntries(timeImportSeqId, timecardReportDtoList, assignmentDetailsMap, loggedInUser);
	}

	private Map<String, EmployeeWorkAssignmentDTO> constructEmployeeAndAssignmentDetailsMap(
	        List<TimeCardReportDTO> timecardReportDtoList) {
		Map<String, EmployeeWorkAssignmentDTO> assignmentDetailsMap = new LinkedHashMap<String, EmployeeWorkAssignmentDTO>();
		for (TimeCardReportDTO timeCardEntry : timecardReportDtoList) {
			String assignmentId = timeCardEntry.getAsssignmentId();
			if (!assignmentDetailsMap.containsKey(assignmentId)) {
				EmployeeWorkAssignmentDTO workAssignmentDto = new EmployeeWorkAssignmentDTO(assignmentId,
				        timeCardEntry.getFirstName(), timeCardEntry.getLastName());
				assignmentDetailsMap.put(assignmentId, workAssignmentDto);
			}
			EmployeeWorkAssignmentDTO assignmentDto = assignmentDetailsMap.get(assignmentId);
			Date timeEntryDate = timeCardEntry.getDate();
			assignmentDto.setAssignmentStartAndEndDates(timeEntryDate);
		}
		return assignmentDetailsMap;
	}

	private void createEmployeeIfNotAlreadyCreated(Map<String, EmployeeWorkAssignmentDTO> assignmentDetailsMap,
	        String loggedInUser) {
		System.out.println("inside the createEmployeeIfNotAlreadyCreated!!!");
		for (Map.Entry<String, EmployeeWorkAssignmentDTO> eachAssignment : assignmentDetailsMap.entrySet()) {
			EmployeeWorkAssignmentDTO employeeAssignmentDto = eachAssignment.getValue();
			Employee employee = new Employee();
			employee.setFirstName(employeeAssignmentDto.getEmployeeFirstName());
			employee.setLastName(employeeAssignmentDto.getEmployeeLastName());
			List<Employee> findEmployeeResultsList = hibernateTemplate.findByExample(employee);
			if (findEmployeeResultsList.isEmpty()) {
				Date sysDate = new Date();
				employee.setEmployeeMailId("NOT_ASSIGNED@nisum.com");
				employee.setDesignation("NOT_ASSIGNED");
				employee.setLocation("NOT_ASSIGNED");
				employee.setActive(1);
				employee.setDateOfJoining(sysDate);
				employee.setRelievingDate(sysDate);
				employee.setCreatedUserId(loggedInUser);
				employee.setLastUpdatedUserId(loggedInUser);
				employee.setCreatedDateTime(sysDate);
				employee.setLastUpdatedDateTime(sysDate);
				hibernateTemplate.save(employee);
				hibernateTemplate.refresh(employee);
			} else {
				employee = findEmployeeResultsList.get(0);
			}
			employeeAssignmentDto.setEmployeeId(employee.getEmployeeId());
		}
		System.out.println("End of createEmployeeIfNotAlreadyCreated()!!");
	}

	private void insertOrUpdateEmployeeWorkAssignments(Map<String, EmployeeWorkAssignmentDTO> assignmentDetailsMap,
	        Integer clientId, String loggedInUser) {
		System.out.println("inside the insertOrUpdateEmployeeWorkAssignments");
		for (Map.Entry<String, EmployeeWorkAssignmentDTO> eachAssignment : assignmentDetailsMap.entrySet()) {
			EmployeeWorkAssignmentDTO employeeAssignmentDto = eachAssignment.getValue();
			String assignmentId = employeeAssignmentDto.getAssignmentId();
			SimpleExpression workOrderIdEq = Restrictions.eq("workOrderId", assignmentId);
			DetachedCriteria workOrderIdCriteria = DetachedCriteria.forClass(EmployeeWorkAssignment.class);
			workOrderIdCriteria.add(workOrderIdEq);
			List<EmployeeWorkAssignment> workOrderResultsList = hibernateTemplate.findByCriteria(workOrderIdCriteria);

			Date newAssignmentStartDate = employeeAssignmentDto.getAssignmentStartDate();
			Date newAssignmentEndDate = employeeAssignmentDto.getAssignmentEndDate();
			if (workOrderResultsList.isEmpty()) {
				EmployeeWorkAssignment newWorkAssignment = new EmployeeWorkAssignment();
				newWorkAssignment.setWorkOrderId(assignmentId);
				int employeeId = employeeAssignmentDto.getEmployeeId();
				newWorkAssignment.setEmployeeId(employeeId);
				// TODO: check this and update with correct client employee id or leave it blank
				newWorkAssignment.setClientEmployeeId(String.valueOf(employeeId));
				newWorkAssignment.setClientId(clientId);
				newWorkAssignment.setCreatedUserId(loggedInUser);
				newWorkAssignment.setLastUpdatedUserId(loggedInUser);
				newWorkAssignment.setAssignmentStartDate(newAssignmentStartDate);
				newWorkAssignment.setAssignmentEndDate(newAssignmentEndDate);
				Date sysDate = new Date();
				newWorkAssignment.setCreatedDateTime(sysDate);
				newWorkAssignment.setLastUpdatedDateTime(sysDate);
				hibernateTemplate.save(newWorkAssignment);
			} else {
				boolean assignmentDataUpdated = false;
				EmployeeWorkAssignment existingWorkAssignment = workOrderResultsList.get(0);
				Date originalAssignmentStartDate = existingWorkAssignment.getAssignmentStartDate();
				Date originalAssignmentEndDate = existingWorkAssignment.getAssignmentEndDate();
				if (originalAssignmentEndDate.before(newAssignmentEndDate)) {
					existingWorkAssignment.setAssignmentEndDate(newAssignmentEndDate);
					assignmentDataUpdated = true;
				}
				if (originalAssignmentStartDate.after(newAssignmentStartDate)) {
					existingWorkAssignment.setAssignmentStartDate(newAssignmentStartDate);
					assignmentDataUpdated = true;
				}
				if (assignmentDataUpdated) {
					existingWorkAssignment.setLastUpdatedUserId(loggedInUser);
					existingWorkAssignment.setLastUpdatedDateTime(new Date());
					hibernateTemplate.update(existingWorkAssignment);
				}
			}
		}
		System.out.println("End of the insertOrUpdateEmployeeWorkAssignments!!!");
	}

	private void createStagingTimeEntries(Integer timeImportSeqId, List<TimeCardReportDTO> timecardReportDtoList,
	        Map<String, EmployeeWorkAssignmentDTO> assignmentDetailsMap, String loggedInUser) {
		System.out.println("Inside the createStagingTimeEntries!!!!!");
		for (TimeCardReportDTO timeCardEntry : timecardReportDtoList) {
			String asssignmentId = timeCardEntry.getAsssignmentId();
			EmployeeWorkAssignmentDTO employeeWorkAssignmentDTO = assignmentDetailsMap.get(asssignmentId);
			int employeeId = employeeWorkAssignmentDTO.getEmployeeId();
			EmployeeTimeEntriesStaging stagingEntry = populateEmployeeTimeEntrieStagingBO(timeCardEntry, loggedInUser);
			stagingEntry.setTimeImportSeqId(timeImportSeqId);
			stagingEntry.setEmployeeId(employeeId);
			hibernateTemplate.save(stagingEntry);
		}
		System.out.println("End of the createStagingTimeEntries!!!!!");
	}

	private EmployeeTimeEntriesStaging populateEmployeeTimeEntrieStagingBO(TimeCardReportDTO timeCardEntry,
	        String loggedInUser) {
		Date sysDate = new Date();
		EmployeeTimeEntriesStaging employeeTimeStagingEntry = new EmployeeTimeEntriesStaging();
		employeeTimeStagingEntry.setWorkOrderId(timeCardEntry.getAsssignmentId());
		employeeTimeStagingEntry.setTimeEntryDate(timeCardEntry.getDate());
		employeeTimeStagingEntry.setHoursSubmitted(timeCardEntry.getHoursSubmitted());
		employeeTimeStagingEntry.setApproverId(timeCardEntry.getApproverName());
		employeeTimeStagingEntry.setApproverEmail(timeCardEntry.getApproverEmailId());
		employeeTimeStagingEntry.setTimeEntryProcInd(TimeCardConstants.HOURS_PROC_IND_NO);
		employeeTimeStagingEntry.setCreatedUserId(loggedInUser);
		employeeTimeStagingEntry.setCreatedDateTime(sysDate);
		employeeTimeStagingEntry.setLastUpdatedUserId(loggedInUser);
		employeeTimeStagingEntry.setLastUpdatedDateTime(sysDate);
		return employeeTimeStagingEntry;
	}

	private void logTimeEntriesImportEndTime(TimeEntryImportLog timeEntryImportLog, String loggedInUser) {
		Date sysDate = new Date();
		timeEntryImportLog.setImportCompletionDateTime(sysDate);
		timeEntryImportLog.setLastUpdatedUserId(loggedInUser);
		timeEntryImportLog.setLastUpdatedDateTime(sysDate);
		hibernateTemplate.update(timeEntryImportLog);
	}
}

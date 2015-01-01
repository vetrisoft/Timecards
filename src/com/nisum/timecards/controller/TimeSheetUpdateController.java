package com.nisum.timecards.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;
import com.nisum.timecards.form.WeekTimecardReportUpdateForm;
import com.nisum.timecards.service.ReportsService;

@Controller
public class TimeSheetUpdateController {

	@Autowired
	private ReportsService reportsService;

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public String timeCardReport(ModelMap model, @ModelAttribute("filterForm") FilterForm filterForm,
	        HttpServletRequest request) {
		boolean updateStatus = false;
		if (StringUtils.isEmpty(filterForm.getFilterStatus())) {
			filterForm.setFilterStatus("BN");
		}
		if (null != filterForm.getEmployeeId() && filterForm.getEmployeeId().length > 0) {
			List<TimeCardWeekReportDTO> timeCardList = getRequestData(filterForm, request);
			reportsService.updateStatus(timeCardList);
			updateStatus = true;
		}
		if (filterForm.getEmployeeId() == null) {
			model.addAttribute("errorField", "Please select atleast one record to update");
		}
		if (updateStatus) {
			model.addAttribute("updateStatus", "Records are updated successfully");
		}
		List<TimeCardWeekReportDTO> timeCardWeekReprotData = reportsService.getAllTimeCards(filterForm.getEmpName(),
		        filterForm.getMonthOption(), filterForm.getFilterStatus());
		model.addAttribute("timeCardWeekReprotData", timeCardWeekReprotData);
		model.addAttribute("filterForm", filterForm);
		model.addAttribute("statusMap", getStatusMap());
		return "viewall";

	}

	@RequestMapping(value = "/weekTimecardUpdate", method = RequestMethod.POST)
	public String weekTimecardUpdate(ModelMap model, HttpSession session,
	        @ModelAttribute("timeCardWeekReprotData") WeekTimecardReportUpdateForm weekTimecardReportUpdateForm) {
		try {
			String userName = (String) session.getAttribute("username");
			List<TimeCardDayReportDTO> timeCardDayReportDTOs = weekTimecardReportUpdateForm.getWeekTimeCardReport();
			reportsService.updateDayTimecard(timeCardDayReportDTOs, weekTimecardReportUpdateForm.getWeekEndDate(),
			        userName);

		} catch (Exception e) {
		}
		return "weekReport";
	}

	// private List<TimeCardDayReportDTO> getWeekRequestData(HttpServletRequest request) {
	// String[] timeCardKey = request.getParameterValues("weekTimeCardKey");
	// List<TimeCardDayReportDTO> weekReportDTOs = new ArrayList<TimeCardDayReportDTO>();
	// for (String dayTimecard : timeCardKey) {
	// TimeCardDayReportDTO timeCardDayReportDTO = new TimeCardDayReportDTO();
	// String[] timeCardArray = dayTimecard.split("#");
	// String empid = timeCardArray[0];
	// String timeCardDate = timeCardArray[1];
	// String nonBillHours = request.getParameter("nbHours" + timeCardDate);
	// Double nbHours = null;
	// if (StringUtils.isNotBlank(nonBillHours)) {
	// nbHours = new Double(nonBillHours);
	// }
	// String nonBillReason = request.getParameter("nbReson" + timeCardDate);
	// String comments = request.getParameter("comments" + timeCardDate);
	// timeCardDayReportDTO.setEmployeeId(new Integer(empid));
	// timeCardDayReportDTO.setNonBillableHours(nbHours);
	// timeCardDayReportDTO.setNonBillableReson(nonBillReason);
	// timeCardDayReportDTO.setComments(comments);
	// }
	// return weekReportDTOs;
	// }

	private List<TimeCardWeekReportDTO> getRequestData(FilterForm filterForm, HttpServletRequest request) {
		List<TimeCardWeekReportDTO> timeCardList = new ArrayList<TimeCardWeekReportDTO>();
		String[] empIdList = filterForm.getEmployeeId();
		for (String empId : empIdList) {
			timeCardList.add(createTimeCardDTO(empId, request));
		}
		return timeCardList;
	}

	private TimeCardWeekReportDTO createTimeCardDTO(String empId, HttpServletRequest request) {
		String statusCode = request.getParameter("status#" + empId);
		String comments = request.getParameter("comments#" + empId);
		String[] empIdArray = empId.split("#");
		TimeCardWeekReportDTO timeCardDTO = new TimeCardWeekReportDTO();
		timeCardDTO.setEmployeeId(new Integer(empIdArray[0]));
		timeCardDTO.setWeekEndDate(empIdArray[1]);
		timeCardDTO.setStatusTypeCode(statusCode);
		timeCardDTO.setComments(comments);
		return timeCardDTO;
	}

	public Map<String, String> getStatusMap() {
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("NO", "Normal");
		statusMap.put("AN", "Above Normal");
		statusMap.put("BN", "Below Normal");
		statusMap.put("JS", "Justified");
		return statusMap;
	}

}

package com.nisum.timecards.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nisum.timecards.dto.ClientDTO;
import com.nisum.timecards.dto.HoursStatusDTO;
import com.nisum.timecards.dto.TimeCardDayReportDTO;
import com.nisum.timecards.dto.TimeCardWeekReportDTO;
import com.nisum.timecards.form.ClientForm;
import com.nisum.timecards.form.WeekTimecardReportUpdateForm;
import com.nisum.timecards.service.ReportsService;
import com.nisum.timecards.util.TimeCardConstants;

@Controller
public class ReportsController {

	@Autowired
	private ReportsService reportsService;

//	@RequestMapping(value = "/viewall", method = RequestMethod.GET)
//	public String timeCardReport(ModelMap model, @ModelAttribute("filterForm") FilterForm filterForm) {
//		if (StringUtils.isBlank(filterForm.getFilterStatus())) {
//			filterForm.setFilterStatus(TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL);
//		}
//		if (StringUtils.isBlank(filterForm.getMonthOption())) {
//			filterForm.setMonthOption("3");
//		}
//		List<TimeCardWeekReportDTO> timeCardWeekReprotData = reportsService.getAllTimeCards(filterForm.getEmpName(),
//		        filterForm.getMonthOption(), filterForm.getFilterStatus());
//		model.addAttribute("timeCardWeekReprotData", timeCardWeekReprotData);
//		model.addAttribute("filterForm", filterForm);
//		model.addAttribute("statusMap", getStatusMap());
//		return "viewall";
//
//	}
	
	@RequestMapping(value = "/viewall", method = RequestMethod.GET)
	public ModelAndView timeCardReport(ModelMap model,HttpServletRequest request, FilterForm filterForm) {
		if (StringUtils.isBlank(filterForm.getFilterStatus())) {
			filterForm.setFilterStatus(TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL);
		}
		if (StringUtils.isBlank(filterForm.getMonthOption())) {
			filterForm.setMonthOption("3");
		}
		List<TimeCardWeekReportDTO> timeCardWeekReprotData = reportsService.getAllTimeCards(filterForm.getEmpName(),
		        filterForm.getMonthOption(), filterForm.getFilterStatus());
		model.addAttribute("timeCardWeekReprotData", timeCardWeekReprotData);
		model.addAttribute("filterForm", filterForm);
		model.addAttribute("statusMap", getStatusMap());
		model.addAttribute("statusMap", getStatusMap());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(filterForm);
		modelAndView.setViewName("viewall");
		return modelAndView;
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.GET)
	public ModelAndView timesheetReport(ModelMap model, @ModelAttribute("filterForm") FilterForm filterForm) {
		if (StringUtils.isBlank(filterForm.getFilterStatus())) {
			filterForm.setFilterStatus(TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL);
		}
		if (StringUtils.isBlank(filterForm.getMonthOption())) {
			filterForm.setMonthOption("3");
		}
		List<TimeCardWeekReportDTO> timeCardWeekReprotData = reportsService.getAllTimeCards(filterForm.getEmpName(),
		        filterForm.getMonthOption(), filterForm.getFilterStatus());
		model.addAttribute("timeCardWeekReprotData", timeCardWeekReprotData);
		model.addAttribute("filterForm", filterForm);
		model.addAttribute("statusMap", getStatusMap());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("timesheetReport");
		return modelAndView;
	}
	
	@RequestMapping(value = "/weekReport", method = RequestMethod.GET)
	public String weekTimeCardReport(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String userName = (String)session.getAttribute("username");
		if (StringUtils.isBlank(userName)) {
			return "login";
		}
		String employeeId = request.getParameter("empId");
		String weekEndDate = request.getParameter("weekEndDate");
		List<TimeCardDayReportDTO> timeCardDayReprotData = reportsService.getDayTimeCards(employeeId, weekEndDate);
		WeekTimecardReportUpdateForm weekTimecardReportUpdateForm = new WeekTimecardReportUpdateForm();
		weekTimecardReportUpdateForm.setWeekTimeCardReport(timeCardDayReprotData);
		model.addAttribute("timeCardWeekReprotData", weekTimecardReportUpdateForm);
		model.addAttribute("nonBillableReasons", reportsService.getNonBillableReasons());
		model.addAttribute("weekEndDate", weekEndDate);
		model.addAttribute("employeeName", reportsService.getEmployeName(new Integer(employeeId)));
		return "weekReport";

	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public String exportExcel(ModelMap model, @ModelAttribute("filterForm") FilterForm filterForm,
	        HttpServletResponse response) throws IOException {
		List<Date> weekendDateList = reportsService.getAllTimeCardsWeekEndDates(filterForm.getMonthOption());
		List<TimeCardWeekReportDTO> timeCardWeekReprotData = reportsService.getAllTimeCards(filterForm.getEmpName(),
		        filterForm.getMonthOption(), filterForm.getFilterStatus());
		List<List<Object>> timeCardArray = getTimeCardArray(timeCardWeekReprotData, weekendDateList);
		model.addAttribute("timeCardWeekReprotData", timeCardWeekReprotData);
		OutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=TimeCard.xls");
			WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet sheet = w.createSheet("TimeCard", 0);
			int row = 0;
			for (List<Object> arrayList : timeCardArray) {
				if (row == 0) {
					writeHeaderInfo(row, arrayList, sheet);
				} else {
					writeHoursData(row, arrayList, sheet);
				}
				row++;

			}
			w.write();
			w.close();
		} catch (Exception e) {

		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;

	}

	private void writeHoursData(int row, List<Object> data, WritableSheet sheet) throws RowsExceededException,
	        WriteException {
		int col = 0;
		for (Object object : data) {
			if (object instanceof String) {
				sheet.addCell(getLabelCell(col, row, object.toString()));
			} else {
				HoursStatusDTO hoursStatusDTO = (HoursStatusDTO) object;
				if (null != hoursStatusDTO) {
					sheet.addCell(getHoursCell(col, row, hoursStatusDTO));
				}
			}
			col++;
		}

	}

	private void writeHeaderInfo(int row, List<Object> data, WritableSheet sheet) throws RowsExceededException,
	        WriteException {
		int col = 0;
		for (Object object : data) {
			if (object instanceof Date) {
				Date date = (Date) object;
				sheet.setColumnView(col, 12);
				sheet.addCell(getDateCell(col, row, date));
			} else {
				sheet.setColumnView(col, 35);
				sheet.addCell(getLabelCell(col, row, object.toString()));
			}
			col++;
		}
	}

	private Label getLabelCell(int col, int row, String cellData) throws WriteException {
		WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
		WritableCellFormat format = new WritableCellFormat(arial10font);
		format.setWrap(true);
		Label label = new Label(col, row, cellData, format);
		return label;

	}

	private DateTime getDateCell(int col, int row, Date date) throws WriteException {
		DateFormat customDateFormat = new DateFormat("mm/dd/yyyy");
		WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);
		dateFormat.setShrinkToFit(true);
		DateTime dateCell = new DateTime(col, row, date, dateFormat);
		return dateCell;
	}

	private Number getHoursCell(int col, int row, HoursStatusDTO hours) throws WriteException {
		WritableCellFormat cellFormat = new WritableCellFormat(NumberFormats.FLOAT);
		if (TimeCardConstants.TIME_ENTRY_STATUS_BELOW_NORMAL.equalsIgnoreCase(hours.getStaus())) {
			cellFormat.setBackground(Colour.RED, Pattern.SOLID);
		} else if (TimeCardConstants.TIME_ENTRY_STATUS_JUSTIFIED.equalsIgnoreCase(hours.getStaus())) {
			cellFormat.setBackground(Colour.YELLOW, Pattern.SOLID);
		}
		cellFormat.setShrinkToFit(true);
		Number number = new Number(col, row, hours.getHours(), cellFormat);
		WritableCellFeatures wcf = new WritableCellFeatures();
		if (StringUtils.isNotBlank(hours.getComments())) {
			wcf.setComment(hours.getComments());
		}
		number.setCellFeatures(wcf);

		return number;
	}

	private List<List<Object>> getTimeCardArray(List<TimeCardWeekReportDTO> timeCardWeekReprotData,
	        List<Date> weekendDateList) {

		List<List<Object>> reportTable = new ArrayList<List<Object>>();
		int row = 0;
		List<Object> headerList = new ArrayList<Object>();
		reportTable.add(reportTable.size(), headerList);
		headerList.add(headerList.size(), "Name");
		headerList.addAll(weekendDateList);
		String previousName = "";
		List<Object> dataList = null;
		// StringBuffer remarksData = null;
		Map<Date, HoursStatusDTO> empAllWeeksMap = new HashMap<Date, HoursStatusDTO>();
		for (TimeCardWeekReportDTO timeCard : timeCardWeekReprotData) {
			String name = timeCard.getFullName();
			if (!previousName.equalsIgnoreCase(name)) {
				// Holding adding remarks to excel report.
				/*
				 * if (remarksData != null) { dataList.add(dataList.size(), remarksData.toString()); }
				 */
				dataList = new ArrayList<Object>();
				// remarksData = new StringBuffer("");
				row++;
				reportTable.add(row, dataList);
				dataList.add(dataList.size(), name);
				if (row >= 2) {
					List<Object> perviousDataList = reportTable.get(row - 1);
					// empAllWeeksMap is previous employee data
					for (Date date : weekendDateList) {
						HoursStatusDTO hoursStatusDTO = empAllWeeksMap.get(date);
						if (hoursStatusDTO == null) {
							perviousDataList.add(perviousDataList.size(), "");
						} else {
							perviousDataList.add(perviousDataList.size(), hoursStatusDTO);
						}
					}
				}
				empAllWeeksMap = new HashMap<Date, HoursStatusDTO>();
			}

			/*
			 * if (row == 1) { headerList.add(headerList.size(), timeCard.getTimeEntryWeekEndDate()); }
			 */
			HoursStatusDTO hoursDTO = new HoursStatusDTO();
			hoursDTO.setHours(timeCard.getTotalHoursForWeek());
			hoursDTO.setStaus(timeCard.getHoursStatusTypeCode());
			hoursDTO.setComments(timeCard.getComments());

			// dataList.add(dataList.size(), hoursDTO);
			empAllWeeksMap.put(timeCard.getTimeEntryWeekEndDate(), hoursDTO);
			// Holding adding remarks to excel report.
			/*
			 * if (StringUtils.isNotBlank(timeCard.getComments())) { remarksData.append(timeCard.getComments() + ". ");
			 * }
			 */
			previousName = timeCard.getFullName();

			/*
			 * HashMap weeklyMap = new HashMap(); weeklyMap.put (weekdate, hours)
			 * 
			 * //If name changes then we need construct row. for items in headerList loop if(headList.item exists in
			 * weeklyMap){ addColumnData (weeklyMap.get(headerList.weekDate) }else { addColumnData ("") }
			 */

		}
		// updating last employee details
		List<Object> perviousDataList = reportTable.get(reportTable.size() - 1);
		for (Date date : weekendDateList) {
			HoursStatusDTO hoursStatusDTO = empAllWeeksMap.get(date);
			if (hoursStatusDTO == null) {
				perviousDataList.add(perviousDataList.size(), "");
			} else {
				perviousDataList.add(perviousDataList.size(), hoursStatusDTO);
			}
		}

		// Holding adding remarks to excel report.
		/*
		 * headerList.add(headerList.size(), "REMARKS"); if (remarksData != null) { dataList.add(dataList.size(),
		 * remarksData.toString()); }
		 */
		return reportTable;
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
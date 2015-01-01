package com.nisum.timecards.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nisum.timecards.service.ImportTimeCardService;
import com.nisum.timecards.service.RollupTimeCardService;
import com.nisum.timecards.util.TimeCardConstants;

@Controller
@RequestMapping("/uploadForm")
public class ImportTimeCardController {

	private static final String DATE_FORMAT = "MM/dd/yyyy";
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

	private final ImportTimeCardService importTimeCardService;
	private final RollupTimeCardService rollupTimeCardService;

	@Autowired
	public ImportTimeCardController(ImportTimeCardService importTimeCardService,
	        RollupTimeCardService rollupTimeCardService) {
		this.importTimeCardService = importTimeCardService;
		this.rollupTimeCardService = rollupTimeCardService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm() {
		ModelAndView modelAndView = new ModelAndView();
		TimeSheetFileForm timeSheetFileForm = new TimeSheetFileForm();

		Calendar calendar = Calendar.getInstance();
		Date endDate = getDefaultRollupEndDate(calendar);
		timeSheetFileForm.setEndDate(dateFormatter.format(endDate));

		Date startDate = getDefaultRollupStartDate(calendar);
		timeSheetFileForm.setStartDate(dateFormatter.format(startDate));

		modelAndView.addObject(timeSheetFileForm);
		modelAndView.setViewName("uploadForm");
		return modelAndView;
	}

	private Date getDefaultRollupEndDate(Calendar calendar) {
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// If today is Friday or Saturday, use this week as roll-up period end date
		if (dayOfWeek > 5) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		} else {
			// use last week end as roll-up period end date
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - calendar.get(Calendar.DAY_OF_WEEK));
		}
		return calendar.getTime();
	}

	private Date getDefaultRollupStartDate(Calendar calendar) {
		int dayOfWeek;
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 90);
		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek != Calendar.SUNDAY) {
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - calendar.get(Calendar.DAY_OF_WEEK));
		}
		return calendar.getTime();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView upload(@ModelAttribute("file") TimeSheetFileForm timeSheetFileForm) throws Exception {
		MultipartFile file = timeSheetFileForm.getFile();

		Integer clientId = new Integer(TimeCardConstants.GID_CLIENT_ID);
		Map<String, Object> importTimeCardDetailsMap = importTimeCardService.importTimeCards(file, clientId);

		Date rollupStartDate = (Date) importTimeCardDetailsMap.get(TimeCardConstants.ROLLUP_PERIOD_START);
		Date rollupEndDate = (Date) importTimeCardDetailsMap.get(TimeCardConstants.ROLLUP_PERIOD_END);

		// String startDate = timeSheetFileForm.getStartDate();
		// String endDate = timeSheetFileForm.getEndDate();

		// if roll-up period start and/or end dates are missing from Front End, use 90 days as roll-up period
		// if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
		// Calendar calendar = Calendar.getInstance();
		// rollupEndDate = getDefaultRollupEndDate(calendar);
		// rollupStartDate = getDefaultRollupStartDate(calendar);
		// } else {
		// rollupStartDate = dateFormatter.parse(startDate);
		// rollupEndDate = dateFormatter.parse(endDate);
		// }
		Integer importTimeCardSeqId = (Integer) importTimeCardDetailsMap.get(TimeCardConstants.TIME_IMPORT_SEQ_ID);
		rollupTimeCardService.rollupTimeCards(importTimeCardSeqId, rollupStartDate, rollupEndDate);
		System.out.println("Finished importing time cards from - " + rollupStartDate + " to - " + rollupEndDate);
		Map<String, Object> model = new HashMap<String, Object>();
		String importStartDate = TimeCardConstants.DATE_FORMATTER.format(rollupStartDate);
		String importEndDate = TimeCardConstants.DATE_FORMATTER.format(rollupEndDate);
		model.put("successMessage", "Successfully imported time cards from '" + importStartDate + "' to '"
		        + importEndDate + "' from the file '" + file.getOriginalFilename() + "'");
		ModelAndView modelAndView = new ModelAndView("uploadForm", model);
		return modelAndView;
	}
}

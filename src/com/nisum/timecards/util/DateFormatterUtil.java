package com.nisum.timecards.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateFormatterUtil {

	public Date parseDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {

			Date date = formatter.parse(dateInString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Date parseDate(String dateInString, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			Date date = formatter.parse(dateInString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String formatDate(Date date, String dateFormat) {
		String strDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		strDate = formatter.format(date);
		return strDate;
	}

	public String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
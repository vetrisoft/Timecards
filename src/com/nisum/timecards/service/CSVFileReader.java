package com.nisum.timecards.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nisum.timecards.dto.TimeCardReportDTO;

public class CSVFileReader {

	public List<TimeCardReportDTO> parse(MultipartFile file) {
		
		List<TimeCardReportDTO> dtoList = new ArrayList<TimeCardReportDTO>();
		
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					
		String line = null;
		int i=0;
		try {
			while ((line = bufferedReader.readLine()) != null)
			{
				if(i < 5) {
					i++;
					continue;
				}
				
				System.out.println(line);
				
				populateTimeCardReportDTO(dtoList, line);
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dtoList;
		
	}

	private void populateTimeCardReportDTO(List<TimeCardReportDTO> dtoList, String data) {
		
		String[] split = data.split(",");
		
		
		TimeCardReportDTO dto = new TimeCardReportDTO();
		
		dto.setApproverEmailId(split[7]);
		dto.setApproverName(split[5]+split[6]);
		dto.setAsssignmentId(split[0]);
		dto.setDate(parseDate(split[3]));
		dto.setFirstName(split[2]);
		dto.setHoursSubmitted(Double.valueOf(split[4]));
		dto.setLastName(split[1]);

		dtoList.add(dto);
		
	}

	private Date parseDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	 
		try {
	 
			Date date = formatter.parse(dateInString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

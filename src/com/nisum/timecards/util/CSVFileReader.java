package com.nisum.timecards.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nisum.timecards.dto.TimeCardReportDTO;

@Component
public class CSVFileReader {

	@Autowired
	private DateFormatterUtil dateUtil;

	public List<TimeCardReportDTO> getData(MultipartFile file) {

		List<TimeCardReportDTO> dtoList = new ArrayList<TimeCardReportDTO>();
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		int i = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (i < 5) {
					i++;
					continue;
				}
				populateTimeCardReportDTO(dtoList, line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtoList;
	}

	private void populateTimeCardReportDTO(List<TimeCardReportDTO> dtoList, String data) {
		String[] split = data.split(",");
		TimeCardReportDTO dto = new TimeCardReportDTO();
		dto.setApproverEmailId(split[7]);
		dto.setApproverName(split[5] + split[6]);
		dto.setAsssignmentId(split[0]);
		dto.setDate(dateUtil.parseDate(split[3]));
		dto.setFirstName(split[2]);
		dto.setHoursSubmitted(Double.valueOf(split[4]));
		dto.setLastName(split[1]);
		dtoList.add(dto);
	}

}

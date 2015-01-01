package com.nisum.timecards.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nisum.timecards.dto.TimeCardReportDTO;

@Service
public class POIFileReader {

	public List<TimeCardReportDTO> getData(MultipartFile file) throws Exception {

		List<TimeCardReportDTO> timeCardDtoList = new ArrayList<TimeCardReportDTO>();

		Iterator<Row> rowIterator = getTimeCardFileContents(file);
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() < 3 || isRowEmpty(row)) {
				// skip the header row or any blank rows in between
				continue;
			}

			Cell workOrderIdCell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			Cell employeeNameCell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
			Cell timeEntryDateCell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
			Cell hoursSubmittedCell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
			Cell approverEmailIdCell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);

			TimeCardReportDTO timeCardDto = new TimeCardReportDTO();
			setWorkOrderId(timeCardDto, workOrderIdCell);
			setEmployeeFirstAndLastNames(timeCardDto, employeeNameCell);
			setTimeEntryDate(timeCardDto, timeEntryDateCell);
			setHoursSubmittedForTheDay(timeCardDto, hoursSubmittedCell);
			setApproverEmail(timeCardDto, approverEmailIdCell);
			timeCardDtoList.add(timeCardDto);
		}

		return timeCardDtoList;
	}

	private Iterator<Row> getTimeCardFileContents(MultipartFile file) {
		try {
			String fileExtension = getFileExtension(file);
			Workbook workbook;
			if (fileExtension.equalsIgnoreCase(".xls")) {
				workbook = new HSSFWorkbook(file.getInputStream());
			} else if (fileExtension.equalsIgnoreCase(".xlsx")) {
				workbook = new XSSFWorkbook(file.getInputStream());
			} else {
				throw new RuntimeException("Unsupported file format, only .xls and .xlsx allowed, but found - "
				        + fileExtension);
			}

			Sheet sheet = workbook.getSheetAt(0);
			return sheet.iterator();
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while reading file - " + e.getMessage(), e);
		}
	}

	private String getFileExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex == -1) {
			throw new RuntimeException("Invalid File, could not determine file type - " + fileName);
		} else if (lastIndex == 0) {
			throw new RuntimeException("Invalid File, it appears file has no name, but just file type - " + fileName);
		}
		String fileExtension = fileName.substring(lastIndex);
		return fileExtension;
	}

	private boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				return false;
			}
		}
		return true;
	}

	private void setWorkOrderId(TimeCardReportDTO timeCardDto, Cell workOrderIdCell) {
		String workOrderId = null;
		if (workOrderIdCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			int numericCellValue = (int) workOrderIdCell.getNumericCellValue();
			workOrderId = String.valueOf(numericCellValue);
		} else if (workOrderIdCell.getCellType() == Cell.CELL_TYPE_STRING) {
			workOrderId = workOrderIdCell.getStringCellValue();
		}

		if (workOrderId == null || workOrderId.isEmpty()) {
			throw new RuntimeException("Invalid Content found for Work Order ID column");
		}
		timeCardDto.setAsssignmentId(workOrderId);
	}

	private void setEmployeeFirstAndLastNames(TimeCardReportDTO timeCardDto, Cell employeeNameCell) {
		String employeeName = employeeNameCell.getStringCellValue();
		if (employeeName.isEmpty() || employeeName.indexOf(",") == -1) {
			throw new RuntimeException("Employee name is supposed to be not empty AND in last_name, first_name format");
		}
		String[] employeeNameArray = employeeName.split(",");
		timeCardDto.setLastName(employeeNameArray[0].trim());
		timeCardDto.setFirstName(employeeNameArray[1].trim());
	}

	private void setTimeEntryDate(TimeCardReportDTO timeCardDto, Cell timeEntryDateCell) {
		Date timeEntryDate = timeEntryDateCell.getDateCellValue();
		timeCardDto.setDate(timeEntryDate);
	}

	private void setHoursSubmittedForTheDay(TimeCardReportDTO timeCardDto, Cell hoursSubmittedCell) {
		double hoursSubmitted = hoursSubmittedCell.getNumericCellValue();
		timeCardDto.setHoursSubmitted(hoursSubmitted);
	}

	private void setApproverEmail(TimeCardReportDTO timeCardDto, Cell approverEmailIdCell) {
		String approverEmailId = approverEmailIdCell.getStringCellValue();
		timeCardDto.setApproverEmailId(approverEmailId);
	}

	public Date getRollupPeriodStartDate(MultipartFile file) throws Exception {
		String[] importPeriodCellValues = getImportPeriodCellValue(file);
		String importStartDate = importPeriodCellValues[0];
		return TimeCardConstants.DATE_FORMATTER.parse(importStartDate);
	}

	private String[] getImportPeriodCellValue(MultipartFile file) {
		Iterator<Row> rowIterator = getTimeCardFileContents(file);
		Row firstRow = rowIterator.next();
		Cell importPeriodCell = firstRow.getCell(2, Row.CREATE_NULL_AS_BLANK);
		String importPeriodCellValue = importPeriodCell.getStringCellValue();
		String[] importPeriodCellValues = importPeriodCellValue.split(" ");
		if (importPeriodCellValues.length != 3) {
			throw new RuntimeException("File should have import start and end dates in cell 'C1'");
		}
		return importPeriodCellValues;
	}

	public Date getRollupPeriodEndDate(MultipartFile file) throws Exception {
		String[] importPeriodCellValues = getImportPeriodCellValue(file);
		String importEndDate = importPeriodCellValues[2];
		return TimeCardConstants.DATE_FORMATTER.parse(importEndDate);
	}
}

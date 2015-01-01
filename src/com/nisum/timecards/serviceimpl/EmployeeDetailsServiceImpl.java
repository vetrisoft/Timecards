/**
 * 
 */
package com.nisum.timecards.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nisum.timecards.dao.EmployeeDAO;
import com.nisum.timecards.dto.EmployeeDTO;
import com.nisum.timecards.service.EmployeeDetailsService;
import com.nisum.timecards.service.TestService;

/**
 * @author Administrator
 *
 */
@Service
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

	private static final Logger logger = Logger.getLogger(EmployeeDetailsServiceImpl.class);

	@Autowired
	private EmployeeDAO employeeDAO;

	/**
	 * To update the new employee details/modify the existing employee details
	 * in employee master table.
	 */
	@Override
	public boolean upateEmployeeDetails(EmployeeDTO employeeDTO) {
		logger.debug("Inside the " + EmployeeDetailsServiceImpl.class
				+ " upateEmployeeDetails()!!!");
		// TODO Auto-generated method stub
		boolean status = false;
		try {
			status = employeeDAO.upateEmployeeDetails(employeeDTO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return status;
	}

	public EmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	/***
	 * Used to load all the Active Employee details from Employee Masters.
	 */
	public List<EmployeeDTO> loadAllEmployeeDetails() {
		logger.debug("Inside the " + EmployeeDetailsServiceImpl.class + " loadAllEmployeeDetails()!!!");
		// TODO Auto-generated method stub
		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		try {
			employeeDTOList = employeeDAO.loadAllEmployeeDetails();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			employeeDTOList = null;
		}
		return employeeDTOList;

	}

	/**
	 * To search the employee detail from employee master table.
	 */
	@Override
	public List<EmployeeDTO> findEmployeeDetail(String employeeId) {
		logger.debug("Inside the " + EmployeeDetailsServiceImpl.class + " loadAllEmployeeDetails()!!!");
		// TODO Auto-generated method stub
		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		try {
			employeeDTOList = employeeDAO.findEmployeeDetail(employeeId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			employeeDTOList = null;
		}
		return employeeDTOList;

	}

}
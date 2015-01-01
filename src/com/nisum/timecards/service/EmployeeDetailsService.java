/**
 * 
 */
package com.nisum.timecards.service;

import java.util.List;

import com.nisum.timecards.dto.EmployeeDTO;

/**
 * @author Administrator
 *
 */
public interface EmployeeDetailsService {

	public boolean upateEmployeeDetails(EmployeeDTO employeeDTO);

	public List<EmployeeDTO> findEmployeeDetail(String employeeId);

	public List<EmployeeDTO> loadAllEmployeeDetails();

}

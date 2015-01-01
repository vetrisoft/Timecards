/**
 * 
 */
package com.nisum.timecards.dao;

import java.util.List;

import com.nisum.timecards.dto.EmployeeDTO;
import com.nisum.timecards.exception.DAOException;

/**
 * @author Administrator
 *
 */
public interface EmployeeDAO {
	
	public boolean upateEmployeeDetails(EmployeeDTO employeeDTO) throws DAOException;
	public List<EmployeeDTO> findEmployeeDetail(String employeeId) throws DAOException;
	public List<EmployeeDTO> loadAllEmployeeDetails() throws DAOException;
}

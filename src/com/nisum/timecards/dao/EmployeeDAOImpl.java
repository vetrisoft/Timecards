/**
 * 
 */
package com.nisum.timecards.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.nisum.timecards.bo.Employee;
import com.nisum.timecards.dto.EmployeeDTO;
import com.nisum.timecards.exception.DAOException;
import com.nisum.timecards.util.DateFormatterUtil;
import com.nisum.timecards.util.TimeCardConstants;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private DateFormatterUtil dateUtil;

	private static final Logger logger = Logger.getLogger(EmployeeDAOImpl.class);

	/**
	 * Used to update the employee details into the Employee master table.
	 * @throws DAOException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean upateEmployeeDetails(EmployeeDTO employeeDTO) throws DAOException {
		// TODO Auto-generated method stub
		logger.debug("Inside the " + EmployeeDAOImpl.class
				+ "updateEmployeeDetails()!!");
		System.out.println("employeeDTO.getEmployeeNumber():::"+employeeDTO.getEmployeeNumber());
		boolean status = false;
		try {
			Employee employee = new Employee();
			// Currently Auto generated when insert the new Employee into the Master Table.
			if(employeeDTO.getEmployeeNumber() !=null && !employeeDTO.getEmployeeNumber().equals(""))
				employee.setEmployeeId(Integer.parseInt(employeeDTO.getEmployeeNumber()));
			
			employee.setFirstName(employeeDTO.getEmployeeFirstName().toUpperCase().trim());
			employee.setLastName(employeeDTO.getEmployeeLastName().toUpperCase().trim());
			employee.setEmployeeMailId(employeeDTO.getEmployeeMailId().trim());
			employee.setLocation(employeeDTO.getBaseLocation().trim());
			employee.setDesignation(employeeDTO.getDesignation().trim());
			employee.setActive(TimeCardConstants.ACTIVE);// by default, the new
															// Employee status
															// is active only.
			employee.setDateOfJoining(dateUtil.parseDate(employeeDTO.getDateOfJoining().trim(), "yyyy-MM-dd"));
			employee.setRelievingDate(dateUtil.parseDate(employeeDTO.getRelievingDate().trim(), "yyyy-MM-dd"));
			Calendar cal = Calendar.getInstance();
			employee.setCreatedUserId("Admin");
			employee.setCreatedDateTime(dateUtil.parseDate(
					dateUtil.getCurrentDate(), "yyyy-MM-dd"));
			employee.setLastUpdatedUserId("Admin");
			employee.setLastUpdatedDateTime(cal.getTime());
			hibernateTemplate.saveOrUpdate(employee);
			status = true;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return status;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public DateFormatterUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateFormatterUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	@Override
	public List<EmployeeDTO> loadAllEmployeeDetails() throws DAOException {
		// TODO Auto-generated method stub
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String orderByClause = " ORDER BY EMP.FIRST_NAME, EMP.LAST_NAME ";
		String selectQuery = "SELECT EMP.EMPLOYEE_ID, EMP.FIRST_NAME,EMP.LAST_NAME, "
				+ "EMP.EMPLOYEE_MAIL_ID,EMP.DATE_OF_JOINING,EMP.RELIEVING_DATE, EMP.DESIGNATION, EMP.LOCATION "
				+ "FROM EMPLOYEE_MSTR_T EMP WHERE EMP.ACTIVE = 1 "
				+ orderByClause;

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		try {
			Query query = session.createSQLQuery(selectQuery);
			List list = query.list();

			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				EmployeeDTO employeeDTO = new EmployeeDTO();
				Object[] object = (Object[]) iterator.next();
				if (null != object[0]) {
					employeeDTO.setEmployeeNumber(object[0].toString());
				}
				if (null != object[1]) {
					employeeDTO.setEmployeeFirstName(object[1].toString());
				}
				if (null != object[2]) {
					employeeDTO.setEmployeeLastName(object[2].toString());
				}
				if (null != object[3]) {
					employeeDTO.setEmployeeMailId(object[3].toString());
				}
				if (null != object[4]) {
					employeeDTO.setDateOfJoining(object[4].toString());
				}
				if (null != object[5]) {
					employeeDTO.setRelievingDate(object[5].toString());
				}
				if (null != object[6]) {
					employeeDTO.setDesignation(object[6].toString());
				}
			
				if (null != object[7]) {
					employeeDTO.setBaseLocation(object[7].toString());
				}

				employeeDTOList.add(employeeDTO);
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		session.close();
		return employeeDTOList;
	}

	@Override
	public List<EmployeeDTO> findEmployeeDetail(String employeeId) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Employee.class).add(Restrictions.eq("employeeId", Integer.parseInt(employeeId)));

		List<Employee> employeeList = new ArrayList<Employee>();
		employeeList = criteria.list();
		Iterator it = employeeList.iterator();

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		while (it.hasNext()) {
			Employee employee = (Employee) it.next();
			EmployeeDTO employeeDTO = new EmployeeDTO();

			employeeDTO.setEmployeeNumber(employee.getEmployeeId().toString());
			employeeDTO.setEmployeeFirstName(employee.getFirstName());
			employeeDTO.setEmployeeLastName(employee.getLastName());
			employeeDTO.setDateOfJoining(employee.getDateOfJoining().toString());
			employeeDTO.setRelievingDate(employee.getRelievingDate().toString());
			employeeDTO.setBaseLocation(employee.getLocation());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTOList.add(employeeDTO);
		}
		session.close();
		// TODO Auto-generated method stub
		System.out.print("employeeDTOList DAO::" + employeeDTOList.size());
		return employeeDTOList;
	}
}
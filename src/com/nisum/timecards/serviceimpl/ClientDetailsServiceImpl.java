/**
 * 
 */
package com.nisum.timecards.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nisum.timecards.dao.ClientDAO;
import com.nisum.timecards.dto.ClientDTO;
import com.nisum.timecards.dto.EmployeeDTO;
import com.nisum.timecards.service.ClientService;

/**
 * @author Administrator
 *
 */
@Service
public class ClientDetailsServiceImpl implements ClientService {

	private static final Logger logger = Logger.getLogger(ClientDetailsServiceImpl.class);

	@Autowired
	private ClientDAO clientDAO;

	@Override
	public boolean updateClientDetails(ClientDTO clientDTO) {
		logger.debug("Inside the " + ClientDetailsServiceImpl.class + " updateClientDetails()!!!");
		// TODO Auto-generated method stub
		boolean status = false;
		try {
			status = clientDAO.updateClientDetails(clientDTO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return status;

	}

	@Override
	public List<ClientDTO> loadAllClientDetails() {
		logger.debug("Inside the " + ClientDetailsServiceImpl.class + " loadAllClientDetails()!!!");
		// TODO Auto-generated method stub
		List<ClientDTO> clientDTOList = new ArrayList<ClientDTO>();
		try {
			clientDTOList = clientDAO.loadAllClientDetails();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			clientDTOList = null;
		}
		return clientDTOList;
	}

	@Override
	public boolean updateClientHolidays(ClientDTO clientDTO) {
		// TODO Auto-generated method stub
		logger.debug("Inside the " + ClientDetailsServiceImpl.class + " updateClientHolidays()!!!");
		boolean status = false;
		try {
			status = clientDAO.updateClientHolidays(clientDTO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return status;
	}

}
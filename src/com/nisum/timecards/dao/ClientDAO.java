/**
 * 
 */
package com.nisum.timecards.dao;

import java.util.List;

import com.nisum.timecards.dto.ClientDTO;
import com.nisum.timecards.exception.DAOException;

/**
 * @author Administrator
 *
 */
public interface ClientDAO {

	public boolean updateClientDetails(ClientDTO clientDTO) throws DAOException;

	public List<ClientDTO> findClientDetails(String clientId)
			throws DAOException;

	public List<ClientDTO> loadAllClientDetails() throws DAOException;
	
	
	public boolean updateClientHolidays(ClientDTO clientDTO) throws DAOException;

}
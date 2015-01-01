/**
 * 
 */
package com.nisum.timecards.service;

import java.util.List;

import com.nisum.timecards.dto.ClientDTO;

/**
 * @author Administrator
 *
 */
public interface ClientService {

	public boolean updateClientDetails(ClientDTO clientDTO);
	
	public boolean updateClientHolidays(ClientDTO clientDTO);

	public List<ClientDTO> loadAllClientDetails();

}
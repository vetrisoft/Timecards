/**
 * 
 */
package com.nisum.timecards.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.nisum.timecards.bo.Client;
import com.nisum.timecards.bo.ClientHolidays;
import com.nisum.timecards.dto.ClientDTO;
import com.nisum.timecards.exception.DAOException;
import com.nisum.timecards.util.DateFormatterUtil;

/**
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
@Repository
public class ClientDAOImpl implements ClientDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private DateFormatterUtil dateUtil;

	private static final Logger logger = Logger.getLogger(ClientDAOImpl.class);

	@Override
	@SuppressWarnings("rawtypes")
	public boolean updateClientDetails(ClientDTO clientDTO) throws DAOException {
		logger.debug("Inside the " + ClientDAOImpl.class + "updateClientDetails()!!");
		System.out.println("updateClientDetails():::" + clientDTO.getClientName());
		boolean status = false;
		try {
			Client client = new Client();
			// Currently Auto generated when insert the new Client into the
			// Master Table.
			if (clientDTO.getClientId() != null && !clientDTO.getClientId().equals(""))
				client.setClientId(Integer.parseInt(clientDTO.getClientId()));

			client.setClientName(clientDTO.getClientName());
			client.setClientManager(clientDTO.getClientManager());
			client.setClientLocation(clientDTO.getClientLocation());
			client.setPhoneNumber(clientDTO.getPhoneNumber());
			client.setClientMailId(clientDTO.getClientMailId());

			client.setCreatedDateTime(dateUtil.parseDate(dateUtil.getCurrentDate(), "yyyy-MM-dd"));
			client.setCreatedUserId("TC_ADMIN");
			client.setLastUpdatedDateTime(dateUtil.parseDate(dateUtil.getCurrentDate(), "yyyy-MM-dd"));
			client.setLastUpdatedUserId("TC_ADMIN");

			hibernateTemplate.saveOrUpdate(client);
			status = true;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return status;
	}

	@Override
	public List<ClientDTO> findClientDetails(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientDTO> loadAllClientDetails() throws DAOException {

		List<ClientDTO> clientDTOList = new ArrayList<ClientDTO>();
		try {
			Session session = hibernateTemplate.getSessionFactory()
					.openSession();
			Criteria criteria = session.createCriteria(Client.class);

			List<Client> clientList = new ArrayList<Client>();
			clientList = criteria.list();
			Iterator it = clientList.iterator();

			while (it.hasNext()) {
				Client client = (Client) it.next();
				ClientDTO clientDTO = new ClientDTO();
				clientDTO.setClientId(client.getClientId().toString());
				clientDTO.setClientName(client.getClientName());
				clientDTO.setClientManager(client.getClientManager());
				clientDTO.setClientLocation(client.getClientLocation());
				clientDTO.setClientMailId(client.getClientMailId());
				clientDTO.setPhoneNumber(client.getPhoneNumber());
				clientDTOList.add(clientDTO);
			}
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new DAOException(e.getMessage());
		}
		return clientDTOList;
	}

	@Override
	public boolean updateClientHolidays(ClientDTO clientDTO)
			throws DAOException {
		logger.debug("Inside the " + ClientDAOImpl.class + "updateClientHolidays()!!");
		System.out.println("ClientDAOImpl ------>updateClientHolidays():::"	+ clientDTO.getClientId());
		boolean status = false;
		try {
			Iterator iterator = clientDTO.getClientHoildays().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry pairs = (Map.Entry)iterator.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				ClientHolidays clientHolidays = new ClientHolidays();
				// Currently Auto generated when insert the new Client into the Master Table.
				if (clientDTO.getClientId() != null && !clientDTO.getClientId().equals(""))
					clientHolidays.setClientId(Integer.parseInt(clientDTO.getClientId()));

				clientHolidays.setHolidayOn(dateUtil.parseDate(pairs.getKey().toString(), "yyyy-MM-dd"));
				clientHolidays.setHolidayOccasion(pairs.getValue().toString());
				clientHolidays.setCreatedDateTime(dateUtil.parseDate(dateUtil.getCurrentDate(), "yyyy-MM-dd"));
				clientHolidays.setCreatedUserId("TC_ADMIN");
				clientHolidays.setLastUpdatedDateTime(dateUtil.parseDate(dateUtil.getCurrentDate(), "yyyy-MM-dd"));
				clientHolidays.setLastUpdatedUserId("TC_ADMIN");

				hibernateTemplate.saveOrUpdate(clientHolidays);
			}
			status = true;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new DAOException(e.getMessage());
		}
		return status;

	}
}

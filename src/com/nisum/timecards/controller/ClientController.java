/**
 * 
 */
package com.nisum.timecards.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nisum.timecards.dto.ClientDTO;
import com.nisum.timecards.form.ClientForm;
import com.nisum.timecards.service.ClientService;

/**
 * @author Administrator
 * 
 */
@Controller
public class ClientController {

	private final ClientService clientService;
	private static final Logger logger = Logger.getLogger(ClientController.class);

	@Autowired
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@RequestMapping(value = "/clientForm", method = RequestMethod.GET)
	public ModelAndView showClientForm() {
		logger.debug("Inside the" + ClientController.class + " -- showClientForm()!!");
		ModelAndView modelAndView = new ModelAndView();
		ClientForm clientForm = new ClientForm();
		// invoke the service call to load the updated employee list.....
		List<ClientDTO> clientList = clientService.loadAllClientDetails();

		modelAndView.addObject("clientList", clientList);
		modelAndView.addObject(clientForm);
		modelAndView.setViewName("clientForm");
		return modelAndView;
	}

	@RequestMapping(value = "/updateClient", method = RequestMethod.POST)
	public ModelAndView updateClientDetails(ModelMap model,
			HttpSession session, ClientForm clientForm) {
		logger.debug("Inside the " + ClientController.class	+ " -- updateClientDetails()!!");
		ModelAndView modelAndView = new ModelAndView();
		try {
			// invoke the service call.....
			ClientDTO clientDTO = getClientRequestData(clientForm);
			clientService.updateClientDetails(clientDTO);
			modelAndView.addObject("message", clientForm.getClientName() + " " + " has been saved successfully.");

			// invoke the service call to load the updated employee list.....
			List<ClientDTO> clientList = clientService.loadAllClientDetails();

			modelAndView.addObject("clientList", clientList);
			modelAndView.addObject(clientForm);
			modelAndView.setViewName("clientForm");
		} catch (Exception exception) {
			exception.printStackTrace();
			modelAndView.addObject("errorblock","Error, While adding an new Client Details. Please contact system Administrotor.");
		}
		return modelAndView;
	}

	/**
	 * 
	 * @param clientForm
	 */
	private ClientDTO getClientRequestData(ClientForm clientForm) {
		ClientDTO clientDTO = new ClientDTO();
		if (clientForm.getClientId() != null && !clientForm.getClientId().equals(""))
			clientDTO.setClientId(clientForm.getClientId());
		clientDTO.setClientName(clientForm.getClientName());
		clientDTO.setClientLocation(clientForm.getClientLocation());
		clientDTO.setClientManager(clientForm.getClientManager());
		clientDTO.setClientMailId(clientForm.getClientMailId());
		clientDTO.setPhoneNumber(clientForm.getPhoneNumber());
		return clientDTO;
	}

	@RequestMapping(value = "/declareClientHoliday", method = RequestMethod.GET)
	public ModelAndView declareClientHoliday() {
		logger.debug("Inside the" + ClientController.class + " -- showClientHoliday()!!");
		ModelAndView modelAndView = new ModelAndView();
		ClientForm clientForm = new ClientForm();

		// invoke the service call to load the updated employee list.....
		List<ClientDTO> clientList = clientService.loadAllClientDetails();

		modelAndView.addObject("clientList", clientList);
		modelAndView.addObject(clientForm);
		modelAndView.setViewName("declareClientHoliday");
		return modelAndView;
	}

	/**
	 * It is used to update the list of holidays for the particular client.
	 * 
	 * @param model
	 * @param session
	 * @param clientForm
	 * @return
	 */
	@RequestMapping(value = "/updateClientHolidays", method = RequestMethod.POST)
	public ModelAndView updateClientHolidays(ModelMap model,HttpServletRequest request, ClientForm clientForm) {
		logger.debug("Inside the" + ClientController.class	+ " -- updateClientHolidays()!!");
		ModelAndView modelAndView = new ModelAndView();
		try {
			String clientId = clientForm.getClientId();
			ClientDTO clientDTO = getClientLeaveDetails(clientForm);
			boolean status = clientService.updateClientHolidays(clientDTO);
			if (status)
				modelAndView.addObject("message","Holiday List has been added successfully for the client : "+ clientId);
			else
				modelAndView.addObject("errorblock","Error, While adding Holiday Details for this client. Please contact system Administrotor.");

			// invoke the service call to load the updated employee list.....
			modelAndView.setViewName("declareClientHoliday");
		} catch (Exception exception) {
			exception.printStackTrace();
			modelAndView.addObject("errorblock","Error, While adding Holiday Details for this client. Please contact system Administrotor.");
		}
		return modelAndView;
	}

	/**
	 * To transfer the client side data from clientform to ClientDTO object.
	 * 
	 * @param clientForm
	 * @return
	 */
	private ClientDTO getClientLeaveDetails(ClientForm clientForm) {
		ClientDTO clientDTO = new ClientDTO();
		if (clientForm.getClientId() != null && !clientForm.getClientId().equals(""))
			clientDTO.setClientId(clientForm.getClientId());

		List<String> dateValueList = clientForm.getDateval();
		List<String> reasonValueList = clientForm.getReasonval();

		Map<String, String> holidayList = new HashMap<String, String>();
		if(dateValueList !=null && dateValueList.size() > 0){
			for(int index = 1 ; index < dateValueList.size(); index ++){
				if(dateValueList.get(index).toString() != null && !dateValueList.get(index).toString().isEmpty()
						&&  reasonValueList.get(index).toString() != null && ! reasonValueList.get(index).toString().isEmpty()){
					holidayList.put(dateValueList.get(index).toString(), reasonValueList.get(index).toString());
				}
			}
		}
		clientDTO.setClientHoildays(holidayList);
		return clientDTO;
	}

}
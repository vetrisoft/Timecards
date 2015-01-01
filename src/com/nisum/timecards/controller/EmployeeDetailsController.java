package com.nisum.timecards.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nisum.timecards.dto.EmployeeDTO;
import com.nisum.timecards.form.EmployeeDetailsForm;
import com.nisum.timecards.service.EmployeeDetailsService;
import com.nisum.timecards.service.RollupTimeCardService;

@Controller
public class EmployeeDetailsController {
	
	private final EmployeeDetailsService employeeDetailsService;
	private final RollupTimeCardService rollupTimeCardService;
	
	private static final Logger logger = Logger.getLogger(EmployeeDetailsController.class);

	@Autowired
	public EmployeeDetailsController(EmployeeDetailsService employeeDetailsService,
	        RollupTimeCardService rollupTimeCardService) {
		this.employeeDetailsService = employeeDetailsService;
		this.rollupTimeCardService = rollupTimeCardService;
	}

	@RequestMapping(value="/employeeForm", method = RequestMethod.GET)
	public ModelAndView showForm() {
		logger.debug("Inside the" + EmployeeDetailsController.class + " -- showForm()!!" );
		ModelAndView modelAndView = new ModelAndView();
		EmployeeDetailsForm employeeDetailsForm = new EmployeeDetailsForm();

		//invoke the service call.....
		List<EmployeeDTO> employeeDTOList = employeeDetailsService.loadAllEmployeeDetails();
		modelAndView.addObject("employeeList", employeeDTOList);
		modelAndView.addObject(employeeDetailsForm);
		modelAndView.setViewName("employeeForm");

		return modelAndView;
	}
	
//	@RequestMapping(value="/editEmployee", method = RequestMethod.GET)
//	//@RequestParam  String employeeNumber
//	//public @ResponseBody EmployeeDetailsForm editEmployee(@PathVariable  String employeeNumber) {
//public @ResponseBody EmployeeDetailsForm editEmployee(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		//response.setContentType("application/json");
//		String employeeNumber = request.getParameter("employeeNumber");
//		System.out.println("inside the editEmployee!!!!"+employeeNumber);
//		logger.debug("Inside the" + EmployeeDetailsController.class + " -- editEmployee()!!" );
//		//ModelAndView modelAndView = new ModelAndView();
//		EmployeeDetailsForm employeeDetailsForm = new EmployeeDetailsForm();
//
//		try {
//			//invoke the service call.....
//			List<EmployeeDTO> employeeDTOList = employeeDetailsService.findEmployeeDetail(employeeNumber);
//			if(employeeDTOList !=null && employeeDTOList.size() >0){
//				EmployeeDTO employeeDTO = employeeDTOList.get(0);
//				employeeDetailsForm.setEmployeeNumber(employeeDTO.getEmployeeNumber());
//				employeeDetailsForm.setFirstName(employeeDTO.getEmployeeFirstName());
//				employeeDetailsForm.setLastName(employeeDTO.getEmployeeLastName());
//				employeeDetailsForm.setBaseLocation(employeeDTO.getBaseLocation());
//				employeeDetailsForm.setDesignation(employeeDTO.getDesignation());
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return employeeDetailsForm;
//	}
	
	@RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
	public ModelAndView updateEmployee(ModelMap model, HttpSession session,
	        @ModelAttribute("timeCardWeekReprotData") EmployeeDetailsForm employeeDetailsForm) {
		logger.debug("Inside the " + EmployeeDetailsController.class + " -- updateEmployee()!!" );
		System.out.println("Inside the " + EmployeeDetailsController.class + " -- updateEmployee()!!" );
		ModelAndView modelAndView = new ModelAndView();
		try{
			//invoke the service call.....
			EmployeeDTO employeeDTO = getEmployeeRequestData(employeeDetailsForm);
			employeeDetailsService.upateEmployeeDetails(employeeDTO);
			modelAndView.addObject("message", employeeDetailsForm.getFirstName() +" "+ employeeDetailsForm.getLastName() + " has been saved successfully.");
			
			//invoke the service call to load the updated employee list.....
			List<EmployeeDTO> employeeDTOList = employeeDetailsService.loadAllEmployeeDetails();
			modelAndView.addObject("employeeList", employeeDTOList);
			modelAndView.addObject(employeeDetailsForm);
			modelAndView.setViewName("employeeForm");

		}catch(Exception exception){
			exception.printStackTrace();
			modelAndView.addObject("errorblock", "Error, While adding an new Employee Details. Please contact system Administrotor.");
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/loadAllEmployeeDetails", method = RequestMethod.POST)
	public ModelAndView loadAllEmployeeDetails(ModelMap model, HttpSession session,
	        @ModelAttribute("timeCardWeekReprotData") EmployeeDetailsForm employeeDetailsForm) {
		logger.debug("Inside the " + EmployeeDetailsController.class + " -- updateEmployee()!!" );
		ModelAndView modelAndView = new ModelAndView();
		try{
			//invoke the service call.....
			List<EmployeeDTO> employeeDTOList = employeeDetailsService.loadAllEmployeeDetails();
			model.addAttribute("employeeDTOList", employeeDTOList);
			modelAndView.addObject(employeeDetailsForm);
			modelAndView.setViewName("employeeForm");
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return modelAndView;
	}

	
	/**
	 * Assign the values from employeeDetailsForm to EmployeeDTO.
	 * @param employeeDetailsForm
	 * @return
	 */
	private EmployeeDTO getEmployeeRequestData(EmployeeDetailsForm employeeForm) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if(employeeForm.getEmployeeNumber() !=null && !employeeForm.getEmployeeNumber().equals(""))
			employeeDTO.setEmployeeNumber(employeeForm.getEmployeeNumber());
		
		employeeDTO.setEmployeeFirstName(employeeForm.getFirstName());
		employeeDTO.setEmployeeLastName(employeeForm.getLastName());
		employeeDTO.setEmployeeMailId(employeeForm.getEmployeeMailId());
		employeeDTO.setDateOfJoining(employeeForm.getDateOfJoining());
		employeeDTO.setRelievingDate(employeeForm.getRelievingDate());
		employeeDTO.setDesignation(employeeForm.getDesignation());
		employeeDTO.setBaseLocation(employeeForm.getBaseLocation());
		return employeeDTO;
	}
}
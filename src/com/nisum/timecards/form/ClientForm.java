/**
 * 
 */
package com.nisum.timecards.form;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class ClientForm implements Serializable {
	
	private String clientId;
	private String clientName;
	private String clientLocation;
	private String clientManager;
	private String clientMailId;
	private String phoneNumber;
	private List<String> dateval;
	private List<String> reasonval;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientLocation() {
		return clientLocation;
	}
	public void setClientLocation(String clientLocation) {
		this.clientLocation = clientLocation;
	}
	public String getClientManager() {
		return clientManager;
	}
	public void setClientManager(String clientManager) {
		this.clientManager = clientManager;
	}
	public String getClientMailId() {
		return clientMailId;
	}
	public void setClientMailId(String clientMailId) {
		this.clientMailId = clientMailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<String> getDateval() {
		return dateval;
	}
	public void setDateval(List<String> dateval) {
		this.dateval = dateval;
	}
	public List<String> getReasonval() {
		return reasonval;
	}
	public void setReasonval(List<String> reasonval) {
		this.reasonval = reasonval;
	}
}

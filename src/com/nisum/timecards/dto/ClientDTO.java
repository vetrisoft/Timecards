/**
 * 
 */
package com.nisum.timecards.dto;

import java.io.Serializable;
import java.util.Map;
/**
 * @author Administrator
 *
 */
public class ClientDTO implements Serializable {
	
	private String clientId;
	private String clientName;
	private String clientLocation;
	private String clientManager;
	private String clientMailId;
	private String phoneNumber;
	private Map<String,String> clientHoildays;
	
	public Map<String, String> getClientHoildays() {
		return clientHoildays;
	}
	public void setClientHoildays(Map<String, String> clientHoildays) {
		this.clientHoildays = clientHoildays;
	}
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

}

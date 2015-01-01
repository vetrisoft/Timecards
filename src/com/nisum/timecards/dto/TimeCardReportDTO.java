package com.nisum.timecards.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TimeCardReportDTO implements Serializable  {

	private String asssignmentId;
	private String firstName;
	private String lastName;
	private Date date;
	private Double hoursSubmitted;
	private String approverName;
	private String approverEmailId;

	public String getApproverEmailId() {
		return approverEmailId;
	}

	public void setApproverEmailId(String approverEmailId) {
		this.approverEmailId = approverEmailId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Double getHoursSubmitted() {
		return hoursSubmitted;
	}

	public void setHoursSubmitted(Double hoursSubmitted) {
		this.hoursSubmitted = hoursSubmitted;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAsssignmentId() {
		return asssignmentId;
	}

	public void setAsssignmentId(String asssignmentId) {
		this.asssignmentId = asssignmentId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(lastName).append(firstName).append(asssignmentId).append(date).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == getClass()) {
			final TimeCardReportDTO other = (TimeCardReportDTO) obj;
			return new EqualsBuilder().append(lastName, other.lastName).append(firstName, other.firstName)
			        .append(asssignmentId, other.asssignmentId).append(date, other.date).isEquals();
		} else {
			return false;
		}
	}

}

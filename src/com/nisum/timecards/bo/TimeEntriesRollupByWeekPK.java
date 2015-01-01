package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "EMP_TIME_ENTR_ROLLUP_BY_WEEK_T")
public class TimeEntriesRollupByWeekPK implements Serializable {

	private static final long serialVersionUID = 8063383529269170452L;

	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	@Column(name = "TE_FOR_WEEK_ENDING")
	private Date timeEntryWeekEndDate;

	public TimeEntriesRollupByWeekPK() {
	}

	public TimeEntriesRollupByWeekPK(Integer employeeId, Date timeEntryDate) {
		this.setEmployeeId(employeeId);
		this.setTimeEntryWeekEndDate(timeEntryDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TimeEntriesRollupByWeekPK) {
			TimeEntriesRollupByWeekPK rollupByWeekPK = (TimeEntriesRollupByWeekPK) obj;

			if (!rollupByWeekPK.getEmployeeId().equals(employeeId)) {
				return false;
			}

			if (!rollupByWeekPK.getTimeEntryWeekEndDate().equals(timeEntryWeekEndDate)) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return employeeId.hashCode() + timeEntryWeekEndDate.hashCode();
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getTimeEntryWeekEndDate() {
		return timeEntryWeekEndDate;
	}

	public void setTimeEntryWeekEndDate(Date timeEntryWeekEndDate) {
		this.timeEntryWeekEndDate = timeEntryWeekEndDate;
	}

}

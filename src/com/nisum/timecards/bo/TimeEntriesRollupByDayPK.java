package com.nisum.timecards.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "EMP_TIME_ENTR_ROLLUP_BY_DAY_T")
public class TimeEntriesRollupByDayPK implements Serializable {

	private static final long serialVersionUID = -9196356102064178489L;

	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	@Column(name = "TIME_ENTRY_DT")
	private Date timeEntryDate;

	public TimeEntriesRollupByDayPK() {
	}

	public TimeEntriesRollupByDayPK(Integer employeeId, Date timeEntryDate) {
		this.setEmployeeId(employeeId);
		this.setTimeEntryDate(timeEntryDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TimeEntriesRollupByDayPK) {
			TimeEntriesRollupByDayPK rollupByDayPK = (TimeEntriesRollupByDayPK) obj;

			if (!rollupByDayPK.getEmployeeId().equals(employeeId)) {
				return false;
			}

			if (!rollupByDayPK.getTimeEntryDate().equals(timeEntryDate)) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return employeeId.hashCode() + timeEntryDate.hashCode();
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getTimeEntryDate() {
		return timeEntryDate;
	}

	public void setTimeEntryDate(Date timeEntryDate) {
		this.timeEntryDate = timeEntryDate;
	}
}
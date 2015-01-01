package com.nisum.timecards.dao;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class ReportsDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void loadAllTimeCardsData(Date fromData, Date toDate, String employeeName) {

		hibernateTemplate.findByNamedQuery("select * from emp_time_entr_rollup_by_week_t");

	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}

package com.serv4.test;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.serv4.model.Bills;
import com.serv4.model.Votes;

@RestController
@Repository
@Transactional
public class TestHibernate {
	Bills bills=new Bills("yashwanth","yes");
	Votes votes = new Votes("yash","i","yes");
	
	@Autowired
	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@RequestMapping(value="/",produces={"text/plain"},method=RequestMethod.GET)
	public String helloWorld(){
		
		return "App started";
	}
	
	@RequestMapping(value="/hi",produces={"text/plain"},method=RequestMethod.GET)
	public String hellosWorld(){
		Session session = sessionFactory.getCurrentSession();
		session.persist(votes);
		return "Bills has been saved";
	}
}

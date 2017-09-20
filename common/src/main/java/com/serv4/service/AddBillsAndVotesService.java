package com.serv4.service;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.serv4.model.Bills;
import com.serv4.model.Votes;

@Repository
@Transactional
public class AddBillsAndVotesService {

	@Autowired
	SessionFactory sessionFactory;
	
	public void addBills(Bills bills){
		Session session=sessionFactory.getCurrentSession();
		session.persist(bills);
	}
	
	public void addVotes(Votes votes){
		Session session=sessionFactory.getCurrentSession();
		session.persist(votes);
	}
	
	
}

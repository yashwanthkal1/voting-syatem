package com.serv4.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@RestController
public class AddBillsAndVotesClient {
	
	ConnectionFactory factory= new ConnectionFactory();
	
	@RequestMapping(value="/",produces={"text/plain"},method=RequestMethod.GET)
	public String helloWorld(){
		return "Application Started";
	}
	
	@RequestMapping(value = "/startMotion/{motionName}", method = RequestMethod.GET, 
			headers = "Accept=application/json",produces={"application/json"})
	public void addMotion(@PathVariable("motionName") String motionName) {
		
		try {
			factory.setHost("localhost");
			factory.setUsername("guest");
			factory.setPassword("guest");
			Connection connection = factory.newConnection();
			Channel channel=connection.createChannel();
			String queueName="MultiThreadingCreateBillQueue";
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicPublish("", queueName, null, motionName.getBytes());
			System.out.println("published message");
			
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}	
	
	@RequestMapping(value = "/vote/{name}/{billId}/{yesOrNo}", method = RequestMethod.GET, 
			headers = "Accept=application/json",produces={"application/json"})
	public void addVote(@PathVariable("name") String name,@PathVariable("billId") String billId,@PathVariable("yesOrNo") String yesOrNo) {
		
		try {
			String voteString=name+","+billId+","+yesOrNo;
			factory.setHost("localhost");
			factory.setUsername("guest");
			factory.setPassword("guest");
			Connection connection = factory.newConnection();
			Channel channel=connection.createChannel();
			String queueName="MultiThreadingCreateBillQueue";
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicPublish("", queueName, null, voteString.getBytes());
			System.out.println("published message");
			
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}

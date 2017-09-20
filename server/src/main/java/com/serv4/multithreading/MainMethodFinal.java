package com.serv4.multithreading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;


public class MainMethodFinal {
	static List<String> al = Collections
			.synchronizedList(new ArrayList<String>());

	static ArrayList<String> vpVote = new ArrayList<String>();

	private static Map<String, MotionRunner> managedMotions = new HashMap<String, MotionRunner>();

	public List<String> getAl() {
		return al;
	}

	public static void main(String[] args) throws IOException, TimeoutException {
		readInputFromUser();
	}

	/*public static void printAvailOperations() {
		System.out
				.println("1.)Enter 1 to create a new bill/motion(<name>, <starttime>, <duration>).");
		System.out
				.println("2.)Enter 2 to vote for a bill/motion(<votername> <yesorno> <billname>).");
		System.out
				.println("3.)Enter 3 to VP vote for a bill/motion(<votername> <yesorno> <billname>).");
		System.out.println("4.)Display motion/bill status(<billname>).");
	}
*/
	public static void readInputFromUser() throws IOException, TimeoutException {
		
		
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		String queueName="MultiThreadingCreateBillQueue";
		channel.queueDeclare(queueName, true, false, false, null);
		Consumer consumer = new DefaultConsumer(channel) {
			  @Override
			  public void handleDelivery(String consumerTag, Envelope envelope,
			                             BasicProperties properties, byte[] body)
			    throws IOException {
			    String queueInput = new String(body, "UTF-8");
			    System.out.println(queueInput);
			    String[] arrayInputFromQueue=queueInput.split(",");
			    if(arrayInputFromQueue.length==1){
				createMotion(arrayInputFromQueue[0]);
				System.out.println(arrayInputFromQueue[0]+" bill started");
				
				}else if(arrayInputFromQueue.length==3){
					acceptVote(arrayInputFromQueue[0], arrayInputFromQueue[1], arrayInputFromQueue[2]);
					System.out.println("Vote accepted");
				}
			  }
			};
			channel.basicConsume(queueName, true, consumer);
			
			
	}

/*	private static void displayBillStatus(Scanner sc) {
		String billName=sc.next();
		MotionRunner motionRunner = managedMotions.get(billName);
		if (motionRunner != null) {
			if (motionRunner.isOpen()) {
				System.out.println(motionRunner.toString());
			}
		}
		
	}

	private static void acceptVPVote(String voterName1, String yesOrNo1,String billName1) {
		String voterName = voterName1;
		String yesOrNo = yesOrNo1;
		String billName = billName1;
		Vote vote = new Vote(voterName, yesOrNo, billName);
		MotionRunner motionRunner = managedMotions.get(billName);
		
		if (motionRunner != null) {
			if (motionRunner.isOpen()) {
				motionRunner.addVpVote(vote);
			} else {
				// motion was managed but it is not active/open now.
			}
		} else {
			// motion not available/or managed
		}
		
	}*/

	private static void acceptVote(String voterName1, String billName1,String yesOrNo1) {
		String voterName = voterName1;
		String yesOrNo = yesOrNo1;
		String billName = billName1;

		Vote vote = new Vote(voterName, yesOrNo, billName);

		MotionRunner motionRunner = managedMotions.get(billName);
		if (motionRunner != null) {
			if (motionRunner.isOpen()) {
				motionRunner.addVote(vote);
			} else {
				// motion was managed but it is not active/open now.
			}
		} else {
			// motion not available/or managed
		}
	}

	private static void createMotion(String billName) {
		String name = billName;
		int startTimeInSecs = 2;
		int durationInSecs = 30;
		startTimeInSecs = (startTimeInSecs <= 0) ? 1 : startTimeInSecs;

		long startTimeInMillis = System.currentTimeMillis() + startTimeInSecs
				* 1000;

		MotionRunner m1 = new MotionRunner(name, startTimeInMillis,
				durationInSecs * 1000);
		m1.setName(name);
		managedMotions.put(name, m1);
		m1.start();
	}
}
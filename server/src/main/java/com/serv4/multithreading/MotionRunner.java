package com.serv4.multithreading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MotionRunner extends Thread {

	private final Map<String, Vote> submittedVotes = new HashMap<String, Vote>();

	private String billName;
	private long startTimeInMillis;
	private long durationInMillis;
	private long endTimeInMillis;

	private final LinkedList<Vote> queuedVotes = new LinkedList<Vote>();
	private final LinkedList<Vote> queuedVpVotes = new LinkedList<Vote>();

	private int numOfYes;
	private int numOfNo;

	public MotionRunner(String name, long startTimeInMillis,
			long durationInMillis) {
		this.billName = name;
		this.startTimeInMillis = startTimeInMillis;
		this.durationInMillis = durationInMillis;

		endTimeInMillis = startTimeInMillis + durationInMillis;
	}

	@Override
	public void run() {
		try {
			while (System.currentTimeMillis() < endTimeInMillis) {
				List<Vote> votesToProcess = getVotesToProcess();

				processVotes(votesToProcess);
			}

			List<Vote> votesToProcess = getVotesToProcess();
			processVotes(votesToProcess);

			if (numOfYes > numOfNo) {
				System.out.println("Bill/Motion is passed, num of yes "
						+ numOfYes + ", nos " + numOfNo);
			} else if (numOfYes < numOfNo) {
				System.out.println("Bill/Motion is failed, num of yes "
						+ numOfYes + ", nos " + numOfNo);
			} else {
				System.out
						.println("Bill/Motion is a tie, will wait for 30 secs for a VP vote.");

				evaluateMotionOnTie();
			}
		} catch (Exception exe) {
			exe.printStackTrace();
		} finally {

		}
	}

	private void evaluateMotionOnTie() throws InterruptedException {
		long endVpTime = System.currentTimeMillis() + 30000;
		while (System.currentTimeMillis() < endVpTime) {
			Vote vpVote = queuedVpVotes.poll();
			if (vpVote != null) {
				evalVote(vpVote);
			} else {
				Thread.sleep(100);
			}
		}

		if (numOfYes > numOfNo) {
			System.out.println("Bill/Motion is passed, num of yes " + numOfYes
					+ ", nos " + numOfNo);
		} else if (numOfYes < numOfNo) {
			System.out.println("Bill/Motion is failed, num of yes " + numOfYes
					+ ", nos " + numOfNo);
		}else {
			System.out.println("Motion failed, VP hasn't voted for bill");
		}
	}

	private void processVotes(List<Vote> votesToProcess)
			throws InterruptedException {
		if (votesToProcess == null || votesToProcess.isEmpty()) {
			Thread.sleep(100);
		} else {
			// process votes
			for (Vote vote : votesToProcess) {
				if (vote.getVoteTimeInMillis() < endTimeInMillis) {
					Vote prevVote = submittedVotes.get(vote.getVoterName());
					if (prevVote == null) {
						submittedVotes.put(vote.getVoterName(), vote);
						evalVote(vote);
					} else {
						// voter already submitted one.
						System.out.println(prevVote.toString());
					}
				} else {
					// ignore it
					break;
				}
			}
		}
	}

	private void evalVote(Vote vote) {
		if (vote.getYesOrNo().equals("Y")) {
			numOfYes++;
		} else {
			numOfNo++;
		}
	}

	public synchronized void addVote(Vote vote) {
		queuedVotes.add(vote);
	}
	
	public synchronized void addVpVote(Vote vote) {
		queuedVpVotes.add(vote);
	}

	private synchronized List<Vote> getVotesToProcess() {
		List<Vote> votesToProcess = new ArrayList<Vote>();
		Vote vote = queuedVotes.poll();
		while (vote != null) {
			votesToProcess.add(vote);
			vote = queuedVotes.poll();
		}
		return votesToProcess;
	}

	@Override
	public String toString() {
		StringBuilder motionInfo = new StringBuilder();
		motionInfo.append("name: ").append(billName);
		motionInfo.append("totalSubmittedVotes: ")
				.append(submittedVotes.size());
		if (numOfYes > numOfNo) {
			motionInfo.append("Bill/Motion is passed, num of yes " + numOfYes
					+ ", nos " + numOfNo);
		} else if (numOfYes < numOfNo) {
			motionInfo.append("Bill/Motion is failed, num of yes " + numOfYes
					+ ", nos " + numOfNo);
		}
		return motionInfo.toString();
	}

	public boolean isOpen() {
		return currentThread().isAlive();
	}
}
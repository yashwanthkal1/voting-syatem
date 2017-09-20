package com.serv4.multithreading;

public class Vote {
	private String voterName;
	private String yesOrNo;
	private String billName;
	private long voteTimeInMillis = System.currentTimeMillis();

	public Vote(String voterName2, String yesOrNo2, String billName2) {
		this.voterName = voterName2;
		this.yesOrNo = yesOrNo2;
		this.billName = billName2;
	}

	public long getVoteTimeInMillis() {
		return voteTimeInMillis;
	}

	public void setVoteTimeInMillis(long voteTime) {
		this.voteTimeInMillis = voteTime;
	}

	public String getVoterName() {
		return voterName;
	}

	public void setVoterName(String voterName) {
		this.voterName = voterName;
	}

	public String getYesOrNo() {
		return yesOrNo;
	}

	public void setYesOrNo(String yesOrNo) {
		this.yesOrNo = yesOrNo;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

}

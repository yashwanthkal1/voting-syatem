package com.serv4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Bills")
public class Bills {
	@Id
	@Column(name="billid")
	private String billId;
	
	private String status;
	
	public Bills(String billId, String status) {
		this.billId = billId;
		this.status = status;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}

package com.infy.hk16.gestapo.eg_client;

public class Delegate {

	public void printMessage() {
		Requestor req = new Requestor();
		System.out.println("Message - " + req.getMessage());
		
	}
	
}

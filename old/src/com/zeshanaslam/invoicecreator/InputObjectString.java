package com.zeshanaslam.invoicecreator;


public class InputObjectString {
	
	public String store;
	public String date;
	public String model;
	public String serial;
	public String desc;
	public String price;
	public String status;
	
	public InputObjectString(String store, String date, String modal, String serial, String desc, String price, String status) {
		this.store = store;
		this.date = date;
		this.model = modal;
		this.serial = serial;
		this.desc = desc;
		this.price = price;
		this.status = status;
	}
}

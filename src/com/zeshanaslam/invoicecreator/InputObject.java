package com.zeshanaslam.invoicecreator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InputObject {
	
	public StringProperty store;
	public StringProperty date;
	public StringProperty model;
	public StringProperty serial;
	public StringProperty desc;
	public StringProperty price;
	public StringProperty status;
	
	public InputObject(String store, String date, String modal, String serial, String desc, String price, String status) {
		this.store = new SimpleStringProperty(store);
		this.date = new SimpleStringProperty(date);
		this.model = new SimpleStringProperty(modal);
		this.serial = new SimpleStringProperty(serial);
		this.desc = new SimpleStringProperty(desc);
		this.price = new SimpleStringProperty(price);
		this.status = new SimpleStringProperty(status);
	}
}

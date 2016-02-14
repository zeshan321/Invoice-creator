package com.zeshanaslam.invoicecreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DataDB {

	private Connection connection = null;

	public DataDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:data.db");

			firstTimeSetup();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void createInput(String date, String store, String modal, String snumber, String desc, double price, String status) {
		try {
			String sql = "INSERT INTO Inputs"
					+ "(Date, Store, Model, Serial, Description, Price, Status) VALUES"
					+ "(?,?,?,?,?,?,?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, date);
			preparedStatement.setString(2, store);
			preparedStatement.setString(3, modal);
			preparedStatement.setString(4, snumber);
			preparedStatement.setString(5, desc);
			preparedStatement.setDouble(6, price);
			preparedStatement.setString(7, status);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateInputString (String ID, String date, String store, String modal, String snumber, String desc, double price, String status) {
		try {
			String sql = "UPDATE Inputs SET "
					+ "Date = ?, Store = ?, Model = ?, Serial = ?, Description = ?, Price = ?, Status = ? WHERE ID = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, date);
			preparedStatement.setString(2, store);
			preparedStatement.setString(3, modal);
			preparedStatement.setString(4, snumber);
			preparedStatement.setString(5, desc);
			preparedStatement.setDouble(6, price);
			preparedStatement.setString(7, status);
			preparedStatement.setString(8, ID);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<InputObject> getInputs(String type, String query) {
		List<InputObject> inputList = new ArrayList<>();
		
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Inputs WHERE "+ type + " = '" + query + "';";
			
			if (type.equals("All")) {
				sql = "SELECT * FROM Inputs;";
			}
			
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				inputList.add(new InputObject(rs.getString("ID"), rs.getString("Store"), rs.getString("Date"), rs.getString("Model"), rs.getString("Serial"), rs.getString("Description"), String.valueOf(rs.getDouble("Price")), rs.getString("Status")));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return inputList;
	}
	
	public List<InputObjectString> getDates(String date1, String date2) {
		List<InputObjectString> inputList = new ArrayList<>();
		
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Inputs WHERE Date BETWEEN '"+ date1 + "' and '" + date2 + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				inputList.add(new InputObjectString(rs.getString("Store"), rs.getString("Date"), rs.getString("Model"), rs.getString("Serial"), rs.getString("Description"), String.valueOf(rs.getDouble("Price")), rs.getString("Status")));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return inputList;
	}
	
	private void firstTimeSetup() {
		PreparedStatement satement = null;

		try {
			//connection.setAutoCommit(false);

			String sql = "CREATE TABLE IF NOT EXISTS Inputs " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Date TEXT NOT NULL, " + 
					"Store TEXT NOT NULL, " + 
					"Model TEXT NOT NULL, " +
					"Serial TEXT NOT NULL, " +
					"Description TEXT NOT NULL, " +
					"Price DOUBLE NOT NULL, " +
					"Status TEXT NOT NULL)"; 

			satement = connection.prepareStatement(sql);
			satement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

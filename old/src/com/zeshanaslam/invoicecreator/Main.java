package com.zeshanaslam.invoicecreator;

import application.MainView;
import javafx.application.Application;
import javafx.stage.Stage;
import util.ConfigLoader;

public class Main extends Application {
	
	public static DataDB dataDB = null;
	public static ConfigLoader configLoader = null;

	@Override
	public void start(Stage primaryStage) throws Exception {}

	public static void main(String[] args) {
		new Thread() {
            @Override
            public void run() {
            	// Setup database
            	dataDB = new DataDB();
            	
            	// Setup config
            	configLoader = new ConfigLoader();
            	
            	// Start first view
                javafx.application.Application.launch(MainView.class);
            }
        }.start();
	}
}

package com.zeshanaslam.invoicecreator;

import application.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static DataDB dataDB = null;

	@Override
	public void start(Stage primaryStage) throws Exception {}

	public static void main(String[] args) {
		new Thread() {
            @Override
            public void run() {
            	// Setup database
            	dataDB = new DataDB();
            	
            	// Start first view
                javafx.application.Application.launch(MainView.class);
            }
        }.start();
	}
}

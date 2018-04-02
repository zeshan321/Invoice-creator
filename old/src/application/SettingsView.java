package application;

import java.io.IOException;

import controller.SettingsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsView extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	
	private double X;
	private double Y;

	public SettingsView(double X, double Y) {
		this.X = X;
		this.Y = Y;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		initRootLayout();
	}

	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SettingsView.class.getResource("/layouts/settings_layout.fxml"));
			rootLayout = (AnchorPane) loader.load();
			
			// Get controller
			SettingsController controller = loader.<SettingsController> getController();
			controller.stage = primaryStage;

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);

			if (X != 0.0) {
				primaryStage.setX(X);
				primaryStage.setY(Y);
			}
			
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

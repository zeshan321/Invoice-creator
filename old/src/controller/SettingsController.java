package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zeshanaslam.invoicecreator.Main;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import util.ConfigLoader;

public class SettingsController implements Initializable {

	// Toolbar
	@FXML private AnchorPane ap_main;
	@FXML private Rectangle rec_toolbar;
	@FXML private Label label_toolbar;

	// Buttons
	@FXML private Button bt_save;
	@FXML private Button bt_cancel;
	@FXML private Label bt_exit;

	// Screen size
	private double initialX;
	private double initialY;
	private double newX;
	private double newY;

	// Company 1
	@FXML private TextField tf_name;
	@FXML private TextField tf_address;
	@FXML private TextField tf_postal;
	@FXML private TextField tf_email;
	@FXML private TextField tf_phone;

	// Company 2
	@FXML private TextField tf_name1;
	@FXML private TextField tf_address1;
	@FXML private TextField tf_postal1;
	@FXML private TextField tf_email1;
	@FXML private TextField tf_phone1;

	// Other
	@FXML private TextField tf_tax;
	@FXML private TextField tf_invoice;
	@FXML private TextField tf_sales;

	// Current stage
	public Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allowDrag();

		ConfigLoader configLoader = Main.configLoader;
		tf_name.setText(configLoader.getString("Name"));
		tf_address.setText(configLoader.getString("Address"));
		tf_postal.setText(configLoader.getString("Postal"));
		tf_email.setText(configLoader.getString("Email"));
		tf_phone.setText(configLoader.getString("Phone"));
		tf_name1.setText(configLoader.getString("Name1"));
		tf_address1.setText(configLoader.getString("Address1"));
		tf_postal1.setText(configLoader.getString("Postal1"));
		tf_email1.setText(configLoader.getString("Email1"));
		tf_phone1.setText(configLoader.getString("Phone1"));
		tf_invoice.setText(configLoader.getString("InvoiceNO"));
		tf_sales.setText(configLoader.getString("Sales"));
		tf_tax.setText(configLoader.getString("Tax"));


		bt_save.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				configLoader.updateConfig(tf_name.getText(), tf_address.getText(), tf_postal.getText(), 
						tf_email.getText(), tf_phone.getText(), tf_name1.getText(), tf_address1.getText(), 
						tf_postal1.getText(), tf_email1.getText(), tf_phone1.getText(), tf_invoice.getText(), tf_sales.getText(), tf_tax.getText());
				
				Main.configLoader = new ConfigLoader();
				stage.close();
			}
		});

		bt_cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Cancel");
				alert.setContentText("Are you sure you want to exit without saving?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					stage.close();
				} else {
					alert.close();
				}
			}
		});

		bt_exit.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Cancel");
				alert.setContentText("Are you sure you want to exit without saving?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					stage.close();
				} else {
					alert.close();
				}
			}
		});
	}

	private void allowDrag() {
		rec_toolbar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					initialX = me.getSceneX();
					initialY = me.getSceneY();
				}
			}
		});

		rec_toolbar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					newX = me.getScreenX() - initialX;
					newY = me.getScreenY() - initialY;

					ap_main.getScene().getWindow().setX(newX);
					ap_main.getScene().getWindow().setY(newY);

					ap_main.requestFocus();
				}
			}
		});

		label_toolbar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					initialX = me.getSceneX();
					initialY = me.getSceneY();
				}
			}
		});

		label_toolbar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if (me.getButton() != MouseButton.MIDDLE) {
					newX = me.getScreenX() - initialX;
					newY = me.getScreenY() - initialY;

					ap_main.getScene().getWindow().setX(newX);
					ap_main.getScene().getWindow().setY(newY);

					ap_main.requestFocus();
				}
			}
		});
	}
}

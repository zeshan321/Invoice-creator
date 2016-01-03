package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zeshanaslam.invoicecreator.DataDB;
import com.zeshanaslam.invoicecreator.InputObject;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CreateController implements Initializable {

	// Toolbar
	@FXML private AnchorPane ap_main;
	@FXML private Rectangle rec_toolbar;
	@FXML private Label label_toolbar;

	// Inputs
	@FXML private TextField tf_store;
	@FXML private DatePicker dp_date;
	@FXML private TextField tf_model;
	@FXML private TextField tf_serial;
	@FXML private TextField tf_status;
	@FXML private TextField tf_price;
	@FXML private TextArea ta_desc;

	// Buttons
	@FXML private Button bt_save;
	@FXML private Button bt_cancel;
	@FXML private Label bt_exit;

	// Screen size
	private double initialX;
	private double initialY;
	private double newX;
	private double newY;

	private String ID;
	private SearchController controller;
	
	public Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allowDrag();

		dp_date.setValue(LocalDate.now());

		bt_save.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!tf_store.getText().isEmpty() && !tf_model.getText().isEmpty() && !tf_serial.getText().isEmpty() && !tf_status.getText().isEmpty() && !tf_price.getText().isEmpty() && !ta_desc.getText().isEmpty()) {
					DataDB dataDB = new DataDB();

					if (ID == null) {
						dataDB.createInput(dp_date.getValue().toString(), tf_store.getText(), tf_model.getText(), tf_serial.getText(), ta_desc.getText(), Double.valueOf(tf_price.getText()), tf_status.getText());
					} else {
						dataDB.updateInputString(ID, dp_date.getValue().toString(), tf_store.getText(), tf_model.getText(), tf_serial.getText(), ta_desc.getText(), Double.valueOf(tf_price.getText()), tf_status.getText());
					}
					
					if (controller != null) {
						controller.loadTable();
					}
					
					stage.close();
				}
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

	public void setData(InputObject object, SearchController controller) {
		if (object == null) {
			return;
		}
		this.controller = controller;
		
		String[] dateSplit = object.date.get().split("-");
		LocalDate localDate = LocalDate.of(Integer.parseInt(dateSplit[0]), Month.of(Integer.parseInt(dateSplit[1])), Integer.parseInt(dateSplit[2]));
		dp_date.setValue(localDate);
		
		tf_store.setText(object.store.get());
		tf_model.setText(object.model.get());
		tf_serial.setText(object.serial.get());
		tf_status.setText(object.status.get());
		tf_price.setText(object.price.get());
		ta_desc.setText(object.desc.get());

		ID = object.ID;
	}
}

package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.zeshanaslam.invoicecreator.DataDB;
import com.zeshanaslam.invoicecreator.InputObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SearchController implements Initializable {

	// Toolbar
	@FXML private AnchorPane ap_main;
	@FXML private Rectangle rec_toolbar;
	@FXML private Label label_toolbar;

	// Inputs
	@FXML private TextField tf_search;
	@FXML private ChoiceBox<String> cb_search;
	@FXML private TableView<InputObject> table_search;

	// Buttons
	@FXML private Button bt_start_search;

	// Screen size
	private double initialX;
	private double initialY;
	private double newX;
	private double newY;

	// Current stage
	public Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allowDrag();

		cb_search.getItems().addAll(Arrays.asList("Store", "Date", "Model", "Serial", "Status"));

		bt_start_search.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (tf_search.getText().isEmpty() && cb_search.getSelectionModel().getSelectedItem() == null) {
					return;
				}

				// Clear table
				table_search.getItems().clear();
				table_search.getColumns().clear();

				DataDB dataDB = new DataDB();

				ObservableList<InputObject> data = FXCollections.observableArrayList(); 
				data.addAll(dataDB.getInputs(cb_search.getSelectionModel().getSelectedItem(), tf_search.getText()));
				table_search.setItems(data);

				TableColumn<InputObject, String> store = new TableColumn<InputObject, String>("Store");
				store.setCellValueFactory(cellData -> cellData.getValue().store);

				TableColumn<InputObject, String> date = new TableColumn<InputObject, String>("Date");
				date.setCellValueFactory(cellData -> cellData.getValue().date);

				TableColumn<InputObject, String> model = new TableColumn<InputObject, String>("Model");
				model.setCellValueFactory(cellData -> cellData.getValue().model);

				TableColumn<InputObject, String> serial = new TableColumn<InputObject, String>("Serial");
				serial.setCellValueFactory(cellData -> cellData.getValue().serial);

				TableColumn<InputObject, String> desc = new TableColumn<InputObject, String>("Description");
				desc.setCellValueFactory(cellData -> cellData.getValue().desc);

				TableColumn<InputObject, String> price = new TableColumn<InputObject, String>("Price");
				price.setCellValueFactory(cellData -> cellData.getValue().price);

				TableColumn<InputObject, String> status = new TableColumn<InputObject, String>("Status");
				status.setCellValueFactory(cellData -> cellData.getValue().status);

				table_search.getColumns().addAll(store, date, model, serial, desc, price, status);
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

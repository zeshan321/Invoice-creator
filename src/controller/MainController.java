package controller;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zeshanaslam.invoicecreator.BackupHandler;
import com.zeshanaslam.invoicecreator.DateObject;
import com.zeshanaslam.invoicecreator.Exporter;

import application.CreateView;
import application.SearchView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable {

	// Toolbar
	@FXML private AnchorPane ap_main;
	@FXML private Rectangle rec_toolbar;
	@FXML private Label label_toolbar;

	// Buttons
	@FXML private Button bt_create;
	@FXML private Button bt_search;
	@FXML private Button bt_export;
	@FXML private Label bt_exit;

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

		bt_exit.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				try {
					new BackupHandler().startBackup();
					Platform.exit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		bt_create.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				try {
					new CreateView(null, newX, newY).start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		bt_search.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				try {
					new SearchView(newX, newY).start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		bt_export.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				try {
					Dialog<DateObject> dialog = new Dialog<>();
					dialog.setTitle("Export");
					dialog.setResizable(true);

					Label label1 = new Label("From: ");
					Label label2 = new Label("To: ");
					DatePicker date1 = new DatePicker();
					DatePicker date2 = new DatePicker();
					RadioButton pdf = new RadioButton("PDF");
					RadioButton excel = new RadioButton("Excel");

					GridPane grid = new GridPane();
					grid.add(label1, 0, 0);
					grid.add(date1, 1, 0);
					grid.add(label2, 0, 1);
					grid.add(date2, 1, 1);
					grid.add(pdf, 0, 3);
					grid.add(excel, 0, 2);
					dialog.getDialogPane().setContent(grid);

					ButtonType buttonTypeOk = new ButtonType("Submit", ButtonData.APPLY);
					dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

					dialog.setResultConverter(new Callback<ButtonType, DateObject>() {
						@Override
						public DateObject call(ButtonType b) {

							if (b == buttonTypeOk) {
								if (date1.getValue() != null || date2.getValue() != null) {

									if (pdf.isSelected() && !excel.isSelected()) {
										return new DateObject(date1.getValue().toString(), date2.getValue().toString(), 0);
									}

									if (!pdf.isSelected() && excel.isSelected()) {
										return new DateObject(date1.getValue().toString(), date2.getValue().toString(), 1);
									}

									if (pdf.isSelected() && excel.isSelected()) {
										return new DateObject(date1.getValue().toString(), date2.getValue().toString(), 2);
									}
								}
							}

							return null;
						}
					});

					Optional<DateObject> result = dialog.showAndWait();

					if (result.isPresent()) {
						new Exporter(result.get().type, result.get().date1, result.get().date2);
					}
				} catch (Exception e) {
					e.printStackTrace();
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

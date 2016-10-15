package PaintGUI;

import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to intialize JFX components currently for testing purposes
 * 
 * @author Jordan Gagnon
 */
public class JFXPaint extends Application {

	// Variables for canvas width and height
	public int cw = 700;
	public int ch = 400;

	private final Label toolLabel = new Label("Tools:");

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {

		Scene scene = new Scene(new Group());
		stage.setTitle("JFXPaint");
		stage.setWidth(800);
		stage.setHeight(600);

		// Declare the canvas
		JFXCanvas jc = new JFXCanvas();
		Canvas canvas = new Canvas(cw, ch);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		jc.draw(gc, canvas);

		// Group that Contains all the toggle buttons, only one or no buttons in
		// this group can be selected
		final ToggleGroup tools = new ToggleGroup();

		tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					// Do Nothing on no change
				} else {

					// Calls Methods when specific tool buttons are pressed
					int tool = (int) tools.getSelectedToggle().getUserData();
					switch (tool) {
					case 0:
						jc.tool = 0;
						break;
					case 1:
						jc.tool = 1;
						break;
					case 2:
						jc.tool = 2;
						break;
					case 3:
						jc.tool = 3;
						break;
					default:
						System.out.println("Default");
						break;

					}
				}

			}
		});

		// Save Option
		Button saveBtn = new Button("Save");
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(stage);

				if (file != null) {
					try {
						WritableImage writableImage = new WritableImage(cw, ch);
						canvas.snapshot(null, writableImage);
						RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
						ImageIO.write(renderedImage, "png", file);
					} catch (IOException ex) {
						Logger.getLogger(JFXPaint.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

		});

		Button clearBtn = new Button("Clear");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				jc.clear(gc, canvas);
			}
		});

		// Declare the color picker
		final ColorPicker colorPicker = new ColorPicker();

		// Event that calls the change color method when the color pickers color
		// is changed
		colorPicker.setOnAction(new EventHandler() {
			public void handle(Event t) {
				jc.changeColor(colorPicker.getValue(), gc);
			}
		});

		ToggleButton drawLineBtn = new ToggleButton("Line");
		drawLineBtn.setToggleGroup(tools);
		drawLineBtn.setUserData(0);
		drawLineBtn.setSelected(true);
		drawLineBtn.setStyle("-fx-base: lightgreen;");

		ToggleButton rectButton = new ToggleButton("Rect");
		rectButton.setToggleGroup(tools);
		rectButton.setUserData(1);
		rectButton.setStyle("-fx-base: lightblue;");

		ToggleButton circleBtn = new ToggleButton("Circle");
		circleBtn.setToggleGroup(tools);
		circleBtn.setUserData(2);
		circleBtn.setStyle("-fx-base: plum;");

		ToggleButton eraserBtn = new ToggleButton("Eraser");
		eraserBtn.setToggleGroup(tools);
		eraserBtn.setUserData(3);
		eraserBtn.setStyle("-fx-base: salmon;");

		HBox toolBox = new HBox();

		toolBox.getChildren().add(drawLineBtn);
		toolBox.getChildren().add(rectButton);
		toolBox.getChildren().add(circleBtn);
		toolBox.getChildren().add(eraserBtn);

		HBox tray = new HBox();

		tray.getChildren().add(colorPicker);
		tray.getChildren().add(clearBtn);
		tray.getChildren().add(saveBtn);

		VBox vbox = new VBox();

		vbox.getChildren().add(toolLabel);
		vbox.getChildren().add(toolBox);
		vbox.getChildren().add(canvas);
		// vbox.getChildren().add(swingNode);
		vbox.getChildren().add(tray);
		vbox.setPadding(new Insets(20, 10, 10, 20));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

}

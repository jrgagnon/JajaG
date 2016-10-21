package PaintGUI;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	public double cw = 760;
	public double ch = 480;

	public double fitW = 40.0;
	public double fitH = 40.0;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {

		Scene scene = new Scene(new Group());
		stage.setTitle("JFXPaint");
		// stage.setWidth(800);
		// stage.setHeight(600);
		stage.setMaximized(true);

		// Declare the canvas
		JFXCanvas jc = new JFXCanvas();
		Canvas canvas = new Canvas(cw, ch);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		jc.draw(gc, canvas);

		// Line Size Chooser
		ChoiceBox lineSize = new ChoiceBox(FXCollections.observableArrayList("1pt", "25pt", "50pt", "100pt"));

		lineSize.getSelectionModel().select(0);

		lineSize.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue ov, Number value, Number new_value) {
				jc.lineSize = new_value.intValue();
				jc.changeLineSize(gc);
			}

		});

		// Changes the size of the canvas when the window resizes
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				cw = ((double) newSceneWidth - 40.0);
				canvas.setWidth(cw);
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				ch = ((double) newSceneHeight - 120.0);
				canvas.setHeight(ch);
			}
		});

		// Group that Contains all the toggle buttons, only one or no buttons in
		// this group can be selected
		final ToggleGroup tools = new ToggleGroup();

		tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					// Do Nothing on no change
				} else {

					// Calls Methods when specific tool buttons are pressed
					// Sets the line size to the appropriate default
					int tool = (int) tools.getSelectedToggle().getUserData();
					switch (tool) {
					case 0:
						lineSize.getSelectionModel().select(0);
						jc.tool = 0;
						break;
					case 1:
						lineSize.getSelectionModel().select(0);
						jc.tool = 1;
						break;
					case 2:
						lineSize.getSelectionModel().select(0);
						jc.tool = 2;
						break;
					case 3:
						jc.tool = 3;
						lineSize.getSelectionModel().select(2);
						break;
					default:
						System.out.println("Default");
						break;

					}
				}

			}
		});

		// Beggining of Menu Bar Section
		// Create Menu
		MenuBar menuBar = new MenuBar();

		// --- File Menu Option
		Menu menuFile = new Menu("File");

		// Adds the menu options to the menu bar
		// just , (menuoption) in the addAll to add
		menuBar.getMenus().addAll(menuFile);

		// Save Option
		MenuItem save = new MenuItem("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();

				// Set extension filter
				FileChooser.ExtensionFilter imageExtensions = new FileChooser.ExtensionFilter("all images", "*.jpg",
						"*.png", "*.JPG", "*.PNG", "*.jpeg", ".JPEG");

				FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg",
						"*.JPG", "*.jpeg", ".JPEG");

				FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png",
						"*.PNG");
				fileChooser.getExtensionFilters().addAll(imageExtensions, extFilterjpg, extFilterpng);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(stage);

				if (file != null) {
					try {
						WritableImage writableImage = new WritableImage((int) cw, (int) ch);
						canvas.snapshot(null, writableImage);
						RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
						ImageIO.write(renderedImage, "png", file);
					} catch (IOException ex) {
						Logger.getLogger(JFXPaint.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

		});

		// Open Option
		MenuItem open = new MenuItem("Open");
		open.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();

				FileChooser.ExtensionFilter imageExtensions = new FileChooser.ExtensionFilter("all images", "*.jpg",
						"*.png", "*.JPG", "*.PNG", "*.jpeg", ".JPEG");

				FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg",
						"*.JPG", "*.jpeg", ".JPEG");

				FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png",
						"*.PNG");
				fileChooser.getExtensionFilters().addAll(imageExtensions, extFilterjpg, extFilterpng);

				// Show open file dialog
				File file = fileChooser.showOpenDialog(null);

				if (file == null) {
					// Do Nothing
				} else {

					try {
						BufferedImage bufferedImage = ImageIO.read(file);
						Image image = SwingFXUtils.toFXImage(bufferedImage, null);
						jc.imageDraw(gc, canvas, image);
					} catch (IOException ex) {
						Logger.getLogger(JFXPaint.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

			}

		});

		// Clear Option
		MenuItem clear = new MenuItem("New");
		clear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				jc.clear(gc, canvas);
			}
		});

		// Add menu options to file portion of the menu bar
		menuFile.getItems().addAll(clear, open, save);

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

		Label toolLabel = new Label("Tools:");

		Image brushImage = new Image(getClass().getResourceAsStream("/icons/brush.png"));
		ImageView scaledBrush = new ImageView(brushImage);
		scaledBrush.setFitHeight(fitH);
		scaledBrush.setFitWidth(fitW);

		ToggleButton drawLineBtn = new ToggleButton(null, scaledBrush);
		drawLineBtn.setToggleGroup(tools);
		drawLineBtn.setUserData(0);
		drawLineBtn.setSelected(true);
		drawLineBtn.setStyle("-fx-base: lightgreen;");

		Image rectImage = new Image(getClass().getResourceAsStream("/icons/square.png"));
		ImageView scaledRect = new ImageView(rectImage);
		scaledRect.setFitHeight(fitH);
		scaledRect.setFitWidth(fitW);

		ToggleButton rectButton = new ToggleButton(null, scaledRect);
		rectButton.setToggleGroup(tools);
		rectButton.setUserData(1);
		rectButton.setStyle("-fx-base: lightblue;");

		Image circleImage = new Image(getClass().getResourceAsStream("/icons/circle.png"));
		ImageView scaledCircle = new ImageView(circleImage);
		scaledCircle.setFitHeight(fitH);
		scaledCircle.setFitWidth(fitW);

		ToggleButton circleBtn = new ToggleButton(null, scaledCircle);
		circleBtn.setToggleGroup(tools);
		circleBtn.setUserData(2);
		circleBtn.setStyle("-fx-base: plum;");

		Image eraserImage = new Image(getClass().getResourceAsStream("/icons/eraser.png"));
		ImageView scaledEraser = new ImageView(eraserImage);
		scaledEraser.setFitHeight(fitH);
		scaledEraser.setFitWidth(fitW);

		ToggleButton eraserBtn = new ToggleButton(null, scaledEraser);
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
		// tray.getChildren().add(clearBtn);
		tray.getChildren().add(lineSize);

		VBox vbox = new VBox();

		vbox.getChildren().add(menuBar);
		vbox.getChildren().add(toolLabel);
		vbox.getChildren().add(toolBox);
		vbox.getChildren().add(canvas);
		vbox.getChildren().add(tray);
		vbox.setPadding(new Insets(5, 10, 5, 10));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

}

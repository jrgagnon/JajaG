package PaintGUI;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

	public double toolSize = 1.00;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {

		Scene scene = new Scene(new Group());
		stage.setTitle("Penguin Paint");
		stage.getIcons().add(new Image("/icons/penguin_2.png"));
		stage.setWidth(800);
		stage.setHeight(600);
		// stage.setMaximized(true);

		URL url = this.getClass().getResource("JFX.css");
		String css = url.toExternalForm();
		scene.getStylesheets().add(css);

		// Declare the canvas
		JFXCanvas jc = new JFXCanvas();
		Canvas canvas = new Canvas(cw, ch);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		jc.draw(gc, canvas);

		// Line Size Chooser

		// ChoiceBox lineSize = new
		// ChoiceBox(FXCollections.observableArrayList("1pt", "25pt", "50pt",
		// "100pt"));
		//
		// lineSize.getSelectionModel().select(0);
		//
		// lineSize.getSelectionModel().selectedIndexProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// public void changed(ObservableValue ov, Number value, Number
		// new_value) {
		// jc.lineSize = new_value.intValue();
		// jc.changeLineSize(gc);
		// }
		//
		// });

		// new tool size
		NumberTextField toolSizeTxt = new NumberTextField();
		toolSizeTxt.setPrefSize(50, 30);
		toolSizeTxt.setAlignment(Pos.BASELINE_RIGHT);
		toolSizeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				toolSize = Double.parseDouble(newValue);
				jc.changeToolSize(gc, toolSize);
			} catch (Exception e) {

			}
		});
		toolSizeTxt.setText("1.0");

		Button addSize = new Button("+");
		addSize.setPrefSize(30, 30);
		addSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				toolSize++;
				toolSizeTxt.setText(String.valueOf(toolSize));
			}
		});

		Button subSize = new Button("-");
		subSize.setPrefSize(30, 30);
		subSize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				if (toolSize > 1) {
					toolSize--;
					toolSizeTxt.setText(String.valueOf(toolSize));
				}
			}
		});
		
		TextField textBox = new TextField();
		textBox.setPrefSize(200, 30);
		textBox.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent event) {
		        jc.setText(textBox.getText());
		    }     
		});
		
		final Tooltip textTooltip = new Tooltip();
		textTooltip.setText(
		    "Press Enter to confirm text"
		);

		textBox.setTooltip(textTooltip);
		textBox.setVisible(false);
		
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
				ch = ((double) newSceneHeight - 130.0);
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
						// lineSize.getSelectionModel().select(0);
						jc.tool = 0;
						textBox.setVisible(false);
						break;
					case 1:
						// lineSize.getSelectionModel().select(0);
						jc.tool = 1;
						textBox.setVisible(false);
						break;
					case 2:
						// lineSize.getSelectionModel().select(0);
						jc.tool = 2;
						textBox.setVisible(false);
						break;
					case 3:
						jc.tool = 3;
						// lineSize.getSelectionModel().select(2);
						textBox.setVisible(false);
						break;
					case 4:
						jc.tool = 4;
						textBox.setVisible(false);
						break;
					case 5:
						jc.tool = 5;
						textBox.setVisible(true);
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
		//final ColorPicker colorPicker = new ColorPicker();
		final ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setPrefHeight(30);

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
		drawLineBtn.setPadding(Insets.EMPTY);
		drawLineBtn.setToggleGroup(tools);
		drawLineBtn.setUserData(0);
		drawLineBtn.setSelected(true);
		//drawLineBtn.setStyle("-fx-base: lightgreen;");

		Image rectImage = new Image(getClass().getResourceAsStream("/icons/square.png"));
		ImageView scaledRect = new ImageView(rectImage);
		scaledRect.setFitHeight(fitH);
		scaledRect.setFitWidth(fitW);

		ToggleButton rectButton = new ToggleButton(null, scaledRect);
		rectButton.setPadding(Insets.EMPTY);
		rectButton.setToggleGroup(tools);
		rectButton.setUserData(1);
		//rectButton.setStyle("-fx-base: lightblue;");

		Image circleImage = new Image(getClass().getResourceAsStream("/icons/circle.png"));
		ImageView scaledCircle = new ImageView(circleImage);
		scaledCircle.setFitHeight(fitH);
		scaledCircle.setFitWidth(fitW);

		ToggleButton circleBtn = new ToggleButton(null, scaledCircle);
		circleBtn.setPadding(Insets.EMPTY);
		circleBtn.setToggleGroup(tools);
		circleBtn.setUserData(2);
		//circleBtn.setStyle("-fx-base: plum;");

		Image eraserImage = new Image(getClass().getResourceAsStream("/icons/eraser.png"));
		ImageView scaledEraser = new ImageView(eraserImage);
		scaledEraser.setFitHeight(fitH);
		scaledEraser.setFitWidth(fitW);

		ToggleButton eraserBtn = new ToggleButton(null, scaledEraser);
		eraserBtn.setPadding(Insets.EMPTY);
		eraserBtn.setToggleGroup(tools);
		eraserBtn.setUserData(3);
		//eraserBtn.setStyle("-fx-base: salmon;");
		
		Image penguinImage = new Image(getClass().getResourceAsStream("/icons/penguin_2.png"));
		ImageView scaledPenguin = new ImageView(penguinImage);
		scaledPenguin.setFitHeight(fitH);
		scaledPenguin.setFitWidth(fitW);
		
		ToggleButton textBtn = new ToggleButton(null, scaledPenguin);
		textBtn.setPadding(Insets.EMPTY);
		textBtn.setToggleGroup(tools);
		textBtn.setUserData(5);

		HBox toolBox = new HBox();

		toolBox.getChildren().add(drawLineBtn);
		toolBox.getChildren().add(rectButton);
		toolBox.getChildren().add(circleBtn);
		toolBox.getChildren().add(eraserBtn);
		toolBox.getChildren().add(textBtn);
		

		HBox tray = new HBox();

		tray.getChildren().add(colorPicker);
		// tray.getChildren().add(clearBtn);
		// tray.getChildren().add(lineSize);
		tray.getChildren().add(subSize);
		tray.getChildren().add(toolSizeTxt);
		tray.getChildren().add(addSize);
		tray.getChildren().add(textBox);
		
		VBox vbox = new VBox();

		vbox.getChildren().add(menuBar);
		vbox.getChildren().add(toolLabel);
		vbox.getChildren().add(toolBox);
		vbox.getChildren().add(canvas);
		vbox.getChildren().add(tray);
		vbox.setPadding(new Insets(0, 10, 0, 10));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

}

package PaintGUI;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;

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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

	public File file = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		// Declare the canvas
		JFXCanvas jc = new JFXCanvas();
		Canvas canvas = new Canvas(cw, ch);
		
		// Set up a ScrollPane
		ScrollPane sp = new ScrollPane();
		sp.setContent(canvas);
		
		sp.setStyle("-fx-background: rgb(255,255,255);");
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		Group group = new Group();
		Scene scene = new Scene(group);
		stage.setTitle("JFXPaint");
		stage.setTitle("Penguin Paint");
		stage.getIcons().add(new Image("/icons/penguin_2.png"));
		stage.setWidth(800);
		stage.setHeight(650);
		// stage.setMaximized(true);

		URL url = this.getClass().getResource("JFX.css");
		String css = url.toExternalForm();
		scene.getStylesheets().add(css);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		jc.draw(gc, canvas);

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

		// textbox for the text tool
		TextField textBox = new TextField();
		textBox.setPrefSize(200, 30);
		textBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				jc.setText(textBox.getText());
			}
		});

		// Tooltip for the textbox
		final Tooltip textTooltip = new Tooltip();
		textTooltip.setText("Press Enter to confirm text");

		textBox.setTooltip(textTooltip);
		textBox.setVisible(false);

		final String[] fonts = new String[] { "Arial", "Courier", "Serif", "Times New Roman" };

		ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Arial", "Courier", "Serif", "Times New Roman"));

		cb.setPrefSize(90, 30);
		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue ov, Number value, Number new_value) {
				jc.setFont(fonts[new_value.intValue()]);
				jc.changeFont(gc);
			}
		});
		cb.getSelectionModel().select(0);
		cb.setVisible(false);

		Button bold = new Button("B");
		bold.setPrefSize(30, 30);
		bold.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (jc.bold) {
					jc.bold = false;
					jc.changeFont(gc);
				} else {
					jc.bold = true;
					jc.changeFont(gc);
				}

			}
		});
		bold.setVisible(false);

		Button italic = new Button("I");
		italic.setPrefSize(30, 30);
		italic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (jc.italic) {
					jc.italic = false;
					jc.changeFont(gc);
				} else {
					jc.italic = true;
					jc.changeFont(gc);
				}

			}
		});
		italic.setVisible(false);

		NumberTextField fontSizeTxt = new NumberTextField();
		fontSizeTxt.setPrefSize(50, 30);
		fontSizeTxt.setAlignment(Pos.BASELINE_RIGHT);
		fontSizeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				double fontSizeHere = Double.parseDouble(newValue);
				jc.fontSize = fontSizeHere;
				jc.changeFont(gc);
			} catch (Exception e) {

			}
		});
		fontSizeTxt.setText("12.0");
		fontSizeTxt.setVisible(false);

		Button addFont = new Button("+");
		addFont.setPrefSize(30, 30);
		addFont.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				jc.fontSize++;
				fontSizeTxt.setText(String.valueOf(jc.fontSize));
				jc.changeFont(gc);
			}
		});
		addFont.setVisible(false);

		Button subFont = new Button("-");
		subFont.setPrefSize(30, 30);
		subFont.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				if (jc.fontSize > 1) {
					jc.fontSize--;
					fontSizeTxt.setText(String.valueOf(jc.fontSize));
					jc.changeFont(gc);
				}
			}
		});
		subFont.setVisible(false);

		// Changes the size of the canvas when the window resizes
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				cw = ((double) newSceneWidth - 40.0);
				
				// Increase canvas size on window expansion
				if (canvas.getWidth() < cw)
					canvas.setWidth(cw);
				
				sp.setPrefWidth(cw);
			}
		});

		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				ch = ((double) newSceneHeight - 130.0);
				
				if (canvas.getHeight() < ch) 
					canvas.setHeight(ch);
				
				sp.setPrefHeight(ch);
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
						jc.tool = 0;
						textReset(textBox, cb, bold, italic, subFont, addFont, fontSizeTxt);
						break;
					case 1:
						jc.tool = 1;
						textReset(textBox, cb, bold, italic, subFont, addFont, fontSizeTxt);
						break;
					case 2:
						jc.tool = 2;
						textReset(textBox, cb, bold, italic, subFont, addFont, fontSizeTxt);
						break;
					case 3:
						jc.tool = 3;
						textReset(textBox, cb, bold, italic, subFont, addFont, fontSizeTxt);
						break;
					case 4:
						jc.tool = 4;
						textReset(textBox, cb, bold, italic, subFont, addFont, fontSizeTxt);
						break;
					case 5:
						jc.tool = 5;
						textBox.setVisible(true);
						cb.setVisible(true);
						bold.setVisible(true);
						italic.setVisible(true);
						subFont.setVisible(true);
						addFont.setVisible(true);
						fontSizeTxt.setVisible(true);
						break;
					default:
						System.out.println("Default");
						break;

					}
				}

			}
		});

		// Beginning of Menu Bar Section
		// Create Menu
		MenuBar menuBar = new MenuBar();

		// --- File Menu Option
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuInsert = new Menu("Insert");

		// Adds the menu options to the menu bar
		// just , (menuoption) in the addAll to add
		menuBar.getMenus().addAll(menuFile, menuEdit, menuInsert);

		// Saves the canvas over the previously selected file location
		// If non was selected has you choose a location and name
		MenuItem save = new MenuItem("Save");
		save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

		save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {

				if(file == null){
					file = getSaveLocation(stage);
				}

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

		// Saves the canvas as a new file
		// Opens the dialog for you to choose location and name
		MenuItem saveAs = new MenuItem("Save As");

		saveAs.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {

				file = getSaveLocation(stage);

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
			public void handle(ActionEvent arg0) {
				Image image = openImage();
				if (image == null) {
					// Do Nothing
				} else {
					jc.imageDraw(gc, canvas, image);
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

		// Undo option 
		MenuItem undo = new MenuItem("Undo");
		undo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		undo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				jc.undo(gc, canvas);
			}
		});

		// Redo option
		MenuItem redo = new MenuItem("Redo");
		redo.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		redo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				jc.redo(gc, canvas);
			}
		});

		MenuItem image = new MenuItem("image");
		image.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
		image.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				Image image = openImage();
				if (image == null) {
					// Do Nothing
				} else {
					jc.tool = 6;
					jc.setImage(image);
				}
			}
		});

		// Add menu options to the drop downs of the menu bar
		menuFile.getItems().addAll(clear, open, save, saveAs);
		menuEdit.getItems().addAll(undo, redo);
		menuInsert.getItems().addAll(image);



		// Declare the color picker
		// final ColorPicker colorPicker = new ColorPicker();
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

		ToggleButton drawLineBtn = new ToggleButton(null, createIcon("/icons/brush.png"));
		drawLineBtn.setPadding(Insets.EMPTY);
		drawLineBtn.setToggleGroup(tools);
		drawLineBtn.setUserData(0);
		drawLineBtn.setSelected(true);

		ToggleButton rectButton = new ToggleButton(null, createIcon("/icons/square.png"));
		rectButton.setPadding(Insets.EMPTY);
		rectButton.setToggleGroup(tools);
		rectButton.setUserData(1);

		ToggleButton circleBtn = new ToggleButton(null, createIcon("/icons/circle.png"));
		circleBtn.setPadding(Insets.EMPTY);
		circleBtn.setToggleGroup(tools);
		circleBtn.setUserData(2);

		ToggleButton eraserBtn = new ToggleButton(null, createIcon("/icons/eraser.png"));
		eraserBtn.setPadding(Insets.EMPTY);
		eraserBtn.setToggleGroup(tools);
		eraserBtn.setUserData(3);

		ToggleButton textBtn = new ToggleButton(null, createIcon("/icons/text.png"));
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
		tray.getChildren().add(subSize);
		tray.getChildren().add(toolSizeTxt);
		tray.getChildren().add(addSize);
		tray.getChildren().add(textBox);
		tray.getChildren().add(cb);
		tray.getChildren().add(subFont);
		tray.getChildren().add(fontSizeTxt);
		tray.getChildren().add(addFont);

		VBox vbox = new VBox();

		vbox.getChildren().add(menuBar);
		vbox.getChildren().add(toolLabel);
		vbox.getChildren().add(toolBox);
		vbox.getChildren().add(sp);
		vbox.getChildren().add(tray);
		vbox.setPadding(new Insets(0, 10, 0, 10));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * Clears the text in the field and makes it invisible
	 * 
	 * @param textBox:
	 *            the textbox to be reset
	 */
	public void textReset(TextField textBox, ChoiceBox cb, Button bold, Button italic, Button add, Button sub, TextField fontSize) {
		textBox.clear();
		textBox.setVisible(false);
		cb.setVisible(false);
		bold.setVisible(false);
		italic.setVisible(false);
		add.setVisible(false);
		sub.setVisible(false);
		fontSize.setVisible(false);
	}

	/**
	 * Creates a scaled image for use as an icon
	 * 
	 * @param location:
	 *            the file location of the image to be scaled
	 * @return a scaled image for use as an icon
	 */
	public ImageView createIcon(String location) {
		Image image = new Image(getClass().getResourceAsStream(location));
		ImageView scaledImage = new ImageView(image);
		scaledImage.setFitHeight(fitH);
		scaledImage.setFitWidth(fitW);
		return scaledImage;
	}

	/**
	 * Opens an image from file to be used
	 * @return the open image if one is open, null if one is not
	 */
	public Image openImage() {

		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter imageExtensions = new FileChooser.ExtensionFilter("all images", "*.jpg", "*.png",
				"*.JPG", "*.PNG", "*.jpeg", ".JPEG");

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
				return image;
			} catch (IOException ex) {
				Logger.getLogger(JFXPaint.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return null;
	}

	public File getSaveLocation(Stage stage){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg",
				"*.JPG", "*.jpeg", ".JPEG");

		FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png",
				"*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterjpg, extFilterpng);

		return fileChooser.showSaveDialog(stage);
	}
}

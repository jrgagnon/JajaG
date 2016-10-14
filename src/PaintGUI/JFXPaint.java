package PaintGUI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Used to intialize JFX components currently for testing purposes
 * 
 * @author Jordan Gagnon
 */
public class JFXPaint extends Application {

	private static final Label toolLabel = new Label("Tools:");

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage) {

		// Imports the JComponent Draw Area into the JFX Window
		SwingNode swingNode = new SwingNode();
		JFXDrawArea drawArea = new JFXDrawArea();
		swingNode.setContent(drawArea);

		Scene scene = new Scene(new Group());
		stage.setTitle("JFXPaint");
		stage.setWidth(800);
		stage.setHeight(600);

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
						drawArea.drawLineTool();
						break;
					case 1:
						drawArea.rectTool();
						break;
					case 2:
						drawArea.circleTool();
						break;
					case 3:
						drawArea.eraserTool();
						break;
					default:
						System.out.println("Default");
						break;

					}
				}

			}
		});

		// Declare the color picker
		final ColorPicker colorPicker = new ColorPicker();
		
		//Event that calls the change color method when the color pickers color is changed
		colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                drawArea.changeColor(colorPicker.getValue());               
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

		HBox hbox = new HBox();

		hbox.getChildren().add(drawLineBtn);
		hbox.getChildren().add(rectButton);
		hbox.getChildren().add(circleBtn);
		hbox.getChildren().add(eraserBtn);

		VBox vbox = new VBox();

		vbox.getChildren().add(toolLabel);
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(swingNode);
		vbox.getChildren().add(colorPicker);
		vbox.setPadding(new Insets(20, 10, 10, 20));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

}

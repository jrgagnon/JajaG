package PaintGUI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Used to intialize JFX components currently for testing purposes
 * 
 * @author Jordan Gagnon
 */
public class JFXPaint extends Application {

	ImageView image = new ImageView();
	Rectangle rect = new Rectangle(700, 400);
	private static final Label label = new Label("Tools:");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("JFXPaint");
		stage.setWidth(800);
		stage.setHeight(600);

		rect.setFill(Color.WHITE);
		rect.setStroke(Color.DARKGRAY);
		rect.setStrokeWidth(2);

		final ToggleGroup group = new ToggleGroup();

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null)
					rect.setFill(Color.WHITE);
				else
					rect.setFill((Color) group.getSelectedToggle().getUserData());
			}
		});

		ToggleButton drawLineBtn = new ToggleButton("Line");
		drawLineBtn.setToggleGroup(group);
		drawLineBtn.setUserData(Color.LIGHTGREEN);
		drawLineBtn.setSelected(true);
		drawLineBtn.setStyle("-fx-base: lightgreen;");

		ToggleButton rectButton = new ToggleButton("Rect");
		rectButton.setToggleGroup(group);
		rectButton.setUserData(Color.LIGHTBLUE);
		rectButton.setStyle("-fx-base: lightblue;");

		ToggleButton circleBtn = new ToggleButton("Circle");
		circleBtn.setToggleGroup(group);
		circleBtn.setUserData(Color.PLUM);
		circleBtn.setStyle("-fx-base: plum;");
		
		ToggleButton eraserBtn = new ToggleButton("Eraser");
		eraserBtn.setToggleGroup(group);
		eraserBtn.setUserData(Color.SALMON);
		eraserBtn.setStyle("-fx-base: salmon;");

		HBox hbox = new HBox();

		hbox.getChildren().add(drawLineBtn);
		hbox.getChildren().add(rectButton);
		hbox.getChildren().add(circleBtn);
		hbox.getChildren().add(eraserBtn);

		rect.setArcHeight(10);
		rect.setArcWidth(10);

		VBox vbox = new VBox();

		vbox.getChildren().add(label);
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(rect);
		vbox.setPadding(new Insets(20, 10, 10, 20));

		((Group) scene.getRoot()).getChildren().add(vbox);
		stage.setScene(scene);
		stage.show();
	}

}

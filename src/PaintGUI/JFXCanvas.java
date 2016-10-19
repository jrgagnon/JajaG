package PaintGUI;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class JFXCanvas {

	protected double oldX = 0.0;
	protected double oldY = 0.0;
	protected double curX = 0.0;
	protected double curY = 0.0;
	protected double endX = 0.0;
	protected double endY = 0.0;

	protected int pressed = 0;
	protected boolean eraser = false;

	public int tool = 0;
	public int lineSize = 0;

	public void draw(GraphicsContext gc, Canvas canvas) {

		// Add Mouse Click Event
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			// What to do on mouse click event
			@Override
			public void handle(MouseEvent event) {
				oldX = event.getX();
				oldY = event.getY();
				pressed = 1;
				tool(gc, canvas);

			}
		});

		// Add Mouse drag event
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

			// What to do on mouse drag
			@Override
			public void handle(MouseEvent event) {
				curX = event.getX();
				curY = event.getY();
				pressed = 2;
				tool(gc, canvas);
			}
		});

		// Add Mouse Release Event
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			// What to do on mouse release
			@Override
			public void handle(MouseEvent event) {
				endX = event.getX();
				endY = event.getY();
				pressed = 0;
				tool(gc, canvas);

			}
		});

		// Clear the Canvas when the user double-clicks
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if (t.getClickCount() > 1) {
					clear(gc, canvas);
				}
			}
		});
	}

	public void border(GraphicsContext gc, Canvas canvas) {
		// Set a draw border
		gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void clear(GraphicsContext gc, Canvas canvas) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	// Changes the tool color when the color is
	// Changed by the color picker
	public void changeColor(Color value, GraphicsContext gc) {
		gc.setStroke(value);
	}

	// Method that is called from the Event Handlers
	// determines what tool to execute
	public void tool(GraphicsContext gc, Canvas canvas) {
		switch (tool) {
		case 0:
			drawLine(gc, canvas);
			break;
		case 1:
			drawRect(gc, canvas);
			break;
		case 2:
			drawCircle(gc, canvas);
			break;
		case 3:
			eraser = true;
			erase(gc, canvas);
			break;
		default:
			System.out.println("Default");
			break;

		}
	}

	// Line Tool Called when tool == 0
	public void drawLine(GraphicsContext gc, Canvas canvas) {
		// Set tool cursor
		canvas.setCursor(Cursor.CROSSHAIR);

		// Start the path from the new mouse location on click
		if (pressed == 1) {
			gc.beginPath();
			gc.moveTo(oldX, oldY);
			gc.stroke();

			// Draw a path if the mouse is dragged
		} else if (pressed == 2) {
			gc.lineTo(curX, curY);
			gc.stroke();
		}

	}

	// Rectangle Tool Called when tool == 1
	public void drawRect(GraphicsContext gc, Canvas canvas) {
		// Set tool cursor
		canvas.setCursor(Cursor.CROSSHAIR);

		if (pressed == 0) {
			// find lowest x and y, then gain shape size
			double length;
			double width;
			double tempX;
			double tempY;

			// Determine Length
			if (oldX < endX) {
				tempX = oldX;
				length = endX - oldX;
			} else {
				tempX = endX;
				length = oldX - endX;
			}

			// Determine Width
			if (oldY < endY) {
				tempY = oldY;
				width = endY - oldY;
			} else {
				tempY = endY;
				width = oldY - endY;
			}

			gc.strokeRect(tempX, tempY, length, width);
		}

	}

	// Circle Tool Called when tool == 1
	public void drawCircle(GraphicsContext gc, Canvas canvas) {
		// Set tool cursor
		canvas.setCursor(Cursor.CROSSHAIR);

		if (pressed == 0) {
			// find lowest x and y, then gain shape size
			double length;
			double width;
			double tempX;
			double tempY;

			// Determine Length
			if (oldX < endX) {
				tempX = oldX;
				length = endX - oldX;
			} else {
				tempX = endX;
				length = oldX - endX;
			}

			// Determine Width
			if (oldY < endY) {
				tempY = oldY;
				width = endY - oldY;
			} else {
				tempY = endY;
				width = oldY - endY;
			}

			gc.strokeOval(tempX, tempY, length, width);
		}

	}

	// Eraser Tool Called when tool == 1
	public void erase(GraphicsContext gc, Canvas canvas) {

		gc.setStroke(Color.WHITE);

		// Adjust cursor based on line width
		if (gc.getLineWidth() == 1.0)
            canvas.setCursor(Cursor.CROSSHAIR);
		else if (gc.getLineWidth() == 25.0)
            canvas.setCursor(Cursor.CROSSHAIR);
		else if (gc.getLineWidth() == 50.0)
			canvas.setCursor(Cursor.CROSSHAIR);
		else if (gc.getLineWidth() == 100.0)
			canvas.setCursor(Cursor.CROSSHAIR);

		// Start the path from the new mouse location on click
		if (pressed == 1) {
			gc.beginPath();
			gc.moveTo(oldX, oldY);
			gc.stroke();

			// Erase a path if the mouse is dragged
		} else if (pressed == 2) {
			gc.lineTo(curX, curY);
			gc.stroke();
		} else if (pressed == 0 && eraser) {
			gc.setStroke(Color.BLACK);
		}

	}

	public void changeLineSize(GraphicsContext gc) {

		switch (lineSize) {
		case 0:
			gc.setLineWidth(1.0);
			break;
		case 1:
			gc.setLineWidth(25.0);
			break;
		case 2:
			gc.setLineWidth(50.0);
			break;
		case 3:
			gc.setLineWidth(100.0);
			break;
		default:
			System.out.println("Default");
			break;

		}

	}

}

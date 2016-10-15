package PaintGUI;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class JFXCanvas {

	public void drawLine(GraphicsContext gc, Canvas canvas) {
		
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gc.beginPath();
				gc.moveTo(event.getX(), event.getY());
				gc.stroke();
			}
		});

		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gc.lineTo(event.getX(), event.getY());
				gc.stroke();
			}
		});

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

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

	public void clear(GraphicsContext gc, Canvas canvas) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void drawRect(GraphicsContext gc, Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	public void changeColor(Color value, GraphicsContext gc) {
		gc.setStroke(value);		
	}

	public void drawCircle(GraphicsContext gc, Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	public void erase(GraphicsContext gc, Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	
	

}

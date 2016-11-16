package PaintGUI;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.javafx.geom.Point2D;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class JFXCanvas {

	protected double oldX = 0.0;
	protected double oldY = 0.0;
	protected double curX = 0.0;
	protected double curY = 0.0;
	protected double endX = 0.0;
	protected double endY = 0.0;

	Paint p = Color.BLACK;
	protected int pressed = 0;
	protected boolean eraser = false;

	public int tool = 0;
	public int lineSize = 0;
	
	Stack stack = new Stack(); // contains images
	
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

				/* Add buffered image to stack for undo functionality */
				System.out.println("Mouse released"); //TODO
				
				WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
				canvas.snapshot(null, writableImage);
			
				Image image = writableImage; // WritableImage extends Image, but probly not sufficient
				stack.push(image);
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
			if(!eraser){
				saveColor(gc);
			}
			eraser = true;
			erase(gc, canvas);
			break;
		case 4:
			fill(gc, canvas);
			break;
		default:
			System.out.println("Default");
			break;

		}
	}

	// Line Tool Called when tool == 0
	public void drawLine(GraphicsContext gc, Canvas canvas) {

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
			//gc.setStroke(Color.BLACK);
			gc.setStroke(p);
		}

	}
	
	public void fill(GraphicsContext gc, Canvas canvas){
		
		if(pressed == 1){
			try{
				float xValue = (float) oldX;
				float yValue = (float) oldY;
				Point2D start = new Point2D(xValue, yValue);			
				WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
				canvas.snapshot(null, writableImage);		
				Image image = writableImage;
				PixelReader pr = image.getPixelReader();
				Paint newColor = p;
				Paint oldColor = pr.getColor((int)oldX, (int)oldY);
				changeToolSize(gc, 1.00);
				boolean[][] mark = new boolean[(int)canvas.getWidth()][(int)canvas.getHeight()];
				
				floodFill(gc, canvas, mark, start, oldColor, newColor, pr);
				
//				Point2D up = new Point2D(xValue, yValue+1);
//				//fillUp(gc, up, oldColor, pr);
//				Point2D down = new Point2D(xValue, yValue-1);
//				fillDown(gc, up, oldColor, pr);
//				Point2D left = new Point2D(xValue-1, yValue);
//				fillLeft(gc, up, oldColor, pr);
//				Point2D right = new Point2D(xValue+1, yValue);
//				fillRight(gc, up, oldColor, pr);
				
				//fillRecursion(gc, canvas, start, start, oldColor, writableImage);
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
				System.out.println("hi");
			}
//			gc.strokeLine(start.x, start.y, start.x, start.y);
//			Point2D up = new Point2D(xValue, yValue+1);
//			Point2D down = new Point2D(xValue, yValue-1);
//			Point2D left = new Point2D(xValue-1, yValue);
//			Point2D right = new Point2D(xValue+1, yValue);
//			fillRecursion(gc, up, oldColor, pr);
//			fillRecursion(gc, down, oldColor, pr);
//			fillRecursion(gc, right, oldColor, pr);
//			fillRecursion(gc, left, oldColor, pr);
		}
		
	}
	
	public void floodFill(GraphicsContext gc, Canvas canvas, boolean [][] mark, Point2D point, Paint old, Paint target, PixelReader pr){
		
		// create queue of points that will store all the points to be filled
        Queue<Point2D> queue = new LinkedList<Point2D>();
        queue.add(point);
        
        while(!queue.isEmpty()){
        	
        	Point2D p = queue.remove();
        	//check point pos
        	if (p.x > 0 && p.x < canvas.getWidth() - 1 && p.y > 0 && p.y < canvas.getHeight() -1){
        		//paint point
        		if(mark[(int) p.x][(int) p.y]){continue;}
        		mark[(int) p.x][(int) p.y] = true;
        		gc.strokeLine(p.x, p.y, p.x, p.y);
        		
        		// test left
                if (pr.getColor((int)p.x - 1, (int)p.y).equals(old))
                {
                	if(!mark[(int) p.x-1][(int) p.y]){
                		queue.add(new Point2D(p.x - 1, p.y));
                    }
                }
                // test right
                if (pr.getColor((int)p.x + 1, (int)p.y).equals(old))
                {
                	if(!mark[(int) p.x+1][(int) p.y]){
                		queue.add(new Point2D(p.x + 1, p.y));
                	}
                }
                // test up
                if (pr.getColor((int)p.x, (int)p.y - 1).equals(old))
                {
                	if(!mark[(int) p.x][(int) p.y-1]){
                		queue.add(new Point2D(p.x, p.y - 1));
                	}
                }
                // test down
                if (pr.getColor((int)p.x, (int)p.y + 1).equals(old))
                {
                	if(!mark[(int) p.x][(int) p.y+1]){
                		queue.add(new Point2D(p.x, p.y + 1));
                	}
                }
        	}
        	
        }
        
//		//tests
//		if(point.x < 0){return;}
//		if(point.y < 0){return;}
//		if(point.x > gc.getCanvas().getWidth()){return;}
//		if(point.y > gc.getCanvas().getHeight()){return;}
//		if(mark[(int) point.x][(int) point.y]){return;}
//		Paint pointColor = pr.getColor((int)point.x, (int)point.y);
//		if(!pointColor.equals(old)){return;}
//		
//		System.out.println("I ran. Paint = " + pointColor + " and " + old + " Point = (" + point.x + ", " + point.y +")");
//		
//		//paint point and mark point as painted (true)
//		gc.strokeLine(point.x, point.y, point.x, point.y);		
//		mark[(int) point.x][(int) point.y] = true;
//		//sleep(25);
//		
//		//recursively check surrounding points
//		Point2D up = new Point2D(point.x, point.y-1);
//		Point2D down = new Point2D(point.x, point.y+1);
//		Point2D left = new Point2D(point.x-1, point.y);
//		Point2D right = new Point2D(point.x+1, point.y);
//		floodFill(gc, mark, up, old, target, pr);
//		floodFill(gc, mark, down, old, target, pr);
//		floodFill(gc, mark, left, old, target, pr);
//		floodFill(gc, mark, right, old, target, pr);
	}
	
	private static void sleep(int msec) {
        try {
            Thread.currentThread().sleep(msec);
        } catch (InterruptedException e) { }
    }
	
	public void fillDown(GraphicsContext gc, Point2D point, Paint old, PixelReader pr){
		
	}
	
	public void fillLeft(GraphicsContext gc, Point2D point, Paint old, PixelReader pr){
		
	}
	
	public void fillRight(GraphicsContext gc, Point2D point, Paint old, PixelReader pr){
		
	}
	
	
	
	//keep track of old point so you can draw a line from new to old
	public void fillRecursion(GraphicsContext gc, Canvas canvas, Point2D prevPoint, Point2D point, Paint old, WritableImage writableImage){
		
		canvas.snapshot(null, writableImage);		
		Image image = writableImage;
		PixelReader pr = image.getPixelReader();
		Paint pointColor = pr.getColor((int)point.x, (int)point.y);
		if(pointColor.equals(old)){
			System.out.println("I ran. Paint = " + pointColor + " and " + old + " Point = (" + point.x + ", " + point.y +")");
			try{
				//paint point and check surrounding points
				//paint
				gc.strokeLine(prevPoint.x, prevPoint.y, point.x, point.y);				
				//check other points
				Point2D up = new Point2D(point.x, point.y-1);
				Point2D down = new Point2D(point.x, point.y+1);
				Point2D left = new Point2D(point.x-1, point.y);
				Point2D right = new Point2D(point.x+1, point.y);
				fillRecursion(gc, canvas, point, up, old, writableImage);
				fillRecursion(gc, canvas, point, down, old, writableImage);
				fillRecursion(gc, canvas, point, right, old, writableImage);
				fillRecursion(gc, canvas, point, left, old, writableImage);
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
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

	public void changeToolSize(GraphicsContext gc, double size) {

		gc.setLineWidth(size);

	}

	public void imageDraw(GraphicsContext gc, Canvas canvas, Image image) {
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();

		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();

		if ((imageWidth > canvasWidth) || (imageHeight > canvasHeight)) {
			gc.drawImage(image, 0, 0, canvasWidth, canvasHeight);
		} else {
			gc.drawImage(image, 0, 0, imageWidth, imageHeight);
		}

	}

	public void saveColor(GraphicsContext gc) {
		p = gc.getStroke();
	}
	
	public void undo(GraphicsContext gc, Canvas canvas){
		System.out.println("Undo");
		Image im = stack.pop();
		if(im != null){
			imageDraw(gc, canvas, im);
		}
			
	}

}

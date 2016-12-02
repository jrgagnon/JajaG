package PaintGUI;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.javafx.geom.Point2D;

import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class JFXCanvas {

	protected double oldX = 0.0;
	protected double oldY = 0.0;
	protected double curX = 0.0;
	protected double curY = 0.0;
	protected double endX = 0.0;
	protected double endY = 0.0;

	Paint p = Color.BLACK;
	protected int pressed = 0;
	protected int firstUndo = 0;
	protected int clearCanvas = 0;
	protected boolean eraser = false;

	public int tool = 0;
	public int lineSize = 0;
	String textString = "";
	Image image = null;
	String font = "";
	boolean bold = false;
	boolean italic = false;
	double fontSize = 12.0;

	Stack undoStack = new Stack(); // contains images
	Stack redoStack = new Stack();

	public void draw(GraphicsContext gc, Canvas canvas) {

		// Add Mouse Click Event
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			// What to do on mouse click event
			@Override
			public void handle(MouseEvent event) {
				
				clearCanvas = 1;
				
				oldX = event.getX();
				oldY = event.getY();
				pressed = 1;
				tool(gc, canvas);

				/* Blank canvas needs added to undo stack */
				if (undoStack.size == 0) {
					WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
					canvas.snapshot(null, writableImage);

					Image image = writableImage;
					undoStack.push(image);
				}
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
				WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
				canvas.snapshot(null, writableImage);

				Image image = writableImage; // WritableImage extends Image, but
												// probly not sufficient
				undoStack.push(image);
				if(firstUndo == 1){
					firstUndo = 0;
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
			if (!eraser) {
				saveColor(gc);
			}
			eraser = true;
			erase(gc, canvas);
			break;
		case 4:
			fill(gc, canvas);
			break;
		case 5:
			textDraw(gc);
			break;
		case 6:
			imageInsert(gc, canvas);
			break;
		case 7:
			cropImage(gc, canvas);
			break;
		case 8:
			selectImage(gc, canvas);
			break;
		case 9:
			drawStraightLine(gc, canvas);
			break;
		default:
			System.out.println("Default");
			break;

		}
	}

	private void cropImage(GraphicsContext gc, Canvas canvas) {
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor
		if (pressed == 0) {
			// find lowest x and y, then gain shape size
			double height;
			double width;

			// Determine Length
			if (oldX < endX) {
				width = endX - oldX;
			} else {
				width = oldX - endX;
			}

			// Determine Width
			if (oldY < endY) {
				height = endY - oldY;
			} else {
				height = oldY - endY;
			}

			WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
			canvas.snapshot(null, image);

			clear(gc, canvas);
			gc.drawImage(image, oldX, oldY, width, height, 0, 0,image.getWidth(), image.getHeight()); //Crop Tool
		}

	}

	public void selectImage(GraphicsContext gc, Canvas canvas) {
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor
		if (pressed == 0) {
			// find lowest x and y, then gain shape size
			double height;
			double width;

			// Determine Length
			if (oldX < endX) {
				width = endX - oldX;
			} else {
				width = oldX - endX;
			}

			// Determine Width
			if (oldY < endY) {
				height = endY - oldY;
			} else {
				height = oldY - endY;
			}

			WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
			canvas.snapshot(null, image);

			clear(gc, canvas);
			gc.drawImage(image, oldX, oldY, width, height, oldX, oldY, width, height); // Select
																						// Tool
		}
	}

	// Line Tool Called when tool == 0
	public void drawLine(GraphicsContext gc, Canvas canvas) {
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor

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
	
	public void drawStraightLine(GraphicsContext gc, Canvas canvas){
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor
		
		//draw initial dot when clicked so user knows where line will start
		if(pressed == 1){
			gc.strokeLine(oldX, oldY, oldX, oldY);
		}
		//draw line from old x,y to end x,y
		else if(pressed == 0){
			gc.strokeLine(oldX, oldY, endX, endY);
		}
	}

	// Rectangle Tool Called when tool == 1
	public void drawRect(GraphicsContext gc, Canvas canvas) {
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor

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
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor
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
			Image eraser = new Image("/CustomCursors/EraserCursor5.png", lineSize, lineSize,
					true, false);
			canvas.setCursor(new ImageCursor(eraser));			
			gc.beginPath();
			gc.moveTo(oldX, oldY);
			gc.stroke();

			// Erase a path if the mouse is dragged
		} else if (pressed == 2) {
			gc.lineTo(curX, curY);
			gc.stroke();
		} else if (pressed == 0 && eraser) {
			gc.setStroke(p);
		}

	}

	public void fill(GraphicsContext gc, Canvas canvas){
		canvas.setCursor(Cursor.CROSSHAIR); // Default cursor
		if(pressed == 1){
			try{
				float xValue = (float) oldX;
				float yValue = (float) oldY;
				Point2D start = new Point2D((int)xValue, (int)yValue);			
				WritableImage writableImage = new WritableImage((int) canvas.getWidth()+1, (int) canvas.getHeight()+1);
				canvas.snapshot(null, writableImage);		
				Image image = writableImage;
				PixelReader pr = image.getPixelReader();
				Paint newColor = p;
				Paint oldColor = pr.getColor((int)oldX, (int)oldY);
				changeToolSize(gc, 1.00);
				boolean[][] mark = new boolean[(int)canvas.getWidth()+1][(int)canvas.getHeight()+1];
				
				floodFill(gc, canvas, mark, start, oldColor, newColor, pr);
				
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
				System.out.println("hi");
			}
//			
		}
		
	}
	
	public void floodFill(GraphicsContext gc, Canvas canvas, boolean [][] mark, Point2D point, Paint old, Paint target, PixelReader pr){
		
		// create queue of points that will store all the points to be filled
        Queue<Point2D> queue = new LinkedList<Point2D>();
        queue.add(point);
        
        while(!queue.isEmpty()){
        	
        	Point2D p = queue.remove();
        	//check point pos
        	if (p.x > 0 && p.x < canvas.getWidth() - 1 && p.y > 0 && p.y < canvas.getHeight() - 1){
        		//paint point
        		if(mark[(int) p.x][(int) p.y]){continue;}
        		mark[(int) p.x][(int) p.y] = true;
        		gc.strokeLine(p.x, p.y, p.x+1, p.y+1);
        		
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

	public void imageInsert(GraphicsContext gc, Canvas canvas) {
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

			gc.drawImage(image, tempX, tempY, length, width);
			tool = 0;
		}

	}

	public void setImage(Image i) {
		image = i;
	}

	public void textDraw(GraphicsContext gc) {
		if (pressed == 1 && textString != null) {
			gc.strokeText(textString, oldX, oldY);
		}

	}

	public void saveColor(GraphicsContext gc) {
		p = gc.getStroke();
	}

	public void undo(GraphicsContext gc, Canvas canvas) {
		Image im = null;
		
		//if this is the first undo pop twice
		if(firstUndo == 0){
			im = undoStack.pop();
			redoStack.push(im);
			if (im != null) {
				im = undoStack.pop();
			}
			firstUndo = 1;
		} else {
			im = undoStack.pop();
		}
		
		if (im != null) {
			redoStack.push(im);
			imageDraw(gc, canvas, im);
		}

	}

	public void redo(GraphicsContext gc, Canvas canvas) {
		Image im = redoStack.pop();
		if (im != null) {
			imageDraw(gc, canvas, im);
		}

	}

	public void setText(String text) {
		textString = text;
	}

	public void setFont(String f) {
		font = f;
	}

	public void changeFont(GraphicsContext gc) {
		// FontSelect fc = new FontSelect();
		// String fontFormated = fc.select(font, bold, italic);
		gc.setFont(Font.font(font, fontSize));
	}

}
	

	
	
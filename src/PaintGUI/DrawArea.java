package PaintGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Component used for drawing.
 * 
 * @author Andrew Fox
 *
 */
public class DrawArea extends JComponent {

	// image were going to draw
	private BufferedImage image;
	// used to draw on
	private Graphics2D g2;
	// Mouse coordinates
	private int currentX, currentY, oldX, oldY, shapeX, shapeY;

	/**
	 * Cases for the tool variable 0: Draw Line 1: Rectangle 2: Circle 3: Eraser
	 */
	public int tool = 0;

	public DrawArea() {
		//set up variables
		shapeX = -1;
		shapeY = -1;
		draw();		
	}
	
	protected void draw(){
		
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) 
			{
				// save x,y when mouse pressed
				oldX = e.getX();
				oldY = e.getY();
			}
			
			public void mouseReleased(MouseEvent m)
	          {
				shapeX = m.getX();
				shapeY = m.getY();
	            repaint();	            
	          }

		});

		addMouseMotionListener(new MouseMotionAdapter() 
		{
			public void mouseDragged(MouseEvent e) {
				// coord x,y when mouse is dragged
				currentX = e.getX();
				currentY = e.getY();
				repaint();				
			}

		});
		
	}

	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g2);
		
		if (image == null) {
			// image to draw null => create
			image = (BufferedImage) createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			// enable
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// clear draw area
			clear();
		}

		g.drawImage(image, 0, 0, null);
		
		//System.out.println(tool);

		if (tool == 0) { // Draw Line
			
			if (g2 != null) {
				// draw line
				g2.drawLine(oldX, oldY, currentX, currentY);
				// refresh draw area to paint
				// store current coords x,y
				oldX = currentX;
				oldY = currentY;
				shapeX = -1;
	    		shapeY = -1;
			}
			
		} else if (tool == 1) {// Draw Rect
			
			if(g2 != null && shapeX != -1 && shapeY != -1)
			{
				//find lowest x and y, then gain shape size
				int tempX;
				int tempY;
				int lengthX;
				int lengthY;
				if(oldX < shapeX){
					tempX = oldX;
					lengthX = shapeX - oldX;
				}else{
					tempX = shapeX;
					lengthX = oldX - shapeX;
				}
				if(oldY < shapeY){
					tempY = oldY;
					lengthY = shapeY - oldY;
				}else{
					tempY = shapeY;
					lengthY = oldY - shapeY;
				}
				// draw rect
				g2.drawRect(tempX, tempY, lengthX, lengthY);
				System.out.println(tempX + ", " + tempY + ", " + lengthX + ", " + lengthY);
				// refresh draw area to paint
				// store current coords x,y
//				oldX = shapeX;
//				oldY = shapeY;
				shapeX = -1;
	    		shapeY = -1;
	    		repaint();
			}

		} else if (tool == 2) {// Draw Circle
			if(g2 != null && shapeX != -1 && shapeY != -1)
			{
				//find lowest x and y, then gain shape size
				int tempX;
				int tempY;
				int lengthX;
				int lengthY;
				if(oldX < shapeX){
					tempX = oldX;
					lengthX = shapeX - oldX;
				}else{
					tempX = shapeX;
					lengthX = oldX - shapeX;
				}
				if(oldY < shapeY){
					tempY = oldY;
					lengthY = shapeY - oldY;
				}else{
					tempY = shapeY;
					lengthY = oldY - shapeY;
				}
				// draw rect
				g2.drawOval(tempX, tempY, lengthX, lengthY);
				System.out.println(tempX + ", " + tempY + ", " + lengthX + ", " + lengthY);
				// refresh draw area to paint
				// store current coords x,y
//				oldX = shapeX;
//				oldY = shapeY;
				shapeX = -1;
	    		shapeY = -1;
	    		repaint();
			}

		} else if(tool == 3){//Erase
			
			if(g2 != null)
			{
				
			}
			
		}
	}

	// now we create exposed methods

	public void clear() {
		g2.setPaint(Color.white);
		// draw white on entire area
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		repaint();
	}

	public void red() {
		// apply red color on g2 context
		g2.setPaint(Color.red);
	}

	public void black() {
		g2.setPaint(Color.black);
	}

	public void magenta() {
		g2.setPaint(Color.magenta);
	}

	public void green() {
		g2.setPaint(Color.green);
	}

	public void blue() {
		g2.setPaint(Color.blue);
	}

	public void yellow() {
		g2.setPaint(Color.yellow);
	}

	public void grey() {
		g2.setPaint(Color.gray);
	}

	public void orange() {
		g2.setPaint(Color.orange);
	}
	
	public void setColor(Color c){
		g2.setPaint(c);
	}
	
	public void saveImage(){
		try{
			//retrieve image
			BufferedImage bi = image;
			File outputFile = new File("saved.png");
			outputFile.getAbsolutePath();
			ImageIO.write(bi, "png", outputFile);
			System.out.println("Success: " + outputFile.getAbsolutePath());
		}
		catch(Exception e){
			System.out.println("Error");
		}
	}
}
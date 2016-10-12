package PaintGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Component used for drawing.
 * 
 * @author Andrew Fox
 *
 */
@SuppressWarnings("serial")
public class DrawArea extends JComponent {

	// image were going to draw
	private BufferedImage image;
	// used to draw on
	private Graphics2D g2;
	// Mouse coordinates
	private int currentX, currentY, oldX, oldY;

	/**
	 * Cases for the tool variable 0: Draw Line 1: Rectangle 2: Circle 3: Eraser
	 */
	public int tool = 0;

	public DrawArea() {
		draw();		
	}
	
	protected void draw(){
		System.out.println(tool);

		if (tool == 0) { // Draw Line
			setDoubleBuffered(false);
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					// save x,y when mouse pressed
					oldX = e.getX();
					oldY = e.getY();
				}

			});

			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					// coord x,y when mouse is dragged
					currentX = e.getX();
					currentY = e.getY();

					if (g2 != null) {
						// draw line
						g2.drawLine(oldX, oldY, currentX, currentY);
						// refresh draw area to paint
						repaint();
						// store current coords x,y
						oldX = currentX;
						oldY = currentY;
					}
				}

			});
		} else if (tool == 1) {// Draw Rect

		} else if (tool == 2) {// Draw Circle

		} else if(tool == 3){//Erase
			
		}
	}

	protected void paintComponent(Graphics g) {
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
}

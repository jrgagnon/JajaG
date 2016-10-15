package PaintGUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class JFXDrawArea extends JComponent {

	javafx.scene.paint.Color c = javafx.scene.paint.Color.rgb(0, 0, 0);
	
	//Pull the rgb values from the color
	double red = c.getRed();
	double green = c.getGreen();
	double blue = c.getBlue();
	
	int r = (int)(255*red);
	int gn = (int)(255*green);
	int b = (int)(255*blue);
	
	public JFXDrawArea() {
		setSize(700, 400);
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.setColor(new Color(r,gn,b));
		g.fillRect(0, 0, getWidth(), getHeight());		
	}
	
	//This method is called when the color pickers color is changed
	public void changeColor(javafx.scene.paint.Color color){
		c = color;
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		
		r = (int)(255*red);
		gn = (int)(255*green);
		b = (int)(255*blue);
		
		repaint();
		System.out.println("Color Changed");
		System.out.println("Red = " + red);
		System.out.println("Green = " + green);
		System.out.println("Blue = " + blue);
		
		System.out.println("Red = " + r);
		System.out.println("Green = " + gn);
		System.out.println("Blue = " + b);
		
		
	}

	public Object drawLineTool() {
		System.out.println("Line");
		return null;
	}

	public Object rectTool() {
		// TODO Auto-generated method stub
		System.out.println("Rectangle");
		return null;
	}

	public Object circleTool() {
		// TODO Auto-generated method stub
		System.out.println("Circle");
		return null;
	}

	public Object eraserTool() {
		// TODO Auto-generated method stub
		System.out.println("Eraser");
		return null;
	}
}

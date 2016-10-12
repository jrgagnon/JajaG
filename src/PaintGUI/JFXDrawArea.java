package PaintGUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class JFXDrawArea extends JComponent {

	public JFXDrawArea() {
		setSize(700, 400);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());		
	}

	public Object drawLineTool() {
		// TODO Auto-generated method stub
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

package PaintGUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingPaint
{

	JButton clearBtn, redBtn, blackBtn, magentaBtn, blueBtn, greenBtn, yellowBtn, grayBtn, orangeBtn;
	DrawArea drawArea;
	ActionListener actionListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == clearBtn)
			{
				drawArea.clear();
			}else if(e.getSource() == redBtn)
			{
				drawArea.red();
			}else if(e.getSource() == blackBtn)
			{
				drawArea.black();
			}else if(e.getSource() == magentaBtn)
			{
				drawArea.magenta();
			}else if(e.getSource() == blueBtn)
			{
				drawArea.blue();
			}else if(e.getSource() == greenBtn)
			{
				drawArea.green();
			}else if(e.getSource() == yellowBtn)
			{
				drawArea.yellow();
			}else if(e.getSource() == grayBtn)
			{
				drawArea.grey();
			}else if(e.getSource() == orangeBtn)
			{
				drawArea.orange();
			}
		}		
	};
	
	public static void main(String[] args)
	{
		new SwingPaint().show();
	}
	
	public void show()
	{
		//main frame
		JFrame frame = new JFrame("Swing Paint");
		Container content = frame.getContentPane();
		//set layout on content pane
		content.setLayout(new BorderLayout());
		//create draw area
		drawArea = new DrawArea();
		//add to content pane
		content.add(drawArea, BorderLayout.CENTER);
		//create controls to apply colors and call clear feature
		JPanel controls = new JPanel();
		JPanel colors = new JPanel();
		 
		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(actionListener);
		blackBtn = new JButton("Black");
		blackBtn.addActionListener(actionListener);
		redBtn = new JButton("Red");
		redBtn.addActionListener(actionListener);
		magentaBtn = new JButton("Magenta");
		magentaBtn.addActionListener(actionListener);
		blueBtn = new JButton("Blue");
		blueBtn.addActionListener(actionListener);
		greenBtn = new JButton("Green");
		greenBtn.addActionListener(actionListener);
		yellowBtn = new JButton("Yellow");
		yellowBtn.addActionListener(actionListener);
		grayBtn = new JButton("Gray");
		grayBtn.addActionListener(actionListener);
		orangeBtn = new JButton("Orange");
		orangeBtn.addActionListener(actionListener);
		
		//add controls to panel
		controls.add(clearBtn);
		
		//add colors to panel
		colors.add(blackBtn);
		colors.add(redBtn);
		colors.add(magentaBtn);
		colors.add(blueBtn);
		colors.add(greenBtn);
		colors.add(yellowBtn);
		colors.add(grayBtn);
		colors.add(orangeBtn);
		
		
		//add to content pane
		content.add(controls, BorderLayout.NORTH);
		content.add(colors, BorderLayout.SOUTH);
		
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		// can close frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// show the swing paint result
		frame.setVisible(true);
		
		
		
	}
	
}

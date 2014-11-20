import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parser.Parser;
import viewer.Viewer;
import expression.Node;

public class ThreeDGraphViewer extends JFrame{
	
	JTextField x = new JTextField("input x");
	JTextField y = new JTextField("input y");
	JTextField z = new JTextField("input z");
	Viewer viewer = new Viewer();
	public void init()
	{		
		JPanel texts = new JPanel();
		JPanel xPane = new JPanel();
		xPane.add(new JLabel("x : "));
		xPane.add(x);
		JPanel yPane = new JPanel();
		yPane.add(new JLabel("y : "));
		yPane.add(y);
		JPanel zPane = new JPanel();
		zPane.add(new JLabel("z : "));
		zPane.add(z);
		texts.add(xPane);
		texts.add(yPane);
		texts.add(zPane);
		JButton draw = new JButton("Draw");
		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expX = x.getText();
				String expY = y.getText();
				String expZ = z.getText();
				Node[] graph = new Node[3];
				Parser parser = new Parser();
				parser.setString(expX);
				graph[0] = parser.expression();
				parser.setString(expY);
				graph[1] = parser.expression();
				parser.setString(expZ);
				graph[2] = parser.expression();
				viewer.clear();
				viewer.addGraph(graph, 0, 10, 0.1, 1, Color.BLUE);
				repaint();
			}
		});
		
		add(viewer,BorderLayout.NORTH);
		add(texts,BorderLayout.EAST);
		add(draw,BorderLayout.SOUTH);
		/*
		Node[] x = new Node[3];
		Parser parser = new Parser("x:t");
		parser.parseVariable();
		parser.setString("x");
		x[0] = parser.expression();
		parser.setString("y:(x+5)");
		parser.parseVariable();
		parser.setString("y");
		x[1] = parser.expression();
		System.out.println(x[1]+" : "+x[1].getValue());
		parser.setString("p[a,0,0]");
		x[2] = parser.expression();
		parser.setString("p[a,0,0]:(-5,1)");
		parser.parseParameter();
		viewer.addGraph(x,-10,10,0.5,0.2,Color.BLUE);

		Node[] y = new Node[3];
		parser = new Parser("x:t");
		parser.parseVariable();
		parser.setString("x");
		y[0] = parser.expression();
		parser.setString("y:(x)");
		parser.parseVariable();
		parser.setString("y+2");
		y[1] = parser.expression();
		System.out.println(x[1]+" : "+x[1].getValue());
		parser.setString("p[a,0,0]");
		y[2] = parser.expression();
		parser.setString("p[a,0,0]:(-5,1)");
		parser.parseParameter();
		viewer.viewInterval(true);
		viewer.addGraph(y,-10,10,0.5,0.2,Color.RED);
		*/
		
		repaint();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		new ThreeDGraphViewer().init();
	}

}

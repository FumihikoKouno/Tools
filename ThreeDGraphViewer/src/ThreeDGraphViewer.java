import javax.swing.JFrame;

import parser.Parser;
import viewer.Viewer;
import expression.Node;

public class ThreeDGraphViewer extends JFrame{
	
	public void init()
	{
		Node[] x = new Node[3];
		Parser parser = new Parser("x:1000*t");
		parser.parseVariable();
		parser.setString("x");
		x[0] = parser.expression();
		parser.setString("y:(x^2)");
		parser.parseVariable();
		parser.setString("y/100");
		x[1] = parser.expression();
		System.out.println(x[1]+" : "+x[1].getValue());
		parser.setString("0");
		x[2] = parser.expression();
		Viewer viewer = new Viewer();
		//viewer.addGraph(x,-10,10,0.01);
		

		Node[] y = new Node[3];
		parser = new Parser("x:1000*t");
		parser.parseVariable();
		parser.setString("x");
		y[0] = parser.expression();
		parser.setString("y:(x^2)");
		parser.parseVariable();
		parser.setString("y/100");
		y[1] = parser.expression();
		System.out.println(x[1]+" : "+x[1].getValue());
		parser.setString("x^3/10000");
		y[2] = parser.expression();
		viewer.addGraph(y,-10,10,0.01);

		add(viewer);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		new ThreeDGraphViewer().init();
	}

}

import javax.swing.JFrame;

import parser.Parser;
import viewer.Viewer;
import expression.Node;

public class ThreeDGraphViewer extends JFrame{
	
	public void init()
	{
		Node[] x = new Node[3];
		Parser parser = new Parser("x:100*t");
		parser.parseVariable();
		parser.setString("x");
		x[0] = parser.expression();
		parser.setString("y:(x^3-4.5345699*x^2+8)^(-1/3)");
		parser.parseVariable();
		parser.setString("1000*y");
		x[1] = parser.expression();
		System.out.println(x[1]+" : "+x[1].getValue());
		parser.setString("0");
		x[2] = parser.expression();
		Viewer viewer = new Viewer();
		viewer.addGraph(x,-100,100,0.01);

		add(viewer);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		while(true)
		{
			viewer.update();
		}
	}

	public static void main(String[] args)
	{
		new ThreeDGraphViewer().init();
	}

}

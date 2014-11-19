import javax.swing.JFrame;

import parser.Parser;
import viewer.Viewer;
import expression.Node;

public class ThreeDGraphViewer extends JFrame{
	
	public void init()
	{
		Node[] x = new Node[3];
		Parser parser = new Parser("t");
		x[0] = parser.expression();
		parser.setString("200*sin[t/10]");
		x[1] = parser.expression();
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

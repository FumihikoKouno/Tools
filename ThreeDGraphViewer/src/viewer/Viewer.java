package viewer;

import expression.Node;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import common.Quaternion;
import common.Vector3D;

public class Viewer extends JPanel{
	private double sita = 0;
	
	private static final int DIMENSION = 3;
	private ArrayList<Node[]> nodes = new ArrayList<Node[]>();
	private ArrayList<Double> lowers = new ArrayList<Double>();
	private ArrayList<Double> uppers = new ArrayList<Double>();
	private ArrayList<Double> intervals = new ArrayList<Double>();

	public Viewer()
	{
		setPreferredSize(new Dimension(500,500));
	}
	
	public void update()
	{
		repaint();
	}
	
	public int getIndex(Node[] n)
	{
		if(n.length != DIMENSION) return -1;
		for(int i = 0; i < nodes.size(); i++)
		{
			for(int j = 0; j < DIMENSION; j++)
			{
				if(!n[j].equals((nodes.get(i)[j])))
				{
					break;
				}
				return i;
			}
		}
		return -1;
	}
	public void addGraph(Node[] n, double l, double u, double i)
	{
		if(n.length!=3) return;
		nodes.add(n);
		lowers.add(l);
		uppers.add(u);
		intervals.add(i);
	}
	public void addNodes(Node[] n)
	{
		addGraph(n,0,0,1);
	}
	public void setNodes(int i, Node[] n)
	{
		nodes.set(i, n);
	}
	public void setLowerTime(int i, double t)
	{
		lowers.set(i, t);
	}
	public void setUpperTime(int i, double t)
	{
		uppers.set(i, t);
	}
	public void setTimeInterval(int i, double t)
	{
		intervals.set(i, t);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		sita += Math.PI/60;
		for(int i = 0; i < nodes.size(); i++)
		{
			ArrayList<Vector3D> points = new ArrayList<Vector3D>();
			for(double t = lowers.get(i); t <= uppers.get(i); t += intervals.get(i))
			{
				for(int idx = 0; idx < nodes.get(i).length; idx++)
				{
					(nodes.get(i))[idx].substitute(t);
				}
				Vector3D newPoint = new Vector3D((nodes.get(i))[0].getValue().getValue(),
						(nodes.get(i))[1].getValue().getValue(),
						(nodes.get(i))[2].getValue().getValue());

				points.add(Quaternion.rotate(newPoint,
											new Vector3D(0,0,1),
											sita));

			}
			for(int j = 0; j < points.size()-1; j++)
			{
				g.drawLine(250+(int)points.get(j).get(0), 250+(int)points.get(j).get(1), 
							250+(int)points.get(j+1).get(0), 250+(int)points.get(j+1).get(1));
			}
		}
	}
}

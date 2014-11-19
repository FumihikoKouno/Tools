package viewer;

import expression.Node;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import common.Quaternion;
import common.Vector3D;

public class Viewer extends JPanel implements MouseListener, MouseMotionListener{
	private double sita = 0;
	private int width = 500;
	private int height = 500;
	private Vector3D clickPoint = new Vector3D(0,0,0);
	private Vector3D currentPoint = new Vector3D(0,0,0);
	private Quaternion quaternion = new Quaternion(1,new Vector3D(0,0,0));
	private Quaternion draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
	private Vector3D viewOrigin = new Vector3D(width/2,height/2,0);
	private static final int DIMENSION = 3;
	private ArrayList<Node[]> nodes = new ArrayList<Node[]>();
	private ArrayList<Double> lowers = new ArrayList<Double>();
	private ArrayList<Double> uppers = new ArrayList<Double>();
	private ArrayList<Double> intervals = new ArrayList<Double>();

	public Viewer()
	{
		setPreferredSize(new Dimension(width,height));
		addMouseMotionListener(this);
		addMouseListener(this);
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
	public void drawAxes(Graphics g) 
	{
		double r = Math.sqrt((width)*(width)+(height)*(height));
		drawLine(g,new Vector3D(-r,0,0),new Vector3D(r,0,0));
		drawLine(g,new Vector3D(0,-r,0),new Vector3D(0,r,0));
		drawLine(g,new Vector3D(0,0,-r),new Vector3D(0,0,r));
	}
	public void drawLine(Graphics g, Vector3D begin, Vector3D end)
	{
		Vector3D b = draggingQuaternion.rotate(quaternion.rotate(begin));
		Vector3D e = draggingQuaternion.rotate(quaternion.rotate(end));

		g.drawLine((int)(width/2.0+b.get(0)),
				(int)(height/2.0-b.get(1)), 
				(int)(width/2.0+e.get(0)), 
				(int)(height/2.0-e.get(1)));	
	}
	@Override
	public void paintComponent(Graphics g) 
	{
		synchronized(this)
		{
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			g.fillRect(0,0,width,height);
			g.setColor(Color.BLACK);
			drawAxes(g);
			g.setColor(Color.BLUE);
			for(int i = 0; i < nodes.size(); i++)
			{
				System.out.println(nodes.size());
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
	
					points.add(newPoint);
				}
				for(int j = 0; j < points.size()-1; j++)
				{
					drawLine(g,points.get(j),points.get(j+1));
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		double x = e.getX()-width/2.0;
		double y = e.getY()-height/2.0;
		double r2 = (width/2.0)*(width/2.0)
					+(height/2.0)*(height/2.0);
		double z = Math.sqrt(r2-x*x-y*y);
		Vector3D vec = new Vector3D(x,y,z);
		draggingQuaternion = Quaternion.createQuaternion(clickPoint,vec);
		update();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		double x = e.getX()-width/2.0;
		double y = e.getY()-height/2.0;
		double r2 = (width/2.0)*(width/2.0)
					+(height/2.0)*(height/2.0);
		double z = Math.sqrt(r2-x*x-y*y);
		clickPoint = new Vector3D(x,y,z);
		update();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		quaternion = Quaternion.mul(draggingQuaternion,quaternion);
		draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
		update();
	}
	
}

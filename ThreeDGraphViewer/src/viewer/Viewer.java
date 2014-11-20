package viewer;

import expression.Node;
import expression.Parameter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import common.Quaternion;
import common.Vector3D;

public class Viewer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
	private int width = 500;
	private int height = 500;
	private double maxX = -Double.MAX_VALUE;
	private double minX = Double.MAX_VALUE;
	private double maxY = -Double.MAX_VALUE;
	private double minY = Double.MAX_VALUE;
	private double xUnit = 1.0;
	private double yUnit = 1.0;
	private double spaceRatio = 1.0/10.0;
	private Vector3D displacement = new Vector3D(0,0,0);
	private Vector3D prevDisplacement = new Vector3D(0,0,0);
	private boolean intervalMode = false;
	private double axisLengthUnit = 20;
	private Vector3D clickPoint = new Vector3D(0,0,0);
	private Vector3D clickDisplacement = new Vector3D(0,0,0);
	private Vector3D currentPoint = new Vector3D(0,0,0);
	private Quaternion quaternion = new Quaternion(1,new Vector3D(0,0,0));
	private Quaternion draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
	private static final int DIMENSION = 3;
	private ArrayList<Node[]> nodes = new ArrayList<Node[]>();
	private ArrayList<Double> lowers = new ArrayList<Double>();
	private ArrayList<Double> uppers = new ArrayList<Double>();
	private ArrayList<Double> intervals = new ArrayList<Double>();
	private ArrayList<Double> parameterIntervals = new ArrayList<Double>();
	private ArrayList<Color> colors = new ArrayList<Color>();
	private ArrayList<DrawPoints> dps = new ArrayList<DrawPoints>();
	private Vector3D rotationPoint = new Vector3D(0,0,1);

	public Viewer()
	{
		setPreferredSize(new Dimension(width,height));
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}
	
	public void update()
	{
		repaint();
	}
	
	public void viewInterval(boolean b)
	{
		intervalMode = b;
	}
	
	public void setParameterInterval(int i, double d)
	{
		parameterIntervals.set(i,d);
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
	public void addGraph(Node[] n, double l, double u, double i, double p, Color c)
	{
		if(n.length!=3) return;
		for(double t = l; t <= u; t += i)
		{
			n[0].substitute(t);
			n[1].substitute(t);
			
			double x = n[0].getValue().getValue();
			double y = n[1].getValue().getValue();
			
			if(maxX < x) maxX = x;
			if(minX > x) minX = x;
			if(maxY < y) maxY = y;
			if(minY > y) minY = y;
		}
		nodes.add(n);
		lowers.add(l);
		uppers.add(u);
		intervals.add(i);
		parameterIntervals.add(p);
		colors.add(c);
		resetViewing();
	}
	public void clear()
	{
		dps.clear();
		nodes.clear();
		lowers.clear();
		uppers.clear();
		intervals.clear();
		parameterIntervals.clear();
		colors.clear();
		maxX = -Double.MAX_VALUE;
		minX = Double.MAX_VALUE;
		maxY = -Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
	}
	public void addNodes(Node[] n)
	{
		addGraph(n,0,0,1,0.2,Color.BLUE);
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
	public void drawAxes() 
	{
		double r = Math.sqrt((width)*(width)+(height)*(height));
		DrawPoints x = new DrawPoints();
		x.addPoint(new Vector3D(-r,0,0));
		x.addPoint(new Vector3D(r,0,0));
		x.setColor(Color.BLACK);
		DrawPoints y = new DrawPoints();
		y.addPoint(new Vector3D(0,-r,0));
		y.addPoint(new Vector3D(0,r,0));
		y.setColor(Color.BLACK);
		DrawPoints z = new DrawPoints();
		z.addPoint(new Vector3D(0,0,-r));
		z.addPoint(new Vector3D(0,0,r));
		z.setColor(Color.BLACK);
		
		dps.add(x);
		dps.add(y);
		dps.add(z);
	}
	
	public void addAxes()
	{
		double r = Math.sqrt((width)*(width)+(height)*(height));
		double thick = 1.0/((Math.max(xUnit,yUnit)/4)*Math.sqrt(2.0));
		for(double i = -r; i <= r-axisLengthUnit; i+=axisLengthUnit)
		{
			DrawPoints x = new DrawPoints();
			x.setColor(Color.BLACK);
			x.addPoint(Vector3D.add(new Vector3D(i,0,0),new Vector3D(0,-thick,-thick)));
			x.addPoint(Vector3D.add(new Vector3D(i,0,0),new Vector3D(0,-thick,thick)));
			x.addPoint(Vector3D.add(new Vector3D(i,0,0),new Vector3D(0,thick,-thick)));
			x.addPoint(Vector3D.add(new Vector3D(i,0,0),new Vector3D(0,thick,thick)));
			x.addPoint(Vector3D.add(new Vector3D(i+axisLengthUnit,0,0),new Vector3D(0,thick,thick)));
			x.addPoint(Vector3D.add(new Vector3D(i+axisLengthUnit,0,0),new Vector3D(0,thick,-thick)));
			x.addPoint(Vector3D.add(new Vector3D(i+axisLengthUnit,0,0),new Vector3D(0,-thick,thick)));
			x.addPoint(Vector3D.add(new Vector3D(i+axisLengthUnit,0,0),new Vector3D(0,-thick,-thick)));
			dps.add(x);
		}
		for(double i = -r; i <= r-axisLengthUnit; i+=axisLengthUnit)
		{
			DrawPoints y = new DrawPoints();
			y.setColor(Color.BLACK);
			y.addPoint(Vector3D.add(new Vector3D(0,i,0),new Vector3D(-thick,0,-thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i,0),new Vector3D(-thick,0,thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i,0),new Vector3D(thick,0,-thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i,0),new Vector3D(thick,0,thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i+axisLengthUnit,0),new Vector3D(thick,0,thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i+axisLengthUnit,0),new Vector3D(thick,0,-thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i+axisLengthUnit,0),new Vector3D(-thick,0,thick)));
			y.addPoint(Vector3D.add(new Vector3D(0,i+axisLengthUnit,0),new Vector3D(-thick,0,-thick)));
			dps.add(y);
		}
		for(double i = -r; i <= r-axisLengthUnit; i+=axisLengthUnit)
		{
			DrawPoints z = new DrawPoints();
			z.setColor(Color.BLACK);
			z.addPoint(Vector3D.add(new Vector3D(0,0,i),new Vector3D(-thick,-thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i),new Vector3D(-thick,thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i),new Vector3D(thick,-thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i),new Vector3D(thick,thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i+axisLengthUnit),new Vector3D(thick,thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i+axisLengthUnit),new Vector3D(thick,-thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i+axisLengthUnit),new Vector3D(-thick,thick,0)));
			z.addPoint(Vector3D.add(new Vector3D(0,0,i+axisLengthUnit),new Vector3D(-thick,-thick,0)));
			dps.add(z);
		}
	}
	public void fillPolygon(Graphics g)
	{
		int n = dps.size();
		ArrayList<DrawPoints> points = new ArrayList<DrawPoints>();
		for(int i = 0; i < n; i++)
		{
			DrawPoints point = new DrawPoints();
			int numOfPoint = dps.get(i).getPoints().size();
			for(int j = 0; j < numOfPoint; j++)
			{
				Vector3D vec = draggingQuaternion.rotate(dps.get(i).getPoints().get(j));
				point.addPoint(vec);
			}
			point.setColor(dps.get(i).getColor());
			points.add(point);
		}
		try{
			Collections.sort(points,new DepthComparator());
		}catch(IllegalArgumentException e){
			update();
			return;
		}
		for(int i = 0; i < n; i++)
		{
			g.setColor(points.get(i).getColor());
			int numOfPoint = points.get(i).getPoints().size();
			int[] x = new int[numOfPoint];
			int[] y = new int[numOfPoint];
			for(int j = 0; j < numOfPoint; j++)
			{
				Vector3D vec = toViewerPoint(points.get(i).getPoints().get(j));
				x[j] = (int)(vec.get(0));
				y[j] = (int)(vec.get(1));
			}
			g.fillPolygon(x,y,numOfPoint);
		}
	}
	public void drawLine(Graphics g, Vector3D begin, Vector3D end)
	{
		Vector3D b = toViewerPoint(draggingQuaternion.rotate(begin));
		Vector3D e = toViewerPoint(draggingQuaternion.rotate(end));
		
		g.drawLine((int)(b.get(0)),
				(int)(b.get(1)), 
				(int)(e.get(0)), 
				(int)(e.get(1)));	
	}
	
	public void resetViewing()
	{
		xUnit = (width/(maxX-minX))*(1-spaceRatio);
		yUnit = (height/(maxY-minY))*(1-spaceRatio);
		prevDisplacement = new Vector3D(0,0,0);
		displacement = new Vector3D(0,0,0);
		quaternion = new Quaternion(1,new Vector3D(0,0,0));
		draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
		rotationPoint = new Vector3D(0,0,1);
		makePlot();
	}
	
	public void addPointsWithParameters(
			DrawPoints points, 
			Node[] nodes,
			ArrayList<Parameter> params, 
			int idx, 
			double parIv,
			boolean back)
	{
		if(back)
		{
			double par;
			for(par = 0; par <= 1; par += parIv);
			par -= parIv;
			for(; par >= 0; par -= parIv)
			{
				for(int i = 0; i < nodes.length; i++)
				{
					nodes[i].setParameter(params.get(idx),par);
				}
				if(idx == 0)
				{
					points.addPoint(new Vector3D(nodes[0].getValue().getValue(),
												nodes[1].getValue().getValue(),
												nodes[2].getValue().getValue()));
				}
				else
				{
					addPointsWithParameters(points, nodes, params, idx-1, parIv, back);
				}
			}
		}
		else
		{
			double par;
			for(par = 0; par <= 1; par += parIv)
			{
				for(int i = 0; i < nodes.length; i++)
				{
					nodes[i].setParameter(params.get(idx),par);
				}
				if(idx == params.size()-1)
				{
					points.addPoint(new Vector3D(nodes[0].getValue().getValue(),
												nodes[1].getValue().getValue(),
												nodes[2].getValue().getValue()));
				}
				else
				{
					addPointsWithParameters(points, nodes, params, idx+1, parIv, back);
				}
			}
			
		}
	}
	
	public void makeThicknessLine(DrawPoints dp)
	{
		if(dp.getPoints().size()!=2)
		{
			dp.reset();
			return;
		}
		Vector3D begin = dp.getPoints().get(0);
		Vector3D end = dp.getPoints().get(1);
		
		Vector3D vec = Vector3D.sub(end,begin);
		Vector3D vertical;
		
		DrawPoints newDp = new DrawPoints();
		if(vec.get(0)!=0)
		{
			vertical = new Vector3D(-vec.get(1)/vec.get(0),1,0);
		}
		else if(vec.get(1)!=0)
		{
			vertical = new Vector3D(0,-vec.get(2)/vec.get(1),1);
		}
		else if(vec.get(2)!=0)
		{
			vertical = new Vector3D(1,0,-vec.get(0)/vec.get(2));
		}
		else
		{
			dp.reset();
			return;
		}
		double thick = 1.0/(Math.max(xUnit,yUnit)/4.0);
		vertical = Vector3D.normalize(vertical);
		vertical = Vector3D.mul(thick,vertical);
		Vector3D vertical2 = Vector3D.cross(vec,vertical);
		vertical2 = Vector3D.normalize(vertical2);
		vertical2 = Vector3D.mul(thick,vertical2);
		newDp.addPoint(Vector3D.add(vertical,begin));
		newDp.addPoint(Vector3D.add(Vector3D.mul(-1.0,vertical),begin));
		newDp.addPoint(Vector3D.add(vertical2,begin));
		newDp.addPoint(Vector3D.add(Vector3D.mul(-1.0,vertical2),begin));
		newDp.addPoint(Vector3D.add(Vector3D.mul(-1.0,vertical2),end));
		newDp.addPoint(Vector3D.add(vertical2,end));
		newDp.addPoint(Vector3D.add(Vector3D.mul(-1.0,vertical),end));
		newDp.addPoint(Vector3D.add(vertical,end));
		newDp.setColor(dp.getColor());
		dp.reset();
		for(int i = 0; i < newDp.getPoints().size(); i++)
		{
			dp.addPoint(newDp.getPoints().get(i));
		}
	}
	
	public Vector3D toViewerPoint(Vector3D vec)
	{
		double x = displacement.get(0)+(((width)*spaceRatio)/2.0)+((vec.get(0)-minX)*xUnit);
		double y = displacement.get(1)+(((height)*spaceRatio)/2.0)+((maxY-vec.get(1))*yUnit);

		return new Vector3D(x,y,0);
	}
	
	public void rotate()
	{
		for(int i = 0; i < dps.size(); i++)
		{
			for(int j = 0; j < dps.get(i).getPoints().size(); j++)
			{
				dps.get(i).setPoint(j,quaternion.rotate(dps.get(i).getPoints().get(j)));
			}
		}
	}
	
	public void makePlot(){
		dps.clear();
		for(int i = 0; i < nodes.size(); i++)
		{
			for(double t = lowers.get(i); t <= uppers.get(i)-intervals.get(i); t += intervals.get(i))
			{
				ArrayList<Parameter> parameters = new ArrayList<Parameter>();
				for(int idx = 0; idx < nodes.get(i).length; idx++)
				{
					for(int j = 0; j < DIMENSION; j++)
					{
						ArrayList<Parameter> tmp = (nodes.get(i))[j].getParameters();
						for(int k = 0; k < tmp.size(); k++)
						{
							parameters.add(tmp.get(k));
						}
					}
				}
				
				DrawPoints dp = new DrawPoints();
				
				// add data f(t)
				(nodes.get(i))[0].substitute(t);
				(nodes.get(i))[1].substitute(t);
				(nodes.get(i))[2].substitute(t);
				if(intervalMode)
				{
					if(parameters.size()>0)
					{
						addPointsWithParameters(dp,nodes.get(i),parameters,0,parameterIntervals.get(i),false);
					}
					else
					{
						dp.addPoint(new Vector3D(nodes.get(i)[0].getValue().getValue(),
								nodes.get(i)[1].getValue().getValue(),
								nodes.get(i)[2].getValue().getValue()));
					}
				}
				else
				{
					dp.addPoint(new Vector3D(nodes.get(i)[0].getValue().getValue(),
											nodes.get(i)[1].getValue().getValue(),
											nodes.get(i)[2].getValue().getValue()));
				}
				// add data f(t+interval)
				(nodes.get(i))[0].substitute(t+intervals.get(i));
				(nodes.get(i))[1].substitute(t+intervals.get(i));
				(nodes.get(i))[2].substitute(t+intervals.get(i));
				if(intervalMode)
				{
					if(parameters.size()>0)
					{
						addPointsWithParameters(dp,nodes.get(i),parameters,parameters.size()-1,parameterIntervals.get(i),true);
					}
					else
					{
						dp.addPoint(new Vector3D(nodes.get(i)[0].getValue().getValue(),
								nodes.get(i)[1].getValue().getValue(),
								nodes.get(i)[2].getValue().getValue()));
					}
				}
				else
				{
					dp.addPoint(new Vector3D(nodes.get(i)[0].getValue().getValue(),
							nodes.get(i)[1].getValue().getValue(),
							nodes.get(i)[2].getValue().getValue()));
				}
				dp.setColor(colors.get(i));
				if(intervalMode && parameters.size()==0)
				{
					makeThicknessLine(dp);
				}
				dps.add(dp);
			}
		}
		if(intervalMode)
		{
			addAxes();
		}
		else
		{
			drawAxes();
		}
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
			if(intervalMode)
			{
				fillPolygon(g);
			}
			else
			{
				for(int j = 0; j < dps.size(); j++)
				{
					g.setColor(dps.get(j).getColor());
					drawLine(g,dps.get(j).getPoints().get(0),dps.get(j).getPoints().get(1));
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
		{
			double x = (e.getX()-width/2.0);
			double y = (-e.getY()+height/2.0);
			double r2 = (width/2.0)*(width/2.0)
						+(height/2.0)*(height/2.0);
			double z = Math.sqrt(r2-x*x-y*y);
			double zUnit = Math.sqrt(xUnit*xUnit+yUnit*yUnit);
			Vector3D vec = new Vector3D(x/xUnit,y/yUnit,z/zUnit);
			draggingQuaternion = Quaternion.createQuaternion(clickPoint,vec);
		}
		else if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
		{
			displacement = Vector3D.add(prevDisplacement,Vector3D.sub(new Vector3D(e.getX(),e.getY(),0),clickDisplacement));
		}
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
		if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
		{
			double x = (e.getX()-width/2.0);
			double y = (-e.getY()+height/2.0);
			double r2 = (width/2.0)*(width/2.0)
						+(height/2.0)*(height/2.0);
			double z = Math.sqrt(r2-x*x-y*y);
			double zUnit = Math.sqrt(xUnit*xUnit+yUnit*yUnit);
			clickPoint = new Vector3D(x/xUnit,y/yUnit,z/zUnit);
		}
		else if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
		{
			clickDisplacement = new Vector3D(e.getX(),e.getY(),0);
		}
		update();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
		{
			quaternion = new Quaternion(draggingQuaternion);
			draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
			rotate();
		}
		else if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)
		{
			prevDisplacement = displacement;
			clickDisplacement = new Vector3D(0,0,0);
		}
		update();
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double x = (e.getX());
		double y = (e.getY());
		
		double ratio = 1-e.getWheelRotation()/10.0;
		
		xUnit = xUnit*ratio;
		yUnit = yUnit*ratio;
		
		Vector3D mouse = new Vector3D(x,y,0);
		Vector3D tmp = Vector3D.sub(displacement, mouse);
		prevDisplacement = Vector3D.add(Vector3D.mul(ratio,tmp),mouse);
		displacement = Vector3D.add(Vector3D.mul(ratio,tmp),mouse);
		update();
	}
}

package viewer;

import expression.Node;
import expression.Num;
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
import common.VectorHeightComparator;

public class Viewer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
	private int width = 500;
	private int height = 500;

	private static final double DEFAULT_LOWER_TIME = 0;
	private static final double DEFAULT_UPPER_TIME = 10;
	private static final double DEFAULT_TIME_INTERVAL = 0.1;
	private static final double DEFAULT_PARAMETER_INTERVAL = 0.2;
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	private Vector3D axisX = new Vector3D(1,0,0);
	private Vector3D axisY = new Vector3D(0,1,0);
	private Vector3D axisZ = new Vector3D(0,0,1);
	
	private int axisStringNum = 5;
	
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
	private boolean severalLines = false;
	private double axisLengthUnit = 20;
	private Vector3D clickPoint = new Vector3D(0,0,0);
	private Vector3D clickDisplacement = new Vector3D(0,0,0);
	private Quaternion quaternion = new Quaternion(1,new Vector3D(0,0,0));
	private Quaternion draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
	private static final int DIMENSION = 3;
	private ArrayList<Node[]> nodes = new ArrayList<Node[]>();
	private ArrayList<Node> lowers = new ArrayList<Node>();
	private ArrayList<Node> uppers = new ArrayList<Node>();
	private ArrayList<Double> intervals = new ArrayList<Double>();
	private ArrayList<Double> parameterIntervals = new ArrayList<Double>();
	private ArrayList<Color> colors = new ArrayList<Color>();
	private ArrayList<Vector3D> points = new ArrayList<Vector3D>();
	private ArrayList<Vector3D> rotatedPoints = new ArrayList<Vector3D>();
	private ArrayList<DrawPoints> dps = new ArrayList<DrawPoints>();

	public Viewer()
	{
		setPreferredSize(new Dimension(width,height));
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}
	
	// update Window
	public void update()
	{
		repaint();
	}
	// set Interval Mode (if b then draw Interval)
	public void viewInterval(boolean b)
	{
		intervalMode = b;
		resetViewing();
	}
	// set Several Mode (if b then draw several lines)
	public void viewSeveralLines(boolean b)
	{
		severalLines = b;
		resetViewing();
	}
	// if Several Mode, return true, else return false
	public boolean getSeveralMode()
	{
		return severalLines;
	}
	// if Interval Mode, return true, else return false
	public boolean getIntervalMode()
	{
		return intervalMode;
	}
	// set i th Parameter Interval
	public void setParameterInterval(int i, double d)
	{
		parameterIntervals.set(i,d);
		resetViewing();
	}
	// get Graph index
	public int getIndex(Node[] n)
	{
		if(n.length != DIMENSION) return -1;
		for(int i = 0; i < nodes.size(); i++)
		{
			boolean equal = true;
			for(int j = 0; j < DIMENSION; j++)
			{
				if(!n[j].equals((nodes.get(i)[j])))
				{
					equal = false;
					break;
				}
			}
			if(equal) return i;
		}
		return -1;
	}
	// add Graph
	public void addGraph(Node[] n, Node l, Node u, double i, double p, Color c)
	{
		if(n.length!=3) return;
		nodes.add(n);
		lowers.add(l);
		uppers.add(u);
		intervals.add(i);
		parameterIntervals.add(p);
		colors.add(c);
		resetViewing();
	}
	// set i th Graph
	public void setGraph(int index, Node[] n, Node l, Node u, double i, double p, Color c)
	{
		if(n.length!=3) return;
		nodes.set(index,n);
		lowers.set(index,l);
		uppers.set(index,u);
		intervals.set(index,i);
		parameterIntervals.set(index,p);
		colors.set(index,c);
		resetViewing();
	}
	// clear data
	public void clear()
	{
		dps.clear();
		points.clear();
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
	// add Node
	public void addNodes(Node[] n)
	{
		addGraph(n,new Num(DEFAULT_LOWER_TIME),new Num(DEFAULT_UPPER_TIME),DEFAULT_TIME_INTERVAL,DEFAULT_PARAMETER_INTERVAL,DEFAULT_COLOR);
	}
	// set i th Node
	public void setNodes(int i, Node[] n)
	{
		nodes.set(i, n);
		update();
	}
	// set i th lower time
	public void setLowerTime(int i, Node t)
	{
		lowers.set(i, t);
		resetViewing();
	}
	// set i th upper time
	public void setUpperTime(int i, Node t)
	{
		uppers.set(i, t);
		resetViewing();
	}
	// set i th time interval
	public void setTimeInterval(int i, double t)
	{
		intervals.set(i, t);
		resetViewing();
	}
	// add 2D axes
	private void addLineAxes() 
	{
		DrawPoints x = new DrawPoints();
		x.addPoint(newPointIndex(new Vector3D(minX-xUnit*width*spaceRatio/2,0,0)));
		x.addPoint(newPointIndex(new Vector3D(maxX+xUnit*width*spaceRatio/2,0,0)));
		x.setColor(Color.BLACK);
		
		DrawPoints y = new DrawPoints();
		y.addPoint(newPointIndex(new Vector3D(0,minY-yUnit*height*spaceRatio/2,0)));
		y.addPoint(newPointIndex(new Vector3D(0,maxY+yUnit*height*spaceRatio/2,0)));
		y.setColor(Color.BLACK);
		
		DrawPoints z = new DrawPoints();
		z.addPoint(newPointIndex(new Vector3D(0,0,(minX-xUnit*width*spaceRatio/2)+(minY-yUnit*height*spaceRatio/2))));
		z.addPoint(newPointIndex(new Vector3D(0,0,(maxX+xUnit*width*spaceRatio/2)+(maxY+yUnit*height*spaceRatio/2))));
		z.setColor(Color.BLACK);
		
		dps.add(x);
		dps.add(y);
		dps.add(z);
	}
	// sub function for adding plot
	private int newPointIndex(Vector3D vec)
	{
		int ret = points.indexOf(vec);
		if(ret < 0)
		{
			points.add(vec);
			return points.size()-1;
		}
		return ret;
	}
	// add 3D axes
	private void addThickAxes()
	{
		double bX = minX-xUnit*width*spaceRatio/2;
		double eX = maxX+xUnit*width*spaceRatio/2;
		for(double t = bX; t < eX - axisLengthUnit; t += axisLengthUnit)
		{
			DrawPoints x = new DrawPoints();
			x.addPoint(newPointIndex(new Vector3D(t,0,0)));
			x.addPoint(newPointIndex(new Vector3D(t+axisLengthUnit,0,0)));
			x.setColor(Color.BLACK);
			dps.add(x);
		}

		double bY = minY-yUnit*height*spaceRatio/2;
		double eY = maxY+yUnit*height*spaceRatio/2;
		for(double t = bY; t < eY - axisLengthUnit; t += axisLengthUnit)
		{
			DrawPoints y = new DrawPoints();
			y.addPoint(newPointIndex(new Vector3D(0,t,0)));
			y.addPoint(newPointIndex(new Vector3D(0,t+axisLengthUnit,0)));
			y.setColor(Color.BLACK);
			dps.add(y);
		}
		
		double bZ = (minX-xUnit*width*spaceRatio/2)+(minY-yUnit*height*spaceRatio/2);
		double eZ = (maxX+xUnit*width*spaceRatio/2)+(maxY+yUnit*height*spaceRatio/2);
		for(double t = bZ; t < eZ - axisLengthUnit; t += axisLengthUnit)
		{
			DrawPoints z = new DrawPoints();
			z.addPoint(newPointIndex(new Vector3D(0,0,t)));
			z.addPoint(newPointIndex(new Vector3D(0,0,t+axisLengthUnit)));
			z.setColor(Color.BLACK);
			dps.add(z);
		}
	}
	// return Convex for dp
	private DrawPoints calculateConvex(ArrayList<Vector3D> v, DrawPoints dp)
	{ 
		ArrayList<Vector3D> vecs = new ArrayList<Vector3D>();
		
		boolean inWindow = false;
		for(int i = 0; i < dp.getPoints().size(); i++)
		{
			if(!inWindow)
			{
				inWindow = inWindow || (0 <= v.get(dp.getPoint(i)).get(0) && v.get(dp.getPoint(i)).get(0) <= width) || (0 <= v.get(dp.getPoint(i)).get(1) && v.get(dp.getPoint(i)).get(1) <= height);
			}
			
			vecs.add(new Vector3D(v.get(dp.getPoint(i))));
		}
		if(!inWindow)
		{
			return null;
		}
		
		Collections.sort(vecs,new VectorHeightComparator());
		
		ArrayList<Vector3D> plainPoints = new ArrayList<Vector3D>();
		
		for(int i = 0; i < vecs.size(); i++)
		{
			boolean included = false;
			Vector3D plain = vecs.get(i);
			
			for(int j = 0; j < plainPoints.size(); j++)
			{
				if(plain.get(0)==plainPoints.get(j).get(0)
				&& plain.get(1)==plainPoints.get(j).get(1))
				{
					included = true;
					break;
				}
			}
			if(!included)
			{
				plainPoints.add(plain);
			}
		}
		
		
		DrawPoints ret = new DrawPoints();
		ret.setColor(dp.getColor());

		if(plainPoints.size()<=2)
		{
			for(int i = 0; i < plainPoints.size(); i++)
			{
				ret.addPoint(rotatedPoints.indexOf(plainPoints.get(i)));
			}
			return ret;
		}
		
		
		Vector3D startPoint = plainPoints.get(0);
		
		Vector3D prevVec = new Vector3D(1,0,0);
		double minimumRadian = Double.MAX_VALUE;
		Vector3D nextPoint = new Vector3D(0,0,0);
		Vector3D prevPoint = new Vector3D(0,0,0);
		
		Vector3D currentPoint = startPoint;
		ret.addPoint(rotatedPoints.indexOf(startPoint));
		
		
		for(int count = 0; count < plainPoints.size(); count++)
		{
			for(int i = 0; i < plainPoints.size(); i++)
			{
				Vector3D candidatePoint = new Vector3D(plainPoints.get(i));
				
				if(candidatePoint.equals(currentPoint)
				|| candidatePoint.equals(prevPoint))
				{
					continue;
				}
				Vector3D currentVec = Vector3D.sub(candidatePoint,currentPoint);
				
				double dot = prevVec.get(0)*currentVec.get(0) + prevVec.get(1)*currentVec.get(1);
				double abs = Math.sqrt(prevVec.get(0)*prevVec.get(0)+prevVec.get(1)*prevVec.get(1))
							* Math.sqrt(currentVec.get(0)*currentVec.get(0)+currentVec.get(1)*currentVec.get(1));
				double radian;
				if(dot>=abs)
				{
					radian = Math.acos(1);
				}
				else if(dot<=-abs)
				{
					radian = Math.acos(-1);
				}
				else
				{
					radian = Math.acos(dot/abs);
				}
				
				if(minimumRadian > radian)
				{
					minimumRadian = radian;
					nextPoint = new Vector3D(candidatePoint);
				}
			}
			if(startPoint.equals(nextPoint))
			{
				return ret;
			}
			
			ret.addPoint(rotatedPoints.indexOf(nextPoint));

			prevVec = Vector3D.sub(nextPoint,currentPoint);
			
			prevPoint = new Vector3D(currentPoint);
			currentPoint = new Vector3D(nextPoint);
			minimumRadian = Double.MAX_VALUE;
		}
		
		return ret;
	}
	// show polygon
	private void fillPolygons(Graphics g)
	{
		int n = dps.size();
		rotatedPoints = new ArrayList<Vector3D>();
		for(int i = 0; i < points.size(); i++)
		{
			Vector3D vec = toViewerPoint(draggingQuaternion.rotate(points.get(i)));
			rotatedPoints.add(vec);
		}
		
		ArrayList<DrawPoints> convexPoints = new ArrayList<DrawPoints>();
		for(int i = 0; i < n; i++)
		{
			ArrayList<Vector3D> vecs = new ArrayList<Vector3D>(rotatedPoints);
			DrawPoints convexPoint = calculateConvex(vecs,dps.get(i));
			if(convexPoint != null)
			{
				convexPoints.add(convexPoint);
			}
		}
		n = convexPoints.size();
		
		Collections.sort(convexPoints,new DepthComparator(rotatedPoints));

		for(int i = 0; i < n; i++)
		{
			g.setColor(convexPoints.get(i).getColor());			
			
			int numOfPoint = convexPoints.get(i).getPoints().size();
			
			if(numOfPoint==2)
			{
				Vector3D vec1 = new Vector3D(rotatedPoints.get(convexPoints.get(i).getPoints().get(0)));
				Vector3D vec2 = new Vector3D(rotatedPoints.get(convexPoints.get(i).getPoints().get(1)));
				g.drawLine((int)vec1.get(0),(int)vec1.get(1),(int)vec2.get(0),(int)vec2.get(1));
			}
			else
			{
				int[] x = new int[numOfPoint];
				int[] y = new int[numOfPoint];
				
				for(int j = 0; j < numOfPoint; j++)
				{
					Vector3D vec = new Vector3D(rotatedPoints.get(convexPoints.get(i).getPoints().get(j)));
					x[j] = (int)(vec.get(0));
					y[j] = (int)(vec.get(1));
				}
				g.fillPolygon(x,y,numOfPoint);
			}
		}
	}
	// show lines
	private void drawLines(Graphics g)
	{
		for(int i = 0; i < dps.size(); i++)
		{
			Vector3D b = toViewerPoint(draggingQuaternion.rotate(points.get(dps.get(i).getPoints().get(0))));
			Vector3D e = toViewerPoint(draggingQuaternion.rotate(points.get(dps.get(i).getPoints().get(1))));
			
			g.setColor(dps.get(i).getColor());
			
			g.drawLine((int)(b.get(0)),
					(int)(b.get(1)), 
					(int)(e.get(0)), 
					(int)(e.get(1)));	
		}
	}
	// reset viewing
	public void resetViewing()
	{
		makePlot();
		if(maxX==minX)
		{
			maxX += 1;
			minX -= 1;
		}
		if(maxY==minY)
		{
			maxY += 1;
			minY -= 1;
		}
		axisX = new Vector3D(1,0,0);
		axisY = new Vector3D(1,0,0);
		axisZ = new Vector3D(1,0,0);
		xUnit = (width/(maxX-minX))*(1-spaceRatio);
		yUnit = (height/(maxY-minY))*(1-spaceRatio);
		prevDisplacement = new Vector3D(0,0,0);
		displacement = new Vector3D(0,0,0);
		quaternion = new Quaternion(1,new Vector3D(0,0,0));
		draggingQuaternion = new Quaternion(1,new Vector3D(0,0,0));
		update();
	}
	// convert point to Viewer point
	private Vector3D toViewerPoint(Vector3D vec)
	{
		double x = displacement.get(0)+(((width)*spaceRatio)/2.0)+((vec.get(0)-minX)*xUnit);
		double y = displacement.get(1)+(((height)*spaceRatio)/2.0)+((maxY-vec.get(1))*yUnit);
		
		return new Vector3D(x,y,vec.get(2));
	}
	
	private void drawAxisValues(Graphics g)
	{
		
		// TODO: implement
	}
	
	// make rotated points
	private void rotate()
	{
		for(int i = 0; i < points.size(); i++)
		{
			points.set(i,quaternion.rotate(points.get(i)));
		}
		axisX = quaternion.rotate(axisX);
		axisY = quaternion.rotate(axisY);
		axisZ = quaternion.rotate(axisZ);
	}
	// make single line
	private void makeSingleLinePlot()
	{
		dps.clear();
		points.clear();
		for(int i = 0; i < nodes.size(); i++)
		{
			for(double t = lowers.get(i).getValue().getValue(); t <= uppers.get(i).getValue().getValue()-intervals.get(i); t += intervals.get(i))
			{
				DrawPoints dp = new DrawPoints();
				
				// add data f(t)
				(nodes.get(i))[0].substitute(t);
				(nodes.get(i))[1].substitute(t);
				(nodes.get(i))[2].substitute(t);
				
				if(maxX<nodes.get(i)[0].getValue().getValue()) maxX = nodes.get(i)[0].getValue().getValue();
				if(minX>nodes.get(i)[0].getValue().getValue()) minX = nodes.get(i)[0].getValue().getValue();
				if(maxY<nodes.get(i)[1].getValue().getValue()) maxY = nodes.get(i)[1].getValue().getValue();
				if(minY>nodes.get(i)[1].getValue().getValue()) minY = nodes.get(i)[1].getValue().getValue();
				
				Vector3D pt = new Vector3D(nodes.get(i)[0].getValue().getValue(),
											nodes.get(i)[1].getValue().getValue(),
											nodes.get(i)[2].getValue().getValue());
				
				dp.addPoint(newPointIndex(pt));

				// add data f(t+interval)
				(nodes.get(i))[0].substitute(t+intervals.get(i));
				(nodes.get(i))[1].substitute(t+intervals.get(i));
				(nodes.get(i))[2].substitute(t+intervals.get(i));

				if(maxX<nodes.get(i)[0].getValue().getValue()) maxX = nodes.get(i)[0].getValue().getValue();
				if(minX>nodes.get(i)[0].getValue().getValue()) minX = nodes.get(i)[0].getValue().getValue();
				if(maxY<nodes.get(i)[1].getValue().getValue()) maxY = nodes.get(i)[1].getValue().getValue();
				if(minY>nodes.get(i)[1].getValue().getValue()) minY = nodes.get(i)[1].getValue().getValue();
				
				pt = new Vector3D(nodes.get(i)[0].getValue().getValue(),
						nodes.get(i)[1].getValue().getValue(),
						nodes.get(i)[2].getValue().getValue());
				
				dp.addPoint(newPointIndex(pt));
				
				dp.setColor(colors.get(i));
				dps.add(dp);
			}
		}
		addLineAxes();
	}
	// make interval polygon
	private void makeIntervalPlot()
	{
		dps.clear();
		points.clear();
		for(int i = 0; i < nodes.size(); i++)
		{
			ArrayList<Parameter> parameters = new ArrayList<Parameter>();
			
			for(int idx = 0; idx < nodes.get(i).length; idx++)
			{
				ArrayList<Parameter> tmp = (nodes.get(i))[idx].getParameters();
				for(int k = 0; k < tmp.size(); k++)
				{
					if(!parameters.contains(tmp.get(k)))
					{
						parameters.add(tmp.get(k));
					}
				}
			}
			ArrayList<Parameter> tmp = (lowers.get(i)).getParameters();
			for(int k = 0; k < tmp.size(); k++)
			{
				if(!parameters.contains(tmp.get(k)))
				{
					parameters.add(tmp.get(k));
				}
			}
			tmp = (uppers.get(i)).getParameters();
			for(int k = 0; k < tmp.size(); k++)
			{
				if(!parameters.contains(tmp.get(k)))
				{
					parameters.add(tmp.get(k));
				}
			}
						
			double[] parameterRatios = new double[parameters.size()];
			
			for(int j = 0; j < parameterRatios.length; j++)
			{
				parameterRatios[j] = 0;
			}

			ArrayList<ArrayList<Vector3D>> allPoints = new ArrayList<ArrayList<Vector3D>>();
			ArrayList<Vector3D> oneTimePoints = new ArrayList<Vector3D>();
			
			boolean allPlotted = false;
			
			while(!allPlotted){
				
				for(int j = 0; j < parameters.size(); j++)
				{
					(nodes.get(i))[0].setParameter(parameters.get(j), parameterRatios[j]);
					(nodes.get(i))[1].setParameter(parameters.get(j), parameterRatios[j]);
					(nodes.get(i))[2].setParameter(parameters.get(j), parameterRatios[j]);
					lowers.get(i).setParameter(parameters.get(j), parameterRatios[j]);
					uppers.get(i).setParameter(parameters.get(j), parameterRatios[j]);
				}
				
				oneTimePoints = new ArrayList<Vector3D>();
				
				for(double t = lowers.get(i).getValue().getValue(); t <= uppers.get(i).getValue().getValue()-intervals.get(i); t += intervals.get(i))
				{
					// add data f(t)
					(nodes.get(i))[0].substitute(t);
					(nodes.get(i))[1].substitute(t);
					(nodes.get(i))[2].substitute(t);
					
					if(maxX<nodes.get(i)[0].getValue().getValue()) maxX = nodes.get(i)[0].getValue().getValue();
					if(minX>nodes.get(i)[0].getValue().getValue()) minX = nodes.get(i)[0].getValue().getValue();
					if(maxY<nodes.get(i)[1].getValue().getValue()) maxY = nodes.get(i)[1].getValue().getValue();
					if(minY>nodes.get(i)[1].getValue().getValue()) minY = nodes.get(i)[1].getValue().getValue();

					Vector3D pt = new Vector3D(nodes.get(i)[0].getValue().getValue(),
												nodes.get(i)[1].getValue().getValue(),
												nodes.get(i)[2].getValue().getValue());

					oneTimePoints.add(pt);
				}
				
				
				if(parameterRatios.length == 0)
				{
					for(int t = 0; t < oneTimePoints.size()-1; t++)
					{
						DrawPoints dp = new DrawPoints();
						dp.setColor(colors.get(i));
						dp.addPoint(newPointIndex(oneTimePoints.get(t)));
						dp.addPoint(newPointIndex(oneTimePoints.get(t+1)));
						dps.add(dp);
					}
					allPlotted = true;
				}
				else
				{
					allPoints.add(oneTimePoints);
				}
				
				for(int j = 0; j < parameterRatios.length; j++)
				{
					if(parameterRatios[j]+parameterIntervals.get(i) < 1)
					{
						parameterRatios[j] += parameterIntervals.get(i);
						break;
					}
					else
					{
						parameterRatios[j] = 0;
						if(j == parameterRatios.length-1) allPlotted = true;
					}
				}
			}
			
			int maxT = 0;
			for(int p = 0; p < allPoints.size(); p++)
			{
				if(maxT<allPoints.get(p).size()) maxT = allPoints.get(p).size();
			}
			for(int t = 0; t < maxT; t++)
			{
				DrawPoints dp = new DrawPoints();
				for(int p = 0; p < allPoints.size(); p++)
				{
					if(allPoints.get(p).size() <= t+1)
					{
						dp.addPoint(newPointIndex(allPoints.get(p).get(allPoints.get(p).size()-1)));
					}
					else
					{
						dp.addPoint(newPointIndex(allPoints.get(p).get(t)));
						dp.addPoint(newPointIndex(allPoints.get(p).get(t+1)));
					}
				}
				dp.setColor(colors.get(i));
				dps.add(dp);
			}
			
		}
		addThickAxes();
	}
	// make several lines
	private void makeSeveralLinesPlot()
	{
		dps.clear();
		points.clear();
		for(int i = 0; i < nodes.size(); i++)
		{
			ArrayList<Parameter> parameters = new ArrayList<Parameter>();
			
			for(int idx = 0; idx < nodes.get(i).length; idx++)
			{
				ArrayList<Parameter> tmp = (nodes.get(i))[idx].getParameters();
				for(int k = 0; k < tmp.size(); k++)
				{
					if(!parameters.contains(tmp.get(k)))
					{
						parameters.add(tmp.get(k));
					}
				}
			}
			
			ArrayList<Parameter> tmp = (lowers.get(i)).getParameters();
			for(int k = 0; k < tmp.size(); k++)
			{
				if(!parameters.contains(tmp.get(k)))
				{
					parameters.add(tmp.get(k));
				}
			}
			
			tmp = (uppers.get(i)).getParameters();
			for(int k = 0; k < tmp.size(); k++)
			{
				if(!parameters.contains(tmp.get(k)))
				{
					parameters.add(tmp.get(k));
				}
			}
			
			double[] parameterRatios = new double[parameters.size()];
			
			for(int j = 0; j < parameterRatios.length; j++)
			{
				parameterRatios[j] = 0;
			}
			
			boolean allPlotted = false;
			
			while(!allPlotted){
				
				for(int j = 0; j < parameters.size(); j++)
				{
					(nodes.get(i))[0].setParameter(parameters.get(j), parameterRatios[j]);
					(nodes.get(i))[1].setParameter(parameters.get(j), parameterRatios[j]);
					(nodes.get(i))[2].setParameter(parameters.get(j), parameterRatios[j]);
					(lowers.get(i)).setParameter(parameters.get(j), parameterRatios[j]);
					(uppers.get(i)).setParameter(parameters.get(j), parameterRatios[j]);
				}
				
				for(double t = lowers.get(i).getValue().getValue(); t <= uppers.get(i).getValue().getValue()-intervals.get(i); t += intervals.get(i))
				{
					DrawPoints dp = new DrawPoints();
					
					// add data f(t)
					(nodes.get(i))[0].substitute(t);
					(nodes.get(i))[1].substitute(t);
					(nodes.get(i))[2].substitute(t);
					
					if(maxX<nodes.get(i)[0].getValue().getValue()) maxX = nodes.get(i)[0].getValue().getValue();
					if(minX>nodes.get(i)[0].getValue().getValue()) minX = nodes.get(i)[0].getValue().getValue();
					if(maxY<nodes.get(i)[1].getValue().getValue()) maxY = nodes.get(i)[1].getValue().getValue();
					if(minY>nodes.get(i)[1].getValue().getValue()) minY = nodes.get(i)[1].getValue().getValue();

					Vector3D pt = new Vector3D(nodes.get(i)[0].getValue().getValue(),
												nodes.get(i)[1].getValue().getValue(),
												nodes.get(i)[2].getValue().getValue());
					
					dp.addPoint(newPointIndex(pt));

					// add data f(t+interval)
					(nodes.get(i))[0].substitute(t+intervals.get(i));
					(nodes.get(i))[1].substitute(t+intervals.get(i));
					(nodes.get(i))[2].substitute(t+intervals.get(i));

					if(maxX<nodes.get(i)[0].getValue().getValue()) maxX = nodes.get(i)[0].getValue().getValue();
					if(minX>nodes.get(i)[0].getValue().getValue()) minX = nodes.get(i)[0].getValue().getValue();
					if(maxY<nodes.get(i)[1].getValue().getValue()) maxY = nodes.get(i)[1].getValue().getValue();
					if(minY>nodes.get(i)[1].getValue().getValue()) minY = nodes.get(i)[1].getValue().getValue();

					pt = new Vector3D(nodes.get(i)[0].getValue().getValue(),
												nodes.get(i)[1].getValue().getValue(),
												nodes.get(i)[2].getValue().getValue());
					
					dp.addPoint(newPointIndex(pt));
					
					dp.setColor(colors.get(i));
					dps.add(dp);
				}
				
				if(parameterRatios.length==0) allPlotted = true;
				
				for(int j = 0; j < parameterRatios.length; j++)
				{
					if(parameterRatios[j] < 1)
					{
						parameterRatios[j] += parameterIntervals.get(i);
						break;
					}
					else
					{
						parameterRatios[j] = 0;
						if(j == parameterRatios.length-1) allPlotted = true;
					}
				}
			}
		}
		
		addLineAxes();
	}
	// make plot
	private void makePlot()
	{
		if(intervalMode) makeIntervalPlot();
		else if(severalLines) makeSeveralLinesPlot();
		else makeSingleLinePlot();
	}
	// paint
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
				fillPolygons(g);
			}
			else
			{
				drawLines(g);
			}
			drawAxisValues(g);
		}
	}
	
	
	
	/********+*****************************
	 ************ Mouse Events ************
	 **************************************/
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {
		if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
		{
			Vector3D diff = new Vector3D(e.getX(),-e.getY(),0);
			diff = Vector3D.sub(diff,clickPoint);
			
			double r = (width+height);
			
			double z = Math.sqrt(
						r*r
						- diff.get(0)*diff.get(0)
						- diff.get(1)*diff.get(1)
						);
			
			diff.set(2,z);
			
			draggingQuaternion = Quaternion.createQuaternion(new Vector3D(0,0,r),diff);
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
			clickPoint = new Vector3D(e.getX(),-e.getY(),0);
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

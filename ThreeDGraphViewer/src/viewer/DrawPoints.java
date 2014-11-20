package viewer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import common.Vector3D;

public class DrawPoints{
	private Color color;
	private ArrayList<Vector3D> points = new ArrayList<Vector3D>();
	public void addPoint(Vector3D point)
	{
		points.add(point);
	}
	public void reset()
	{
		points.clear();
	}
	public void setColor(Color c)
	{
		color = c;
	}
	public Color getColor()
	{
		return color;
	}
	public Vector3D getPoint(int i)
	{
		return points.get(i);
	}
	public void setPoint(int i, Vector3D v)
	{
		points.set(i, v);
	}
	public ArrayList<Vector3D> getPoints()
	{
		return points;
	}
	public Vector3D getMedian()
	{
		double maxX=-Double.MAX_VALUE, minX=Double.MAX_VALUE;
		double maxY=-Double.MAX_VALUE, minY=Double.MAX_VALUE;
		double maxZ=-Double.MAX_VALUE, minZ=Double.MAX_VALUE;
		for(Vector3D point : points)
		{
			if(minX>point.at(0)) minX = point.at(0);
			if(maxX<point.at(0)) maxX = point.at(0);
			if(minY>point.at(1)) minY = point.at(1);
			if(maxY<point.at(1)) maxY = point.at(1);
			if(minZ>point.at(2)) minZ = point.at(2);
			if(maxZ<point.at(2)) maxZ = point.at(2);
		}
		return new Vector3D((maxX+minX)/2.0,
							(maxY+minY)/2.0,
							(maxZ+minZ)/2.0);
	}
	public Vector3D getAverage()
	{
		Vector3D vec = new Vector3D(0,0,0);
		for(Vector3D point : points)
		{
			vec = Vector3D.add(vec,point);
		}
		return Vector3D.mul(1.0/points.size(),vec);
	}
}

package viewer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import common.Vector3D;

public class DrawPoints{
	private Color color;
	private ArrayList<Integer> points = new ArrayList<Integer>();
	public void addPoint(int point)
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
	public int getPoint(int i)
	{
		return points.get(i);
	}
	public void setPoint(int i, int v)
	{
		points.set(i, v);
	}
	public ArrayList<Integer> getPoints()
	{
		return points;
	}
	public String toString()
	{
		String ret = "";
		for(int i = 0; i < points.size(); i++)
		{
			ret += points.get(i) + " : ";
		}
		return ret;
	}
}

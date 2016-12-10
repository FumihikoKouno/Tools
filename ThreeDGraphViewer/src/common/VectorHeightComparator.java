package common;

import java.util.Comparator;

import viewer.DrawPoints;


public class VectorHeightComparator implements Comparator<Vector3D>
{
	
	public int compare(Vector3D v1, Vector3D v2)
	{
		if(v1.at(1)>v2.at(1))
		{
			return 1;
		}
		else if(v1.at(1)==v2.at(1))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}

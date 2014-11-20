package viewer;

import java.util.Comparator;


public class DepthComparator implements Comparator<DrawPoints>
{
	public int compare(DrawPoints p1, DrawPoints p2)
	{
		//return (int)(p1.getMedian().at(2)-p2.getMedian().at(2));
		return (int)(p1.getAverage().at(2)-p2.getAverage().at(2));
	}
}

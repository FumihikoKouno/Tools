import java.util.Comparator;

public class DateComparator implements Comparator<TimePeriod>{

	@Override
	public int compare(TimePeriod o1, TimePeriod o2) {
		return (int)(o1.getStartTime().getTime() - o2.getStartTime().getTime());
	}
}

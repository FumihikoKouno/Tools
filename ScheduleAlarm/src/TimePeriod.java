import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class for one time period
 * @author Fumihiko
 *
 */
public class TimePeriod {
	private Date startTime;
	private Date stopTime;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
	private static final String prefixDate = "[";
	private static final String postfixDate = "] ";
	private static final String interfixDate = " - ";

	public TimePeriod(){
		start();
	}
	public TimePeriod(Date start, Date stop){
		this.startTime = start;
		this.stopTime = stop;
	}
	public void setStartTime(Date date){
		this.startTime = date;
	}
	public Date getStartTime(){
		return startTime;
	}
	public void setStopTime(Date date){
		this.stopTime = date;
	}
	public Date getStopTime(){
		return stopTime;
	}
	public String getStartTimeString(){
		return sdf.format(startTime);
	}
	public String getStopTimeString(){
		if(stopTime == null){
			return null;
		}
		return sdf.format(stopTime);
	}
	public long getMilliSeconds(){
		if(stopTime == null){
			return new Date().getTime() - startTime.getTime();
		}else{
			return stopTime.getTime() - startTime.getTime();
		}
	}
	public void setStartTime(long time){
		startTime = new Date();
		startTime.setTime(time);
	}
	public void setStopTime(long time){
		stopTime = new Date();
		stopTime.setTime(time);
	}
	public void start(){
		startTime = new Date();
	}
	public void stop(){
		stopTime = new Date();
	}
	public static TimePeriod fromString(String str){
		String startStr = 
				str.substring(
						str.indexOf(TimePeriod.prefixDate) + TimePeriod.prefixDate.length(),
						str.indexOf(TimePeriod.interfixDate)
				);
		String stopStr = 
				str.substring(
						str.indexOf(TimePeriod.interfixDate) + TimePeriod.interfixDate.length(),
						str.indexOf(TimePeriod.postfixDate)
				);
		try {
			Date start = TimePeriod.sdf.parse(startStr);
			Date stop = TimePeriod.sdf.parse(stopStr);
			return new TimePeriod(start, stop);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String toString(){
		long time = this.getMilliSeconds();
		long second = time / 1000;
		long minute = second / 60;
		second = second % 60;
		long hour = minute / 60;
		minute = minute % 60;
		return TimePeriod.prefixDate + 
				this.getStartTimeString() + 
				TimePeriod.interfixDate + 
				this.getStopTimeString() + 
				TimePeriod.postfixDate + 
				hour + "h " + 
				minute + "m " + 
				second + "s";
	}
}

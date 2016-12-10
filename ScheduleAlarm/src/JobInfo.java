import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JobInfo {
	private String name;
	private List<TimePeriod> times = new ArrayList<TimePeriod>();
	private Map<Date, String> comments = new HashMap<Date, String>();
	private JobProgress progress;
	private TimePeriod currentTime;	
	public JobInfo(String name){
		this.name = name;
		this.currentTime = new TimePeriod();
	}
	
	public JobInfo(String name, List<TimePeriod> times){
		this(name);
		this.times = times;
	}
	
	public void stop(){
		this.currentTime.stop();
		times.add(currentTime);
	}
	
	public void start(){
		this.currentTime = new TimePeriod();
	}
	
	public static JobInfo fromString(String name, List<String> timeStrs){
		List<TimePeriod> times = new ArrayList<TimePeriod>();
		for(String timeStr : timeStrs){
			times.add(TimePeriod.fromString(timeStr));
		}
		return new JobInfo(name, times);
	}
	
	public static JobInfo fromFile(File file){
		try {
			Scanner scanner = new Scanner(file);
			List<String> timeStrs = new ArrayList<String>();
			while(scanner.hasNext()){
				timeStrs.add(scanner.nextLine());
			}
			return fromString(file.getName(), timeStrs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File toFile(){
		File file = new File(this.name);
		file.delete();
		try{
			if(file.createNewFile()){
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(this.toString());
				writer.close();
			}
			return file;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public String toString(){
		TimePeriod[] timeArray = times.toArray(new TimePeriod[0]);
		Arrays.sort(timeArray, new DateComparator());
		String str = "";
		for (TimePeriod time : timeArray) {
			str = str + time + System.lineSeparator();
		}
		return str;
	}
}

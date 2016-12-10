import java.util.ArrayList;
import java.util.List;

public class JobList {
	private static List<JobInfo> jobs;
	public static void init(){
		JobList.jobs = new ArrayList<JobInfo>();
	}
	
	public static void addJob(JobInfo job){
		JobList.jobs.add(job);
	}
	
	public static void read(){
		
	}
	
}

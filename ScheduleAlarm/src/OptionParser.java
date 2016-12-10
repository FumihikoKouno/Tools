import java.util.HashMap;
import java.util.Map;

public class OptionParser {
	private static final String USAGE = 
			"Usage: java Frontend "
			+ "[--conf <config file>] "
			+ "[--data <data directory]";
	
	private String[] options;
	private Map<String, String> parsedOptions;
	public OptionParser(String[] args){
		this.options = args;
	}
	
	public void parse(){
		this.parsedOptions = new HashMap<>();
		try{
			for(int i = 0; i < this.options.length; i++){
				switch(this.options[i]){
				case "--conf":
					parsedOptions.put("config", this.options[i+1]);
				case "--data":
					parsedOptions.put("data", this.options[i+1]);
				default:
				}
			}
		}catch(IndexOutOfBoundsException e){
			System.err.println("");
		}
	}
	
	public Object getValue(String key){
		return this.parsedOptions.get(key);
	}
}

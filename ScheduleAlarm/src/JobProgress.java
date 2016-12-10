import java.text.NumberFormat;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class JobProgress {
	public enum Type{
		PAGE,
		ITEM,
		KLOC,
		PERCENT
	}
	private static final Map<Type, String> unit = new HashMap<Type, String>(){
		{
			put(Type.PAGE, "p");
			put(Type.ITEM, "");
			put(Type.KLOC, "kloc");
			put(Type.PERCENT, "%");
		}
	};
	private Type type;
	private double current;
	private double total;
	public JobProgress(Type type, double current, double total){
		this.type = type;
		this.current = current;
		this.total = total;
	}
	public JobProgress(double current){
		this.type = Type.PERCENT;
		this.current = current;
		this.total = 100;
	}

	public static String[] separateString(String str){
		int index = 0;
		for(;
			str.length() > index &&
			(str.charAt(index) == '.' ||
			(str.charAt(index) >= '0' && str.charAt(index) <= '9'));
			index++);
		String[] ret = {
			str.substring(0, index),
			str.substring(index, str.length())
		};
		return ret;
	}

	public static JobProgress fromString(String str) {
		String[] currentTotal = str.split("/");
		String[] currentUnit = separateString(currentTotal[0].trim());
		String[] totalUnit = separateString(currentTotal[1].trim());
		double current = Double.parseDouble(currentUnit[0]);
		double total = Double.parseDouble(totalUnit[0]);
		if(currentUnit[1] != totalUnit[1]){
			System.err.println("Unit format is not same.");
			return null;
		}
		String unit = currentUnit[1];
		Type type = Type.ITEM;
		if(unit.equals(Type.ITEM)){
			type = Type.ITEM;
		}else if(unit.equals(Type.KLOC)){
			type = Type.KLOC;
		}else if(unit.equals(Type.PAGE)){
			type = Type.PAGE;
		}else if(unit.equals(Type.PERCENT)){
			type = Type.PERCENT;
		}else{
			System.err.println("Unit format is invalid.");
			return null;
		}
		return new JobProgress(type, current, total);
	}
	
	public String toString(){
		String unit = this.unit.get(this.type);
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		String current = format.format(this.current);
		String total = format.format(this.total);
		return current + unit + " / " + total + unit;
	}
}
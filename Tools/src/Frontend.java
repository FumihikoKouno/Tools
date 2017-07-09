import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.JsonFormatter;

public class Frontend {

	public static void main(String[] args) {
		String json = 
				"{\n"+
				"  \"foo\": [ 1, null ],\n"+
				"  \"baz\": {\n"+
				"    \"foo\": [ true, \"bar\" ],\n"+
				"    \"baz\": \"qux\"\n"+
				"}";
		JsonFormatter jsonFormatter = new JsonFormatter(json);
		Map obj = (Map)jsonFormatter.parse();
		obj.put("foo", 3);
		List<Object> list = new ArrayList<Object>();
		list.add("fumihiko");
		list.add(6);
		list.add(14);
		obj.put("kono", list);
		JsonFormatter toStr = new JsonFormatter(obj);
		System.out.println(toStr);
	}

}

package utils;

import java.util.ArrayList;

import javax.swing.JTextArea;

public class Common {
	public static JTextArea console = new JTextArea();
	public static final int INDENT = 2;
	public static final String RESTAPI_FILENAME = "restapi.yml";

	public static void writeConsole(ArrayList<String> severalLineString){
		String outputString = "";
		for(String lineString : severalLineString){
			outputString += lineString + System.getProperty("line.separator");
		}
		console.setText(outputString);
	}

	public static void init(){
		console.setEditable(false);
	}
}

package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Lexer.Token;

public class JsonFormatter {
	private Lexer lexer;
	private LexerStatus lexerStatus;
	private Object parsedObject;
	private String INDENT_UNIT = "  ";

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


	public JsonFormatter(String[] json){
		String oneLine = "";
		for(String line : json){
			oneLine += line;
			oneLine += "\n";
		}
		lexer = new Lexer(removeRedundantChars(oneLine));
	}
	
	public JsonFormatter(Map<String, Object> object){
		parsedObject = object;
	}

	public JsonFormatter(List<Object> object){
		parsedObject = object;
	}
	
	public JsonFormatter(String json){
		lexer = new Lexer(removeRedundantChars(json));
	}
	
	public void outputLog(){
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//		System.out.println(ste[2].getMethodName() + ": " + ste[2].getLineNumber() + " - " + lexerStatus);
//		System.out.println(lexer);
	}
	
	public Object parse(){
		if((parsedObject = array()) != null){
			outputLog();
			return parsedObject;
		}
		if((parsedObject = object()) != null){
			outputLog();
			return parsedObject;
		}
		outputLog();
		return null;
	}

	public String removeRedundantChars(String str){
		String ret = "";
		int doubleQuotationCount = 0;
		for(int index = 0; index < str.length(); index++){
			char character = str.charAt(index);
			if(character == '"'){
				doubleQuotationCount++;
			}else{
				if(isRedundantChar(character) && 
						doubleQuotationCount % 2 == 0){
					continue;
				}
			}
			ret += character;
		}
		outputLog();
		return ret;
	}
	
	private boolean isRedundantChar(char character){
		return character == ' ' || 
				character == '\t' ||
				character == '\n';
	}
	
	private void start(){
		lexerStatus = lexer.getStatus();
	}
	
	private void failed(){
		lexer.setStatus(lexerStatus);
	}
	
	public String string(){
		start();
		String ret = null;
		if (lexer.getToken() == Token.DOUBLE_QUOTATION){
			ret = "";
			lexer.next();
			while(true){
				if (lexer.getToken() == Token.DOUBLE_QUOTATION){
					lexer.next();
					break;
				}
				ret += lexer.getTokenString();
				lexer.next();
			}
		}
		outputLog();
		return ret;
	}
	
	public Double number(){
		start();
		Double ret = null;
		if (lexer.getToken() == Token.NUMBER){
			ret = Double.parseDouble(lexer.getTokenString());
			lexer.next();
		}
		outputLog();
		return ret;
	}
	
	public Object value(){
		start();
		Object ret = null;
		if (lexer.getToken() == Token.IDENTIFIER){
			String tokenString = lexer.getTokenString();
			if(tokenString.equals("true")){
				lexer.next();
				outputLog();
				return true;
			}
			if(tokenString.equals("false")){
				lexer.next();
				outputLog();
				return false;
			}
			if(tokenString.equals("null")){
				lexer.next();
				outputLog();
				return "null";
			}
			failed();
			outputLog();
			return null;
		}
		if ((ret = string()) != null){
			outputLog();
			return ret;
		}
		if ((ret = number()) != null){
			outputLog();
			return ret;
		}
		if ((ret = object()) != null){
			outputLog();
			return ret;
		}
		if ((ret = array()) != null){
			outputLog();
			return ret;
		}
		failed();
		outputLog();
		return null;
	}
	
	public Map<String, Object> object(){
		start();
		Map<String, Object> ret = null;
		if(lexer.getToken() != Token.LEFT_BRACES){
			failed();
			outputLog();
			return null;
		}
		lexer.next();
		if(lexer.getToken() == Token.RIGHT_BRACES){
			lexer.next();
			outputLog();
			return new HashMap<String, Object>();
		}
		if((ret = members()) != null){
			if(lexer.getToken() == Token.RIGHT_BRACES){
				lexer.next();
				outputLog();
				return ret;
			}
		}
		failed();
		outputLog();
		return null;
	}
	
	public Map<String, Object> pair(){
		start();
		String key = null;
		Object value = null;
		if((key = string()) == null){
			failed();
			outputLog();
			return null;
		}
		if(lexer.getToken() != Token.COLON){
			failed();
			outputLog();
			return null;
		}
		lexer.next();
		if((value = value()) == null){
			failed();
			outputLog();
			return null;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put(key, value);
		outputLog();
		return ret;
	}
	
	public Map<String, Object> members(){
		start();
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> tmp = null;
		if((tmp = pair()) != null){
			for(String key : tmp.keySet()){
				ret.put(key, tmp.get(key));
			}
		}else{
			failed();
			outputLog();
			return null;
		}
		if(lexer.getToken() == Token.CAMMA){
			lexer.next();
			if((tmp = members()) != null){
				for(String key : tmp.keySet()){
					ret.put(key, tmp.get(key));
				}
			}else{
				failed();
				outputLog();
				return null;
			}
		}
		outputLog();
		return ret;
	}
	
	public List<Object> array(){
		start();
		List<Object> ret = null;
		if(lexer.getToken() != Token.LEFT_BRACKETS){
			failed();
			outputLog();
			return null;
		}
		lexer.next();
		if(lexer.getToken() == Token.RIGHT_BRACKETS){
			lexer.next();
			outputLog();
			return new ArrayList<Object>();
		}
		if((ret = elements()) != null){
			if(lexer.getToken() == Token.RIGHT_BRACKETS){
				lexer.next();
				outputLog();
				return ret;
			}
		}
		failed();
		return null;
	}
	
	public List<Object> elements(){
		start();
		List<Object> ret = new ArrayList<Object>();
		Object tmpValue = null;
		List<Object> tmpElement = null;
		if((tmpValue = value()) != null){
			ret.add(tmpValue);
		}else{
			failed();
			outputLog();
			return null;
		}
		if(lexer.getToken() == Token.CAMMA){
			lexer.next();
			if((tmpElement = elements()) != null){
				ret.addAll(tmpElement);
				return ret;
			}else{
				failed();
				outputLog();
				return null;
			}
		}else{
			outputLog();
			return ret;
		}
	}
	
	public String toString(Object object, int indent){
		String ret = "";
		String indentString = "";
		for(int i = 0; i < indent; i++){
			indentString += INDENT_UNIT;
		}
		if(object != null){
			if(object instanceof List){
				ret += indentString;
				ret += "[\n";
				for(Object element : (List<Object>)object){
					ret += toString(element, indent+1) + ",\n";
				}
				ret = ret.substring(0, ret.length()-2);
				ret += "\n" + indentString + "]";
				outputLog();
				return ret;
			}
			if(object instanceof Map){
				Map<Object, Object> map = (Map<Object, Object>)object;
				ret += indentString;
				ret += "{\n";
				for(Object element : map.keySet()){
					ret += indentString + INDENT_UNIT + toString(element, 0) + ": ";
					ret += toString(map.get(element), indent+1).substring((indentString+INDENT_UNIT).length()) + ",\n";
				}
				ret = ret.substring(0, ret.length()-2);
				ret += "\n" + indentString + "}";
				outputLog();
				return ret;
			}
			if(object instanceof String){
				outputLog();
				return indentString + "\"" + object.toString() + "\"";
			}
			outputLog();
			return indentString + object.toString();
		}
		outputLog();
		return "";
	}
	
	public String toString(){
		outputLog();
		return toString(parsedObject, 0);
	}
}

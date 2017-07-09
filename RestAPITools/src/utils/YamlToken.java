package utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class YamlToken {
	public enum TYPE{
		HASH,
		LIST,
		STRING,
	}
	
	private TYPE type;
	private HashMap<YamlToken, YamlToken> hash;
	private List<YamlToken> list;
	private String string;

	public YamlToken(HashMap<YamlToken, YamlToken> hash){
		type = TYPE.HASH;
		this.hash = hash;
	}
	
	public YamlToken(List list){
		type = TYPE.LIST;
		this.list = list;
	}
	
	public YamlToken(String string){
		type = TYPE.STRING;
		this.string = string;
	}
	
	public TYPE getType(){
		return this.type;
	}
	
	public HashMap<YamlToken, YamlToken> getHash(){
		return this.hash;
	}
	
	public List<YamlToken> getList(){
		return this.list;
	}

	public String getString(){
		return this.string;
	}
	
	private String indentSpaces(int indent){
		String string = "";
		for(int i = 0; i < indent; i++){
			string = string + " ";
		}
		return string;
	}
	
	private String getString(int indent, boolean inlist){
		String separator = System.getProperty("line.separator");
		boolean first = true;
		String ret = "";
		switch(this.type){
		case HASH:
			for(YamlToken key : this.hash.keySet()){
				YamlToken value = this.hash.get(key);
				if(first && inlist){
					first = false;
				}else{
					ret = ret + this.indentSpaces(indent);
				}
				ret = ret + key.getString(indent, false) + ": ";
				if(value.getType() == TYPE.STRING){
					ret = ret + value.getString(indent, false);	
					ret = ret + separator;
				}else{
					int nextIndent = indent + Common.INDENT;
					ret = ret + separator;
					ret = ret + value.getString(nextIndent, false);
				}
			}
			return ret;
		case LIST:
			for(YamlToken value : this.list){
				ret = ret + this.indentSpaces(indent);
				ret = ret + "- ";
				if(value.getType() == TYPE.STRING){
					ret = ret + value.getString(indent, false);
					ret = ret + separator;
				}else if(value.getType() == TYPE.HASH){
					int nextIndent = indent + Common.INDENT;
					ret = ret + value.getString(nextIndent, true);
				}else{
					int nextIndent = indent + Common.INDENT;
					ret = ret + separator;
					ret = ret + value.getString(nextIndent, false);
				}
			}
			return ret;
		case STRING:
			return this.string;
		default:
			return null;
		}
	}
	
	@Override
	public String toString(){
		return this.getString(0, false);
	}
	
}

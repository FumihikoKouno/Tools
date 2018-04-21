package paresr;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EBNFParser {
	private String bnfDefinitionString;
	
	private static String defStr = "(* see; 7.2 *) letter\n" +
							"= 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h'\n" +
							"| 'i' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p'\n" +
							"| 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x'\n" +
							"| 'y' | 'z'\n" +
							"| 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H'\n" +
							"| 'I' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | 'P'\n" +
							"| 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'X'\n" +
							"| 'Y' | 'Z';\n" +
							"(* see 7.2 *) decimal digit\n" +
							"= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7'\n" +
							"| '8' | '9';\n" +
							"(*\n" +
							"The representation of the following\n" +
							"terminal-characters is defined in clauses 7.3,\n" +
							"7.4 and tables 1, 2.\n" +
							"*)\n" +
							"concatenate symbol = ',';\n" +
							"defining symbol = '=';\n" +
							"definition separator symbol = '|' | '/' | '!';\n" +
							"end comment symbol = '*)';\n" +
							"end group symbol = ')';\n" +
							"end option symbol = ']' | '/)';\n" +
							"end repeat symbol = '}' | ':)', abc;\n" +
							"except symbol = '-';\n" +
							"first quote symbol = \"'\";\n" +
							"repetition symbol = '*';\n" +
							"second quote symbol = '\"';\n" +
							"special sequence symbol = '?';\n" +
							"start comment symbol = '(*';\n" +
							"start group symbol = '(';\n" +
							"start option symbol = '|||[' | '(/';\n" +
							"start repeat symbol = '{' | '(:';\n" +
							"terminator symbol = ';' | '.';\n" +
							"(* see 7.5 *) other character\n" +
							"= ' ' | 3121 * ':' | '+' | '_' | '%' | '@'\n" +
							"| '&' | '#' | '$' | '<' | '>' | '\\' | ';';\n" +
							"(* see 7.6 *) space character = ' ';";
	
	public EBNFParser(List<String> bnfDefinitions){
		this(bnfDefinitions.toArray(new String[]{}));
	}
	
	public EBNFParser(String[] bnfDefinitions){
		this.bnfDefinitionString = "";
		for(String bnfDefinition : bnfDefinitions){
			this.bnfDefinitionString = this.bnfDefinitionString + "\n" + bnfDefinition;
		}
		this.bnfDefinitionString = this.bnfDefinitionString.substring(1);
	}
	
	public EBNFParser(File file){
		this.bnfDefinitionString = "";
		try(Scanner scanner = new Scanner(file)){
			this.bnfDefinitionString = this.bnfDefinitionString + "\n" + scanner.nextLine();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		this.bnfDefinitionString = this.bnfDefinitionString.substring(1);
	}
	
	public EBNFParser(String bnfDefinition){
		this.bnfDefinitionString = bnfDefinition;
		
		this.bnfDefinitionString = removeComments(this.bnfDefinitionString);
		List<EBNFNode> nodes = parse();
		for(EBNFNode node : nodes){
			node.accept(new EBNFTreePrinter());
		}
	}
	
	public List<EBNFNode> parse(){
		List<EBNFNode> root = new ArrayList<EBNFNode>();
		
		List<String> lines = splitDefinitions(this.bnfDefinitionString, ";");
		for(String line : lines){
			int equalPos = line.indexOf("=");
			String key = line.substring(0, equalPos).trim();
			List<String> elements = splitDefinitions(
					line.substring(equalPos + 1).replace(System.lineSeparator(), ""), 
					"|");
			List<List<EBNFNode>> ebnfNodesList = new ArrayList<List<EBNFNode>>();
			for(String element : elements){
				List<EBNFNode> ebnfNodes = new ArrayList<EBNFNode>();
				for(String node : splitDefinitions(element, ",")){
					try{
						ebnfNodes.add(createEBNFNode(node));
					}catch(ParseException e){
						e.printStackTrace();
						return null;
					}
				}
				ebnfNodesList.add(ebnfNodes);
			}
			root.add(new EBNFDefinition(key, new EBNFOr(ebnfNodesList)));
		}
		
		return root;
	}
	
	public EBNFNode createEBNFNode(String node) throws ParseException{
		String nodeString = node.trim();
		char firstCharacter = nodeString.charAt(0);
		char lastCharacter = nodeString.charAt(nodeString.length() - 1);
		String content = nodeString.substring(1, nodeString.length() - 1);
		
		if(firstCharacter == '\'' || firstCharacter == '"'){
			if(lastCharacter != firstCharacter){
				throw new ParseException(String.valueOf(firstCharacter), nodeString.length());
			}
			return new EBNFFinish(content);
		}
		
		if(firstCharacter == '['){
			if(lastCharacter != ']'){
				throw new ParseException("]", nodeString.length());
			}
			return new EBNFOption(createEBNFNode(content));
		}
		
		if(firstCharacter == '{'){
			if(lastCharacter != '}'){
				throw new ParseException("]", nodeString.length());
			}
			return new EBNFRepeat(createEBNFNode(content));
		}

		if('0' <= firstCharacter && firstCharacter <= '9'){
			int opePos = nodeString.indexOf("*");
			if(opePos < 0){
				throw new ParseException("*", nodeString.length() - 1);
			}
			int count = 0;
			try{
				count = Integer.parseInt(nodeString.substring(0, opePos).trim());
			}catch(NumberFormatException e){
				throw new ParseException("Number", opePos - 1);
			}
			return new EBNFTimes(count, createEBNFNode(nodeString.substring(opePos + 1).trim()));
		}
		
		int minusPos = nodeString.indexOf("-");
		if(minusPos >= 0){
			EBNFDefinitionCall definition = new EBNFDefinitionCall(nodeString.substring(0, minusPos).trim());
			try{
				return new EBNFExcept(definition,
						createEBNFNode(nodeString.substring(minusPos + 1)));
			}catch(IndexOutOfBoundsException e){
				return definition;
			}
		}
		
		return new EBNFDefinitionCall(nodeString);
	}
	
	public String removeComments(String bnfString){
		String result = "";
		for(int index = 0; index < bnfString.length(); index++){
			String character = bnfString.substring(index, index+1);
			// Skip symbol
			if(character.equals("\"") || character.equals("'")){
				int endPos = bnfString.substring(index + 1).indexOf(character) + index + 1;
				result = result + bnfString.substring(index, endPos + 1);
				index = endPos;
				continue;
			}

			// Skip comment
			if(character.equals("(")){
				if(bnfString.charAt(index+1) == '*'){
					index = bnfString.substring(index + 1).indexOf("*)") + index + 2;
					continue;
				}
			}
			result = result + character;
		}
		return result;
	}
	
	public List<String> splitDefinitions(String bnfString, String separator){
		List<String> result = new ArrayList<String>();
		int lineStart = 0;
		for(int index = 0; index < bnfString.length(); index++){
			String character = bnfString.substring(index, index+1);
			// Skip symbol
			if(character.equals("\"") || character.equals("'")){
				index = bnfString.substring(index + 1).indexOf(character) + index + 1;
				continue;
			}

			// Skip comment
			if(character.equals("(")){
				if(bnfString.charAt(index+1) == '*'){
					index = bnfString.substring(index + 1).indexOf("*)") + index + 2;
					continue;
				}
			}

			// Separate line
			if(character.equals(separator)){
				result.add(bnfString.substring(lineStart, index));
				lineStart = index + 1;
			}
		}
		if(lineStart < bnfString.length()){
			result.add(bnfString.substring(lineStart));
		}
		return result;
	}
	
	public static void main(String[] args){
		new EBNFParser(defStr);
		
	}
}

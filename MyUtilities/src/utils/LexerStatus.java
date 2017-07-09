package utils;

public class LexerStatus {
	public String currentTokenString;
	public Lexer.Token currentToken;
	public int index;
	
	public String toString(){
		try{
			return "" + index + ": (" + currentToken.name() + ", " + currentTokenString + ")";
		}catch(Exception e){
		}
		return "";
	}
}

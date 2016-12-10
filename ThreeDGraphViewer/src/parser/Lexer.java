package parser;

public class Lexer {
	private String expression;
	private int index;
	private int current;
	private String str;
	public enum Token{
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		POW,
		LEFT_PARENTHESIS,
		RIGHT_PARENTHESIS,
		LEFT_BRACKETS,
		RIGHT_BRACKETS,
		LEFT_BRACES,
		RIGHT_BRACES,
		COMMA,
		COLON,
		DOT,
		IDENTIFIER,
		NUM,
		UNKNOWN,
		FINISH,
	}
	public Lexer(String expression)
	{
		this.expression = expression;
		eliminateSpace();
		index = 0;
	}
	
	public Lexer() {
		index = 0;
	}
	
	public void eliminateSpace()
	{
		String eliminated = "";
		for(int i = 0; i < expression.length(); i++)
		{
			if(expression.charAt(i) != ' '
			&& expression.charAt(i) != '\n'
			&& expression.charAt(i) != '\t'
			&& expression.charAt(i) != '\r')
			{
				eliminated = eliminated + expression.charAt(i);
			}
		}
		expression = eliminated;
	}

	public void setString(String str)
	{
		expression = str;
		eliminateSpace();
	}
	
	public boolean finished()
	{
		if(expression.length()<=index) return true;
		return false;
	}

	public boolean finishRead()
	{
		if(expression.length()<=current) return true;
		return false;
	}
	
	public int getPosition()
	{
		return index;
	}
	
	public void setPosition(int i)
	{
		index = i;
	}
	
	private boolean isNumber(char c)
	{
		return '0' <= c && c <= '9';
	}
	
	private boolean isAlphabet(char c)
	{
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
	}
	
	public String getString()
	{
		return str;
	}
	
	public Token getToken()
	{
		current = index;
		if(finished()) return Token.FINISH;
		if(isNumber(expression.charAt(current)))
		{
			boolean dotAppeared = false;
			while(!finishRead() && 
				(  isNumber(expression.charAt(current))
				|| expression.charAt(current)=='.'))
			{
				if(expression.charAt(current)=='.')
				{
					if(dotAppeared)
					{
						break;
					}
					else
					{
						dotAppeared=true;
					}
				}
				current++;
			}
			str = expression.substring(index,current);
			index = current;
			return Token.NUM;
		}
		if(isAlphabet(expression.charAt(current)))
		{
			while(!finishRead() &&
				( isAlphabet(expression.charAt(current))
				|| isNumber(expression.charAt(current))
				|| expression.charAt(current)=='_'))
			{
				current++;
			}
			str = expression.substring(index,current);
			index = current;
			return Token.IDENTIFIER;
		}
		current++;
		str = expression.substring(index,current);
		char ch = expression.charAt(index);
		index = current;
		switch(ch)
		{
		case '+':
			return Token.PLUS;
		case '-':
			return Token.MINUS;
		case '*':
			return Token.TIMES;
		case '/':
			return Token.DIVIDE;
		case '^':
			return Token.POW;
		case '(':
			return Token.LEFT_PARENTHESIS;
		case ')':
			return Token.RIGHT_PARENTHESIS;
		case '{':
			return Token.LEFT_BRACES;
		case '}':
			return Token.RIGHT_BRACES;
		case '[':
			return Token.LEFT_BRACKETS;
		case ']':
			return Token.RIGHT_BRACKETS;
		case ',':
			return Token.COMMA;
		case ':':
			return Token.COLON;
		case '.':
			return Token.DOT;
		}
		return Token.UNKNOWN;
	}
}

package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Lexer {
	public enum Token {
		IDENTIFIER, COLON, SEMI_COLON, CAMMA, DOT, NUMBER, LEFT_PARENTHESES, RIGHT_PARENTHESES, LEFT_BRACKETS, RIGHT_BRACKETS, LEFT_BRACES, RIGHT_BRACES, LEFT_ANGLE_BRACKETS, RIGHT_ANGLE_BRACKETS, SINGLE_QUOTATION, DOUBLE_QUOTATION, LINE_BREAK, SPACE, EQUAL, PLUS, HYPHEN, ASTERISK, AMPERSAND, EXCLAMATION, DOLLAR, CARET, TILDE, VERTICAL_BAR, AT, QUESTION, SLASH, PERCENT, NUMERICAL, TAB, BACK_SLASH, BACK_QUOTATION, UNDERSCORE,
	}

	private String src;
	private int index;
	private Token currentToken;
	private String currentTokenString;
	private static final char NEW_LINE_CODE = '\n';
	private static final char TAB_CODE = '\t';

	public Lexer(File file) throws FileNotFoundException {
		try (Scanner scanner = new Scanner(file)) {
			src = scanner.nextLine();
			while (scanner.hasNextLine()) {
				src += NEW_LINE_CODE;
				src += scanner.nextLine();
			}
			currentToken = null;
			index = 0;
		} catch (FileNotFoundException e) {
			throw e;
		}
	}

	public Lexer(List<String> strs) {
		for (String str : strs) {
			if (src == null) {
				src = "";
			} else {
				src += NEW_LINE_CODE;
			}
			src += str;
		}
		currentToken = null;
		index = 0;
	}

	public Lexer(String str) {
		src = str;
		currentToken = null;
		index = 0;
	}

	public void next() {
		currentTokenString = "";
		currentToken = null;
		getToken();
	}

	public Token getToken() {
		if (currentToken != null) {
			return currentToken;
		}
		Token token = null;
		if (((token = identifier()) != null) || ((token = number()) != null) || ((token = symbol()) != null)) {
			currentToken = token;
			return token;
		}
		return null;
	}

	public Token symbol() {
		Token ret = null;
		currentTokenString = Character.toString(getChar());
		switch (getChar()) {
		case '!':
			ret = Token.EXCLAMATION;
			break;
		case '"':
			ret = Token.DOUBLE_QUOTATION;
			break;
		case '#':
			ret = Token.NUMERICAL;
			break;
		case '$':
			ret = Token.DOLLAR;
			break;
		case '%':
			ret = Token.PERCENT;
			break;
		case '&':
			ret = Token.AMPERSAND;
			break;
		case '\'':
			ret = Token.SINGLE_QUOTATION;
			break;
		case '(':
			ret = Token.LEFT_PARENTHESES;
			break;
		case ')':
			ret = Token.RIGHT_PARENTHESES;
			break;
		case '=':
			ret = Token.EQUAL;
			break;
		case '-':
			ret = Token.HYPHEN;
			break;
		case '~':
			ret = Token.TILDE;
			break;
		case '^':
			ret = Token.CARET;
			break;
		case '|':
			ret = Token.VERTICAL_BAR;
			break;
		case '\\':
			ret = Token.BACK_SLASH;
			break;
		case '`':
			ret = Token.BACK_QUOTATION;
			break;
		case '@':
			ret = Token.AT;
			break;
		case '{':
			ret = Token.LEFT_BRACES;
			break;
		case '[':
			ret = Token.LEFT_BRACKETS;
			break;
		case '+':
			ret = Token.PLUS;
			break;
		case ';':
			ret = Token.SEMI_COLON;
			break;
		case '*':
			ret = Token.ASTERISK;
			break;
		case ':':
			ret = Token.COLON;
			break;
		case '}':
			ret = Token.RIGHT_BRACES;
			break;
		case ']':
			ret = Token.RIGHT_BRACKETS;
			break;
		case '<':
			ret = Token.LEFT_ANGLE_BRACKETS;
			break;
		case ',':
			ret = Token.CAMMA;
			break;
		case '>':
			ret = Token.RIGHT_ANGLE_BRACKETS;
			break;
		case '.':
			ret = Token.DOT;
			break;
		case '?':
			ret = Token.QUESTION;
			break;
		case '/':
			ret = Token.SLASH;
			break;
		case '_':
			ret = Token.UNDERSCORE;
			break;
		case TAB_CODE:
			ret = Token.TAB;
			break;
		case NEW_LINE_CODE:
			ret = Token.LINE_BREAK;
			break;
		case ' ':
			ret = Token.SPACE;
			break;
		default:
			ret = null;
			break;
		}
		if (ret != null && hasNext()) {
			nextChar();
		}
		return ret;
	}

	private Token number() {
		currentTokenString = "";
		boolean dot_appeared = false;
		if (Character.isDigit(getChar())) {
			while (Character.isDigit(getChar()) || (getChar() == '.' && !dot_appeared)) {
				currentTokenString += getChar();
				if (hasNext()) {
					nextChar();
				} else {
					break;
				}
			}
		}
		return currentTokenString.equals("") ? null : Token.NUMBER;
	}

	public Token identifier() {
		currentTokenString = "";
		if (Character.isAlphabetic(getChar()) || getChar() == '_') {
			while (Character.isAlphabetic(getChar()) || getChar() == '_' || Character.isDigit(getChar())) {
				currentTokenString += getChar();
				if (hasNext()) {
					nextChar();
				} else {
					break;
				}
			}
		}
		return currentTokenString.equals("") ? null : Token.IDENTIFIER;
	}

	public String getTokenString() {
		return currentTokenString;
	}

	private char getChar() {
		return src.charAt(index);
	}

	public boolean hasNext() {
		return index < src.length() - 1;
	}

	public boolean ended() {
		return index == src.length() - 1 && currentToken != null;
	}

	private void nextChar() {
		index++;
	}
}
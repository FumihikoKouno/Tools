package utils;

import java.util.ArrayList;
import java.util.List;

public class TokenFinder {
	List<String> tokens;
	String str;

	public TokenFinder(String str) {
		this.str = str;
		tokens = new ArrayList<String>();
		Lexer lexer = new Lexer(str);
		while (!lexer.ended()) {
			lexer.getToken();
			tokens.add(lexer.getTokenString());
			lexer.next();
		}
	}

	public TokenFinder(List<Lexer.Token> tokens) {
		str = "";
		for (Lexer.Token token : tokens) {
			this.tokens.add(token.toString());
			str = str + token.toString();
		}
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public int indexOf(String str) {
		int index = str.indexOf(this.str);
		if (index >= 0) {
			Lexer lexer = new Lexer(str);
			List<String> tokenStrings = new ArrayList<String>();
			while (!lexer.ended()) {
				lexer.next();
				tokenStrings.add(lexer.getTokenString());
			}
			for (int i = 0; i < tokenStrings.size(); i++) {
				boolean matched = true;
				for (int j = 0; j < tokens.size(); j++) {
					if (!tokenStrings.get(i + j).equals(tokens.get(j))) {
						matched = false;
						break;
					}
				}
				if (matched) {
					int matchedColumn = 0;
					for (int j = 0; j < i; j++) {
						matchedColumn += tokenStrings.get(j).length();
					}
					return matchedColumn;
				}
			}
			return -1;
		} else {
			return -1;
		}
	}
}
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class YamlParser {
	private int row = 0;
	private int previousRow = 0;
	private int col = 0;
	private int previousCol = 0;
	private int indent = 0;
	private YamlToken result;
	private boolean inlist = false;
	private ArrayList<String> yamlStrings = new ArrayList<String>();

	public YamlParser(String fileName) {
		try (Scanner fileScanner = new Scanner(new File(fileName))) {
			while (fileScanner.hasNext()) {
				String line = fileScanner.nextLine();
				int finalChar = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) != ' ') {
						finalChar = i;
					}
				}
				if (finalChar > 0) {
					line = line.substring(0, finalChar + 1);
					if (line.length() > 0) {
						yamlStrings.add(line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			Common.console.setText("[Error] File " + fileName + " is not found.");
		}
	}

	public YamlToken getResult() {
		this.init();
		return this.token();
	}

	private void init() {
		this.row = 0;
		this.previousRow = 0;
		this.col = 0;
		this.previousCol = 0;
		this.indent = 0;
		this.inlist = false;
		this.result = null;
	}

	private char getChar() {
		return this.yamlStrings.get(this.row).charAt(this.col);
	}

	private boolean hasNextChar() {
		if (this.col < this.yamlStrings.get(this.row).length() - 1) {
			return true;
		}
		return false;
	}

	private boolean nextChar() {
		if (this.col < this.yamlStrings.get(this.row).length() - 1) {
			this.col++;
			return true;
		}
		return false;
	}

	private boolean hasNextLine() {
		if (this.row < this.yamlStrings.size() - 1) {
			return true;
		}
		return false;
	}

	private boolean nextLine() {
		if (this.row < this.yamlStrings.size() - 1) {
			this.row++;
			this.col = 0;
			return true;
		}
		return false;
	}

	private YamlToken token() {
		YamlToken token;
		token = this.list();
		if (token != null) {
			return token;
		}
		token = this.hash();
		if (token != null) {
			return token;
		}
		return null;
	}

	private boolean parseEnded() {
		return !this.hasNextChar() && !this.hasNextLine();
	}

	private YamlToken hash() {
		HashMap<YamlToken, YamlToken> hash = new HashMap<YamlToken, YamlToken>();
		previousRow = this.row;
		previousCol = this.col;
		YamlToken key;
		YamlToken value;
		boolean first = true;
		while (true) {
			if (this.parseEnded()) {
				if (hash.size() > 0) {
					this.col = 0;
					return new YamlToken(hash);
				}
				this.row = previousRow;
				this.col = previousCol;
				return null;
			}
			int skipCount = this.skipSpace();
			if (this.indent != skipCount) {
				if (this.inlist && first) {
					first = false;
					if (this.getChar() != '-' || skipCount != this.indent - Common.INDENT) {
						this.row = previousRow;
						this.col = previousCol;
						return null;
					}
					if (!this.nextChar()) {
						this.row = previousRow;
						this.col = previousCol;
						return null;
					}
					if (this.skipSpace() == 0) {
						this.row = previousRow;
						this.col = previousCol;
						return null;
					}
				} else {
					if (hash.size() > 0) {
						this.col = 0;
						return new YamlToken(hash);
					}
					this.row = previousRow;
					this.col = previousCol;
					return null;
				}
			}
			key = this.string();
			if (key == null) {
				this.row = previousRow;
				this.col = previousCol;
				return null;
			}
			if (this.getChar() == ':') {
				this.nextChar();
				skipCount = this.skipSpace();
				if (skipCount > 0) {
					value = this.string();
					if (value != null) {
						this.skipSpace();
						if (this.hasNextChar()) {
							this.row = previousRow;
							this.col = previousCol;
							return null;
						}
						hash.put(key, value);
						if (!nextLine()) {
							return new YamlToken(hash);
						}
					}
				} else {
					if (!this.nextLine()) {
						this.row = previousRow;
						this.col = previousCol;
						return null;
					}
					this.indent += Common.INDENT;
					value = this.token();
					this.indent -= Common.INDENT;
					if (value == null) {
						this.row = previousRow;
						this.col = previousCol;
						return null;
					}
					hash.put(key, value);
				}
			}
		}
	}

	private YamlToken list() {
		ArrayList<YamlToken> list = new ArrayList<YamlToken>();
		previousRow = this.row;
		previousCol = this.col;
		YamlToken value;
		while (true) {
			if (this.parseEnded()) {
				if (list.size() > 0) {
					this.col = 0;
					return new YamlToken(list);
				}
				this.row = previousRow;
				this.col = previousCol;
				return null;
			}
			if (this.indent != this.skipSpace()) {
				if (list.size() > 0) {
					this.col = 0;
					return new YamlToken(list);
				}
				this.row = previousRow;
				this.col = previousCol;
				return null;
			}
			if (this.getChar() != '-') {
				if (list.size() > 0) {
					this.col = 0;
					return new YamlToken(list);
				}
				this.row = previousRow;
				this.col = previousCol;
				return null;
			}
			this.nextChar();
			int skipCount = this.skipSpace();
			if (skipCount > 0) {
				this.indent += Common.INDENT;
				this.inlist = true;
				int tmpCol = this.col;
				this.col = 0;
				value = this.hash();
				this.inlist = false;
				this.indent -= Common.INDENT;
				if (value != null) {
					list.add(value);
				} else {
					this.col = tmpCol;
					value = this.string();
					if (value != null) {
						this.skipSpace();
						if (this.hasNextChar()) {
							this.row = previousRow;
							this.col = previousCol;
							return null;
						}
						list.add(value);
						if (!nextLine()) {
							return new YamlToken(list);
						}
					}
				}
			} else {
				if (!this.nextLine()) {
					this.row = previousRow;
					this.col = previousCol;
					return null;
				}
				this.indent += Common.INDENT;
				value = this.token();
				this.indent -= Common.INDENT;
				if (value == null) {
					this.row = previousRow;
					this.col = previousCol;
					return null;
				}
				list.add(value);
			}
		}
	}

	private YamlToken string() {
		if (this.isSymbol()) {
			return null;
		}
		String string = "";
		while (!this.isSymbol()) {
			string = string + this.getChar();
			if (!this.nextChar()) {
				break;
			}
		}
		return new YamlToken(string);
	}

	private boolean isSymbol() {
		return this.getChar() == ':';
	}

	private int skipSpace() {
		int skipCount = 0;
		while (this.getChar() == ' ') {
			if (this.nextChar()) {
				skipCount++;
			} else {
				break;
			}
		}
		return skipCount;
	}

}

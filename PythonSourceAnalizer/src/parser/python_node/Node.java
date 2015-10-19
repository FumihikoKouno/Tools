package parser.python_node;

public class Node {
	public enum NodeType
	{
		OBJ,
		IF,
		FOR,
		WHILE,
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		MOD,
		LIST,
		DICT,
		TUPLE,
		SUBSTITUTE,
		EQUAL,
		NOT,
		AND,
		OR,
		NUMBER,
		COMMENT,
		DOC,
		STRING,
		IMPORT,
		MEMBER,
	}
	protected String name;
}

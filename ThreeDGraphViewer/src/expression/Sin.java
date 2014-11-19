package expression;

import common.Value;

public class Sin extends UnaryNode {
	public Sin(Node n)
	{
		super(n);
	}
	public Value getValue() {
		return new Value(Math.sin(getChild().getValue().getValue()));
	}

	public String toString() {
		return "sin(" + getChild() + ")";
	}

}

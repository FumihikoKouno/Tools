package expression;

import common.Value;

public class Tan extends UnaryNode {
	public Tan(Node n)
	{
		super(n);
	}
	public Value getValue() {
		return new Value(Math.tan(getChild().getValue().getValue()));
	}

	public String toString() {
		return "tan(" + getChild() + ")";
	}

}

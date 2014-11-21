package expression;

import common.Value;

public class Negative extends UnaryNode {
	public Negative(Node n)
	{
		super(n);
	}
	public Value getValue() {
		return Value.sub(new Value(0),getChild().getValue());
	}

	public Node clone()
	{
		return new Negative(getChild().clone());
	}
	public String toString() {
		return "-" + getChild();
	}

}

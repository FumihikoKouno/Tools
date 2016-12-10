package expression;

import common.Value;

public class Cos extends UnaryNode {

	public Cos(Node n)
	{
		super(n);
	}
	public Node clone()
	{
		return new Cos(getChild().clone());
	}
	public Value getValue() {
		return new Value(Math.cos(getChild().getValue().getValue()));
	}

	public String toString() {
		return "cos(" + getChild() + ")";
	}

}

package expression;

import common.Value;

public class Positive extends UnaryNode {
	public Positive(Node n)
	{
		super(n);
	}
	public Value getValue() {
		return getChild().getValue();
	}

	public Node clone()
	{
		return new Positive(getChild().clone());
	}
	public String toString() {
		return "+" + getChild();
	}

}

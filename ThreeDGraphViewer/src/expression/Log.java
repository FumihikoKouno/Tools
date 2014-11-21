package expression;

import common.Value;

public class Log extends UnaryNode {
	public Log(Node n)
	{
		super(n);
	}
	public Value getValue() {
		return new Value(Math.log(getChild().getValue().getValue()));
	}

	public Node clone()
	{
		return new Log(getChild().clone());
	}
	public String toString() {
		return "log(" + getChild() + ")";
	}

}

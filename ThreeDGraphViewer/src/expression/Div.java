package expression;

import common.Value;

public class Div extends BinaryNode {
	public Div(Node l, Node r)
	{
		super(l,r);
	}
	public Value getValue() {
		return Value.div(getLhs().getValue(),getRhs().getValue());
	}

	public String toString() {
		return "(" + getLhs() + "/" + getRhs() + ")";
	}

}

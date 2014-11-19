package expression;

import common.Value;

public class Pow extends BinaryNode {
	public Pow(Node l, Node r)
	{
		super(l,r);
	}
	public Value getValue() {
		return Value.pow(getLhs().getValue(),getRhs().getValue());
	}

	public String toString() {
		return "(" + getLhs() + "^" + getRhs() + ")";
	}

}

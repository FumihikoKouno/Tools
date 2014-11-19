package expression;

import common.Value;

public class Mul extends BinaryNode {
	public Mul(Node l, Node r)
	{
		super(l,r);
	}
	public Value getValue() {
		return Value.mul(getLhs().getValue(),getRhs().getValue());
	}

	public String toString() {
		return "(" + getLhs() + "*" + getRhs() + ")";
	}

}

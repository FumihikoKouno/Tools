package expression;

import common.Value;

public class Sub extends BinaryNode {
	public Sub(Node l, Node r)
	{
		super(l,r);
	}
	public Value getValue() {
		return Value.sub(getLhs().getValue(),getRhs().getValue());
	}

	public Node clone()
	{
		return new Sub(getLhs().clone(),getRhs().clone());
	}

	public String toString() {
		return "(" + getLhs() + "-" + getRhs() + ")";
	}

}

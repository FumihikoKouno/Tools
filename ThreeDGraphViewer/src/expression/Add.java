package expression;

import common.Value;

public class Add extends BinaryNode {
	
	public Add(Node l, Node r)
	{
		super(l,r);
	}
	
	public Node clone()
	{
		return new Add(getLhs().clone(),getRhs().clone());
	}

	public Value getValue() {
		return Value.add(getLhs().getValue(),getRhs().getValue());
	}

	public String toString() {
		return "(" + getLhs() + "+" + getRhs() + ")";
	}

}

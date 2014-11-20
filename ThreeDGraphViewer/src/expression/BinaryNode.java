package expression;

import java.util.ArrayList;

import common.Value;

public abstract class BinaryNode extends Node {

	private Node lhs, rhs;
	
	public BinaryNode(Node l, Node r)
	{
		this.lhs = l;
		this.rhs = r;
	}
	
	public void setLhs(Node n){ lhs = n; }
	public void setRhs(Node n){ rhs = n; }
	
	public boolean equals(Node n)
	{
		if(n instanceof BinaryNode)
		{
			BinaryNode node = (BinaryNode)n;
			return this.getClass().getName().equals(n.getClass().getName())
				&& getLhs().equals(node.getLhs())
				&& getRhs().equals(node.getRhs());
		}
		return false;
	}
	
	public Node getLhs()
	{
		return lhs;
	}
	
	public Node getRhs()
	{
		return rhs;
	}
	@Override
	public void setParameter(String n, int d, int p, double r) {
		getLhs().setParameter(n,d,p,r);
		getRhs().setParameter(n,d,p,r);
	}
	
	public void substitute(double d)
	{
		getLhs().substitute(d);
		getRhs().substitute(d);
	}
	
	public ArrayList<Parameter> getParameters()
	{
		ArrayList<Parameter> ret = new ArrayList<Parameter>();
		ArrayList<Parameter> tmp;
		tmp = getLhs().getParameters();
		for(int i = 0; i < tmp.size(); i++)
		{
			ret.add(tmp.get(i));
		}
		tmp = getRhs().getParameters();
		for(int i = 0; i < tmp.size(); i++)
		{
			ret.add(tmp.get(i));
		}
		return ret;
	}
	
	public abstract Value getValue();
	public abstract String toString();

}

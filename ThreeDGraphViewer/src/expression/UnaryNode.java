package expression;

import java.util.ArrayList;

import common.Value;

public abstract class UnaryNode extends Node {

	private Node child;
	
	public UnaryNode(Node n)
	{
		child = n;
	}
	
	public void setChild(Node n)
	{
		child = n;
	}
		
	public Node getChild()
	{
		return child;
	}
	
	public boolean equals(Node n)
	{
		if(n instanceof UnaryNode)
		{
			UnaryNode node = (UnaryNode)n;
			return this.getClass().getName().equals(n.getClass().getName())
				&& getChild().equals(node.getChild());
		}
		return false;
	}
	
	public void setParameter(String n, int d, int p, double r) {
		getChild().setParameter(n, d, p, r);
	}
	public void substitute(double d)
	{
		getChild().substitute(d);
	}
	
	public ArrayList<Parameter> getParameters()
	{
		return getChild().getParameters();
	}
	
	public abstract Value getValue();
	public abstract String toString();

}

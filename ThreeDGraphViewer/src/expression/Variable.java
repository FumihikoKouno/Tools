package expression;

import common.Value;

public class Variable extends UnaryNode {
	String name;
	public Variable(String name, Node c)
	{
		super(c);
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public Node clone()
	{
		return new Variable(name,getChild().clone());
	}
	public boolean equals(Node n)
	{
		if(n instanceof Variable)
		{
			Variable v = (Variable)n;
			return this.name.equals(v.getName());
		}
		return false;
	}
	public Value getValue() {
		return getChild().getValue();
	}
	public String toString() {
		if(getChild()!=null) return name + ":" + getChild();
		else return name;
	}
}

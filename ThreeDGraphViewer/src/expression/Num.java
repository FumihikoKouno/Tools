package expression;

import java.util.ArrayList;

import common.Value;

public class Num extends Node {

	private Value num;
	public Num(Value num)
	{
		this.num = num;
	}
	public Num(double num)
	{
		this.num = new Value(num);
	}
	
	public Node clone()
	{
		return new Num(num);
	}
	
	public boolean equals(Node n)
	{
		if(n instanceof Num)
		{
			Num tmp = (Num)n;
			return Value.equals(this.getValue(),tmp.getValue());
		}
		return false;
	}
	
	public void setParameter(String n, int d, int p, double r){}
	public void substitute(double d){}
	
	public ArrayList<Parameter> getParameters()
	{
		return new ArrayList<Parameter>();
	}
	
	public Value getValue() {
		return num;
	}

	public String toString() {
		return num+"";
	}

}

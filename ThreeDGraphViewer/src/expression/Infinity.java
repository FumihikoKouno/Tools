package expression;

import java.util.ArrayList;

import common.Value;

public class Infinity extends Node {

	@Override
	public Value getValue() 
	{
		return new Value(Double.MAX_VALUE);
	}

	@Override
	public String toString() 
	{
		return "inf";
	}
	
	public Node clone()
	{
		return new Infinity();
	}

	@Override
	public void setParameter(String n, int d, int p, double r) {}

	@Override
	public void substitute(double d) {}

	@Override
	public boolean equals(Node n)
	{
		if(n instanceof Infinity) return true;
		return false;
	}

	@Override
	public ArrayList<Parameter> getParameters()
	{
		return new ArrayList<Parameter>();
	}

}

package expression;

import common.Value;

public class T extends Node {

	private double time;
	public T()
	{
		time = 0;
	}
	public T(double d)
	{
		time = d;
	}
	
	public boolean equals(Node n)
	{
		if(n instanceof T) return true;
		return false;
	}
	
	public void substitute(double d)
	{
		time = d;
	}
	
	public void setParameter(String n, int d, int p, double r) {}
	
	public Value getValue() {
		return new Value(time);
	}

	public String toString() {
		return "t";
	}

}

package expression;

import java.util.ArrayList;

import common.Value;

public abstract class Node
{
	public abstract Value getValue();
	public abstract String toString();
	public void setParameter(Parameter par, double d)
	{
		setParameter(par.getName(),par.getDerivative(),par.getPhase(),d);
	}
	public abstract Node clone();
	public abstract void setParameter(String n, int d, int p, double r);
	public abstract void substitute(double d);
	public abstract boolean equals(Node n);
	public abstract ArrayList<Parameter> getParameters();
}

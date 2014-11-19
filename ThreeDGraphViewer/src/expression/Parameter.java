package expression;

import common.Value;

public class Parameter extends UnaryNode {

	private String name;
	private int derivative;
	private int phase;
	
	public Parameter(String s, int d, int i, Interval v)
	{
		super(v);
		name = s;
		derivative = d;
		phase = i;
	}
	
	public String getName(){ return name; }
	public int getDerivative(){ return derivative; }
	public int getPhase(){ return phase; }
	
	public boolean equals(Node n)
	{
		if(n instanceof Parameter)
		{
			Parameter p = (Parameter)n;
			return name.equals(p.getName())
				&& derivative == p.getDerivative()
				&& phase == p.getPhase();
		}
		return false;
	}

	public void setParameter(String n, int d, int p, double r)
	{
		if(name.equals(n)&&derivative==d&&phase==p)
		{
			if(getChild() instanceof Interval)
			{
				Interval iv = (Interval)getChild();
				iv.setParameter(r);
			}
		}
		else
		{
			getChild().setParameter(n, d, p, r);
		}
	}
	
	@Override
	public Value getValue() {
		return getChild().getValue();
	}

	@Override
	public String toString() {
		if(getChild()!=null) return "p"+"["+name+","+derivative+","+phase+"]"+"{"+getChild()+"}";
		else return "p"+"["+name+","+derivative+","+phase+"]";
	}

}

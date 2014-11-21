package expression;

import common.Value;

public class Interval extends BinaryNode {

	public double ratio;
	private boolean lhsClosed, rhsClosed;
	
	public Interval(Node l, Node r)
	{
		super(l,r);
		lhsClosed = false;
		rhsClosed = false;
		ratio = 0;
	}
	public Node clone()
	{
		Interval iv = new Interval(getLhs().clone(),getRhs().clone());
		iv.lhsClosed(lhsClosed);
		iv.rhsClosed(rhsClosed);
		return iv;
	}
	
	public void lhsClosed(boolean b)
	{
		lhsClosed = b;
	}
	public void rhsClosed(boolean b)
	{
		rhsClosed = b;
	}
	public void setParameter(double r)
	{
		ratio = r;
	}
	public void setParameter(String n, int d, int p, double r)
	{
		getLhs().setParameter(n,d,p,r);
		getRhs().setParameter(n,d,p,r);
	}
	
	public Value getValue() {
		Value l = getLhs().getValue();
		Value r = getRhs().getValue();
		if(Value.ge(l,r))
		{
			System.err.println("invalid range");
			return null;
		}
		if(ratio == 0 && !lhsClosed)
		{
			return new Value(l.getValue()+Double.MIN_VALUE);
		}
		if(ratio == 1 && !rhsClosed)
		{
			return new Value(r.getValue()-Double.MIN_VALUE);
		}
		double v = l.getValue()+(ratio*Value.sub(r,l).getValue());
		return new Value(v);
	}

	@Override
	public String toString() {
		String lStr = (lhsClosed)?"[":"(";
		String rStr = (rhsClosed)?"]":")";
		return lStr + getLhs() + "," + getRhs() + rStr;
	}

}

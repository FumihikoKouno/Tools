package common;

public class Value 
{
	private double value;
	// Constructor for Value
	public Value(double v)
	{
		value = v;
	}
	public static boolean le(Value v1, Value v2)
	{
		return v1.getValue()<=v2.getValue();
	}
	public static boolean lt(Value v1, Value v2)
	{
		return v1.getValue()<v2.getValue();
	}
	public static boolean ge(Value v1, Value v2)
	{
		return v1.getValue()>=v2.getValue();
	}
	public static boolean gt(Value v1, Value v2)
	{
		return v1.getValue()>v2.getValue();
	}
	
	public static boolean equals(Value v1, Value v2)
	{
		return v1.getValue()==v2.getValue();
	}
	public static Value pow(Value v1, Value v2)
	{
		return new Value(Math.pow(v1.getValue(),v2.getValue()));
	}
	public static Value add(Value v1, Value v2)
	{
		return new Value(v1.getValue()+v2.getValue());
	}
	public static Value sub(Value v1, Value v2)
	{
		return new Value(v1.getValue()-v2.getValue());
	}
	public static Value mul(Value v1, Value v2)
	{
		return new Value(v1.getValue()*v2.getValue());
	}
	public static Value div(Value v1, Value v2)
	{
		return new Value(v1.getValue()/v2.getValue());
	}
	public double getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return value + "";
	}
}

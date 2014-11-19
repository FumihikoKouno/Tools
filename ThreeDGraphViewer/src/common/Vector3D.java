package common;

public class Vector3D {
	private static final int DIMENSION = 3;
	private double[] x = new double[DIMENSION]; 
	public Vector3D(double x1, double x2, double x3)
	{
		x[0] = x1;
		x[1] = x2;
		x[2] = x3;
	}
	public Vector3D(double[] v)
	{
		if(v.length==DIMENSION)
		{
			x = v;
		}
		else
		{
			System.err.println("difference of dimension size");
			for(int i = 0; i < DIMENSION; i++)
			{
				x[i] = 0;
			}
		}
	}
	public Vector3D(Vector3D v)
	{
		for(int i = 0; i < DIMENSION; i++)
		{
			x[i] = v.at(i);
		}
	}
	public double abs()
	{
		double ret = 0;
		for(int i = 0; i < DIMENSION; i++)
		{
			ret += at(i)*at(i);
		}
		return Math.sqrt(ret);
	}
	public static Vector3D normalize(Vector3D v)
	{
		double c = 0;
		for(int i = 0; i < DIMENSION; i++)
		{
			c += v.at(i)*v.at(i);
		}
		c = Math.sqrt(c);
		return Vector3D.mul(c,v);
	}
	public double get(int i)
	{
		return at(i);
	}
	public double at(int i)
	{
		return x[i];
	}
	public static double dot(Vector3D v1, Vector3D v2)
	{
		double ret = 0;
		for(int i = 0; i < DIMENSION; i++)
		{
			ret += v1.at(i)*v2.at(i);
		}
		return ret;
	}
	public static Vector3D cross(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.at(1)*v2.at(2)-v1.at(2)*v2.at(1),
							v1.at(2)*v2.at(0)-v1.at(0)*v2.at(2),
							v1.at(0)*v2.at(1)-v1.at(1)*v2.at(0));
	}
	public static Vector3D add(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.at(0)+v2.at(0),
							v1.at(1)+v2.at(1),
							v1.at(2)+v2.at(2));
	}
	public static Vector3D sub(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.at(0)-v2.at(0),
							v1.at(1)-v2.at(1),
							v1.at(2)-v2.at(2));
	}
	public void mul(double d)
	{
		for(int i = 0; i < DIMENSION; i++)
		{
			x[i] *= d;
		}
	}
	public static Vector3D mul(double d, Vector3D v)
	{
		return new Vector3D(d*v.at(0),
							d*v.at(1),
							d*v.at(2));
	}
	public String toString()
	{
		return "(" + at(0) + ", " + at(1) + ", " + at(2) + ")";
	}
}

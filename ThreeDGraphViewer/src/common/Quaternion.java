package common;
import common.Vector3D;

public class Quaternion {
	private double t;
	private Vector3D v;
	public Quaternion(double d, Vector3D v3)
	{
		t = d;
		v = v3;
	}
	public Quaternion(Quaternion q)
	{
		t = q.getDouble();
		v = new Vector3D(q.getVector3D());
	}
	public Quaternion conjugate()
	{
		return new Quaternion(getDouble(),Vector3D.mul(-1,getVector3D()));
	}
	public Vector3D rotate(Vector3D point)
	{
		Quaternion ret = Quaternion.mul(Quaternion.mul(this,new Quaternion(0,point)),conjugate());
		return ret.getVector3D();
	}
	public static Quaternion createQuaternion(Vector3D axis, double s)
	{
		double cos = Math.cos(s/2.0);
		double sin = Math.sin(s/2.0);
		Vector3D normalized = Vector3D.normalize(axis);
		return new Quaternion(cos,Vector3D.mul(-sin,normalized));
	}
	public static Vector3D rotate(Vector3D point, Vector3D axis, double s)
	{
		return createQuaternion(axis,s).rotate(point);
	}
	public static Quaternion createQuaternion(Vector3D begin, Vector3D end)
	{
		double cosS = Vector3D.dot(begin,end)/(begin.abs()*end.abs());
		double cosS2 = Math.sqrt(0.5*(1.0+cosS));
		double sinS2 = Math.sqrt(0.5*(1.0-cosS));
		Vector3D axis = Vector3D.cross(end,begin);
		axis = Vector3D.mul(1.0/axis.abs(),axis);
		return new Quaternion(cosS2,Vector3D.mul(-sinS2,axis));
	}
	public static Quaternion mul(Quaternion q1, Quaternion q2)
	{
		double r = q1.getDouble()*q2.getDouble()
					- Vector3D.dot(q1.getVector3D(),q2.getVector3D());
		Vector3D vec = 
				Vector3D.add(
					Vector3D.add(
						Vector3D.mul(q1.getDouble(),q2.getVector3D()),
						Vector3D.mul(q2.getDouble(),q1.getVector3D())
					),
					Vector3D.cross(q1.getVector3D(),q2.getVector3D())
				);
		return new Quaternion(r,vec);
	}
	public double getDouble()
	{
		return t;
	}
	public Vector3D getVector3D()
	{
		return v;
	}
	public String toString()
	{
		return "(" + getDouble() + ", " + getVector3D() + ")";
	}
}

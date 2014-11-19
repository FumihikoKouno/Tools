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
	public static Vector3D rotate(Vector3D point, Vector3D axis, double s)
	{
		double cos = Math.cos(s/2.0);
		double sin = Math.sin(s/2.0);
		Vector3D normalizedAxis = Vector3D.normalize(axis);
		Quaternion p = new Quaternion(0,point);
		Quaternion q = new Quaternion(cos,Vector3D.mul(sin,normalizedAxis));
		Quaternion r = new Quaternion(cos,Vector3D.mul(-sin,normalizedAxis));
		Quaternion ret = Quaternion.mul(Quaternion.mul(r,p),q);
		return ret.getVector3D();
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
}

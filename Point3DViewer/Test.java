import java.util.*;

class Test{
	public static void main(String args[]){
		Vec3D a = new Vec3D(0,0,1);
		Vec3D b = new Vec3D(1,0,0);
		Quaternion q = new Quaternion(a,b);
		Scanner sc = new Scanner(System.in);
		while(true){
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double z = sc.nextDouble();
			Vec3D t = new Vec3D(x,y,z);
			Quaternion q1 = new Quaternion(a,t);
			System.out.println(q1);
			System.out.println(a +" to "+ q1.rotate(a));
		}
	}
}

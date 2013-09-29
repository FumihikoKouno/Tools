import java.util.*;

class Test{
	public static void main(String args[]){
		Vec3D a = new Vec3D(0,0,1);
		Vec3D b = new Vec3D(1,0,0);
		Quaternion q = new Quaternion(a,b);
		Scanner sc = new Scanner(System.in);
		while(true){
			double t = sc.nextDouble();
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			double z = sc.nextDouble();
			if(x == 0 && y == 0 && z == 0) break;
			Quaternion tmp = new Quaternion(t,x,y,z);
			t = sc.nextDouble();
			x = sc.nextDouble();
			y = sc.nextDouble();
			z = sc.nextDouble();
			if(x == 0 && y == 0 && z == 0) break;
			Quaternion tmp2 = new Quaternion(t,x,y,z);
			System.out.println(tmp+ " X "+ tmp2+ " = "+ tmp.mul(tmp2));
			/*
			Vec3D t = new Vec3D(x,y,z);
			System.out.println(t + " to " + q.rotate(t));
			*/
		}
	}
}

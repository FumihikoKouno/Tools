package Tools.Data;

import java.util.ArrayList;

public class MotionData{

    public static final int TORSO = 0;
    public static final int NECK = 1;
    public static final int HEAD = 2;
    public static final int R_SHOULDER = 3;
    public static final int R_ELBOW = 4;
    public static final int R_HAND = 5;
    public static final int L_SHOULDER = 6;
    public static final int L_ELBOW = 7;
    public static final int L_HAND = 8;
    public static final int R_HIP = 9;
    public static final int R_KNEE = 10;
    public static final int R_FOOT = 11;
    public static final int L_HIP = 12;
    public static final int L_KNEE = 13;
    public static final int L_FOOT = 14;

    public static final int JOINT_NUMBER = 15;

    private ArrayList<Vec3D[]> data;

    public MotionData(){}
    public void add(Vec3D[] a){
	data.add(a);
    }
    
    public int size(){
	return data.size();
    }

    public Vec3D get(int time, int position){
	if(time >= data.size() || position >= JOINT_NUMBER) return null;
	else return data.get(time)[position];
    }

    public MotionData convert(MotionData model){
	
    }

    private Vec3D makeNext(Vec3D[] model, int from, int to){
	Vec3D ret = model[to]-model[from];
	ret = ret.times(1/ret.abs());
	ret.times(1);
    }

}

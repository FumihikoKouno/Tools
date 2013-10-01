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
    
    public Vec3D[] convert(MotionData model, int i){
        if(empty() || model.empty()){ return null; }
        Vec3D diff = new Vec3D(get(0,TORSO).sub(model.get(0,TORSO)));
        Vec3D[] vec = new Vec3D[JOINT_NUMBER];
        vec[TORSO] = get(i,TORSO).add(diff);
        vec[NECK]       = makeNext(model,i, TORSO,      NECK);
        vec[HEAD]       = makeNext(model,i, NECK,       HEAD);
        vec[R_SHOULDER] = makeNext(model,i, TORSO,      R_SHOULDER);
        vec[R_ELBOW]    = makeNext(model,i, R_SHOULDER, R_ELBOW);
        vec[R_HAND]     = makeNext(model,i, R_ELBOW,    R_HAND);
        vec[L_SHOULDER] = makeNext(model,i, TORSO,      L_SHOULDER);
        vec[L_ELBOW]    = makeNext(model,i, L_SHOULDER, L_ELBOW);
        vec[L_HAND]     = makeNext(model,i, L_ELBOW,    L_HAND);
        vec[R_HIP]      = makeNext(model,i, TORSO,      R_HIP);
        vec[R_KNEE]     = makeNext(model,i, R_HIP,      R_KNEE);
        vec[R_FOOT]     = makeNext(model,i, R_KNEE,     R_FOOT);
        vec[L_HIP]      = makeNext(model,i, TORSO,      L_HIP);
        vec[L_KNEE]     = makeNext(model,i, L_HIP,      L_KNEE);
        vec[L_FOOT]     = makeNext(model,i, L_KNEE,     L_FOOT);
        return vec;
    }
    
    public boolean empty(){
        return data.size() == 0;
    }

    public MotionData convertAll(MotionData model){
        MotionData ret = new MotionData();
        int end = Math.min(model.size(),size());
        for(int i = 0; i < end; i++){
            ret.add(convert(model,i));
        }
        return ret;
    }

    private Vec3D makeNext(MotionData model, int t, int from, int to){
        Vec3D ret = model.get(t,to).sub(model.get(t,from));
        ret = ret.times(1.0/ret.abs());
        return get(t,from).add(ret.times((get(t,to).sub(get(t,from)).abs())));
    }

}

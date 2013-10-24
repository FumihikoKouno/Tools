package Tools;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import Tools.Point3D.*;
import Tools.Data.*;

public class Main extends JFrame {
	Point3DPlayer pp;
	MotionDataConverter mdc;
	
	int fps = 30;
	
	public Main() {
		setTitle("Point3DPlayer");
		mdc = new MotionDataConverter(160,160);
		pp = new Point3DPlayer(640,640);
		Container contentPane = getContentPane();
		contentPane.add(pp,BorderLayout.EAST);
		contentPane.add(mdc,BorderLayout.WEST);
		pack();
	}
	
	public void run(){
		int mspf = 1000/fps;
		long time;
		while(true){
			try{
				time = System.currentTimeMillis();
				update();
				time = System.currentTimeMillis()-time;
				if(mspf > time){
					Thread.sleep(mspf-time);
				}else{
					System.out.println("SlowDown");
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void load(String[] args){
		for(int i = 0; i < args.length; i++){
			pp.loadData(args[i]);
		}
	}
	
	public void update(){
		pp.update();
	}

	public static void main(String[] args) {
		Main frame = new Main();
		frame.load(args);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.run();
	}
}
package Tools;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import Tools.Point3D.*;
import Tools.Data.*;

public class Main extends JFrame {
	Point3DPlayer pp;
	MotionDataConverter mdc;
    JTabbedPane tab = new JTabbedPane();

	int fps = 30;
	
	public Main() {
		setTitle("Point3DPlayer");
		mdc = new MotionDataConverter(640,640);
		pp = new Point3DPlayer(640,640);
		tab.addTab("Player",pp);
		tab.addTab("Converter",mdc);
		Container contentPane = getContentPane();
		/*
		contentPane.add(pp,BorderLayout.EAST);
		contentPane.add(mdc,BorderLayout.WEST);
		*/
		contentPane.add(tab,BorderLayout.CENTER);
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
	    switch(tab.getSelectedIndex()){
	    case 0:
      		pp.update();
		break;
	    case 1:
		break;
	    default:
		break;
	    }
	}

	public static void main(String[] args) {
		Main frame = new Main();
		frame.load(args);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.run();
	}
}
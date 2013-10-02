package Tools;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import Tools.Point3DViewer.*;

public class Main extends JFrame {
    //	Point3DViewer pv;
    Point3DPlayer pp;
	public Main() {
		setTitle("Point3DPPlayer");
		pp = new Point3DPlayer();
		Container contentPane = getContentPane();
		contentPane.add(pp);
		pack();
	}
	
	public void start(){
	    pp.start();
	    /*
	    while(true){
		try{
		    Thread.sleep(30);
		}catch(InterruptedException e){
		    e.printStackTrace();
		}
		pp.update();
	    }
	    */
	}

	public static void main(String[] args) {
		Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.start();
	}
}
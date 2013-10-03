package Tools;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import Tools.Point3DViewer.*;

public class Main extends JFrame {
	Point3DPlayer pp;
	Point3DPlayer pp2;
	public Main() {
		setTitle("Point3DPPlayer");
		pp = new Point3DPlayer(640,640);
	//	pp2 = new Point3DPlayer(320,640);
		Container contentPane = getContentPane();
		contentPane.add(pp,BorderLayout.EAST);
	//	contentPane.add(pp2,BorderLayout.WEST);
		pack();
	}
	
	public void start(){
		pp.start();
//		pp2.start();
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
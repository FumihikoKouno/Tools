import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main extends JFrame {
	Point3DViewer pv;
	public Main() {
		setTitle("Point3DViewer");
		pv = new Point3DViewer();
		Container contentPane = getContentPane();
		contentPane.add(pv,BorderLayout.EAST);
		pack();
	}
	
	public void start(){
	    pv.start();
	    /*
	    while(true){
		try{
		    Thread.sleep(30);
		}catch(InterruptedException e){
		    e.printStackTrace();
		}
		pv.update();
		pv2.update();
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
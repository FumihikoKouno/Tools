import java.awt.Container;

import javax.swing.JFrame;

public class Main extends JFrame {
	Point3DViewer pv;
	
	public Main() {
		setTitle("Point3DViewer");
		pv = new Point3DViewer();
		Container contentPane = getContentPane();
		contentPane.add(pv);
		pack();
	}
	
	public void start(){
		pv.start();
	}

	public static void main(String[] args) {
		Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.start();
	}
}
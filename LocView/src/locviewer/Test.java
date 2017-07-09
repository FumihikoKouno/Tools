package locviewer;

import javax.swing.JFrame;

public class Test extends JFrame {
	private static final long serialVersionUID = 1L;

	public Test() {
		this.add(new LOCViewerController());
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Test();
	}
}

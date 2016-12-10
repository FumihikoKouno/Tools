package memo;

import java.awt.Dimension;

import javax.swing.JFrame;

import memo.viewer.MemoViewer;

public class Test extends JFrame {
	public Test() {
		this.add(new MemoViewer());
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}

	public static void main(String[] args) {
		new Test();
	}
}

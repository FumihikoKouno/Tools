package Tools;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Tools.Point3D.*;
import Tools.Data.*;
import Tools.Runner.*;

public class Main extends JFrame implements ComponentListener{
	Point3DPlayer pp;
	MotionDataConverter mdc;
	Runner runner;
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	Container contentPane;
	Tools.Point3D.Option playerOption;
	
	public Main() {
		setTitle("Main");
		playerOption = new Tools.Point3D.Option(this);
		pp = new Point3DPlayer(480,480);
		pp.addComponentListener(this);
		mdc = new MotionDataConverter(pp);
		mdc.addComponentListener(this);
		runner = new Runner(mdc);
		runner.addComponentListener(this);
		JPanel left = new JPanel();
		left.add(mdc);
		right.setLeftComponent(pp);
		right.setRightComponent(runner);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		contentPane = getContentPane();
		contentPane.add(splitPane);
		makeMenu();
		pack();
	}
	
	public void makeMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.exit(0);
				}
			}
		);
		menuFile.add(exit);
		JMenu menuOption = new JMenu("Option");
		JMenuItem playerOption = new JMenuItem("Player Option");
		//		JMenuItem kimoissOption = new JMenuItem("Kimoiss Option");
		playerOption.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					callPlayerOption();
				}
			}
		);
		menuOption.add(playerOption);
		//		menuOption.add(kimoissOption);
		menuBar.add(menuFile);
		menuBar.add(menuOption);
		setJMenuBar(menuBar);
	}
	
	public void callPlayerOption(){
		playerOption.setVisible(true);
	}
	
	public void run(){
		int mspf;
		long time;
		while(true){
			mspf = 1000/Tools.Point3D.Option.fps;
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
	
	public void componentResized(ComponentEvent e){
		Dimension frameD = getSize();
		Dimension mdcD = mdc.getSize();
		Dimension runnerD = runner.getSize();
		int newW = frameD.width-mdcD.width;
		int newH = frameD.height-runnerD.height;
		pp.reSize(newW,newH);
		contentPane.validate();
		contentPane.repaint();
	}
	public void componentMoved(ComponentEvent e){
	}
	public void componentShown(ComponentEvent e){
	}
	public void componentHidden(ComponentEvent e){
	}
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.load(args);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.run();
	}
}
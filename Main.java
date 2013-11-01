package Tools;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import Tools.Point3D.*;
import Tools.Data.*;
import Tools.Runner.*;

public class Main extends JFrame implements ComponentListener{
	Point3DPlayer pp;
	MotionDataConverter mdc;
	Runner runner;
	//JTabbedPane tab = new JTabbedPane();
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	int fps = 30;
	
	public Main() {
		setTitle("Main");
		mdc = new MotionDataConverter(480,160);
		pp = new Point3DPlayer(480,480);
		runner = new Runner(160,540);
		JPanel left = new JPanel();
		left.add(runner);
		right.setLeftComponent(pp);
		right.setRightComponent(mdc);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		Container contentPane = getContentPane();
		contentPane.add(splitPane);
		
		splitPane.addComponentListener(this);
		right.addComponentListener(this);
		addComponentListener(this);
		pack();
	}
	
	public void debugPrint(){
    Dimension pre = pp.getPreferredSize();
    Dimension max = pp.getMaximumSize();
    Dimension mix = pp.getMinimumSize();
    System.out.println("„§:(" + pre.width + "," + pre.height + ")");
    System.out.println("Å‘å:(" + max.width + "," + max.height + ")");
    System.out.println("Å¬:(" + mix.width + "," + mix.height + ")");
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
		/*
	    switch(tab.getSelectedIndex()){
	    case 0:
      		pp.update();
		break;
	    case 1:
		break;
	    default:
		break;
	    }
		*/
	}
	
	public void componentResized(ComponentEvent e){
		Component comp = e.getComponent();
		if(comp == this){
			System.out.println("this resize");
		}
		if(comp == splitPane){
			System.out.println("splitPane resize");
		}
		if(comp == right){
			System.out.println("right resize");
		}
	}
	public void componentMoved(ComponentEvent e){
		Component comp = e.getComponent();
		if(comp == this){
			System.out.println("this move");
		}
		if(comp == splitPane){
			System.out.println("splitPane move");
		}
		if(comp == right){
			System.out.println("right move");
		}
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
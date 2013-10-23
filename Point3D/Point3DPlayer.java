package Tools.Point3D;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;

import java.io.IOException;
import java.io.File;

import Tools.Data.*;


public class Point3DPlayer extends JPanel implements ActionListener, AdjustmentListener{

	public int WIDTH;
	public int HEIGHT;

	public Point3DViewer pv;

	public JScrollBar sb;
	
	public int playPosition;

    private JFileChooser jfc = new JFileChooser();

    //    private Option option = new Option(JOptionPane.getFrameForComponent(this));

	public boolean playing;
	public ArrayList<Boolean> sentFlag = new ArrayList<Boolean>();

	public ArrayList<MotionData> mdList = new ArrayList<MotionData>();
	public ArrayList<Point[]> line = new ArrayList<Point[]>();
	
	public Color[] color = {
		Color.WHITE,
		Color.RED,
		Color.GREEN,
		Color.BLUE,
		Color.YELLOW,
		Color.PINK,
		Color.ORANGE
	};
	
	public void adjustmentValueChanged(AdjustmentEvent e){
		if(sb.getValue() != playPosition){
			pause();
			playPosition = sb.getValue();
			updatePoints();
		}
	}
	
	public void actionPerformed(ActionEvent e){
		String s = e.getActionCommand();
		if(s == "Play"){
			play();
		}
		if(s == "Stop"){
			stop();
		}
		if(s == "Pause"){
			pause();
		}
		if(s == "Option"){
		    Option option = new Option(JOptionPane.getFrameForComponent(this));
		    option.init();
		    option.setVisible(true);
		}
		if(s == "Add Data"){
		    if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			addData(jfc.getSelectedFile());
		    }
		}
		if(s == "Reset View"){
		    pv.setDefaultView();
		}
	}
	
	public Point3DPlayer(){
		this(640,640);
	}

	public Point3DPlayer(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		
		playPosition = 0;
		initGUI();
	}
	
	public void initGUI(){
		JButton pl = new JButton("Play");
		pl.addActionListener(this);
		JButton pa = new JButton("Pause");
		pa.addActionListener(this);
		JButton st = new JButton("Stop");
		st.addActionListener(this);
		JButton op = new JButton("Option");
		op.addActionListener(this);
		JButton ad = new JButton("Add Data");
		ad.addActionListener(this);
		JButton rv = new JButton("Reset View");
		rv.addActionListener(this);
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,3));

		buttons.add(pl);
		buttons.add(pa);
		buttons.add(st);
		buttons.add(op);
		buttons.add(ad);
		buttons.add(rv);
		
		sb = new JScrollBar(JScrollBar.HORIZONTAL);
		sb.setMaximum(0);
//		sb.setVisibleAmount(WIDTH);
		sb.addAdjustmentListener(this);

		setLayout(new BorderLayout());
		
		pv = new Point3DViewer(WIDTH, HEIGHT-80);
		add(pv,BorderLayout.NORTH);
		add(sb,BorderLayout.CENTER);
		add(buttons,BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void loadData(String s){
		MotionData tmp = new MotionData();
		if(!tmp.readFile(s)) return;
		if(sb.getMaximum()-1 < tmp.size()) sb.setMaximum(tmp.size()-1);
		mdList.add(tmp);
		line.add(tmp.getLine());
		sentFlag.add(false);
		sendPoints();
	}

        public void addData(File f){
		MotionData tmp = new MotionData();
		if(!tmp.readFile(f)) return;
		if(sb.getMaximum()-1 < tmp.size()) sb.setMaximum(tmp.size()-1);
		mdList.add(tmp);
		line.add(tmp.getLine());
		sentFlag.add(false);
		sendPoints();
	}
	
	public void pause(){
		playing = false;
	}
	
	public void stop(){
		playing = false;
		playPosition = 0;
		sb.setValue(0);
		updatePoints();
	}
	
	public void updatePoints(){
		boolean allEnd = true;
		for(int i = 0; i < mdList.size(); i++){
			Vec3D[] tmp = mdList.get(i).get(playPosition);
			if(tmp == null){ continue; }
			allEnd = false;
			pv.updatePoints(i,tmp);
		}
		if(allEnd){
			pause();
		}
	}
	
	public void next(){
		playPosition++;
		sb.setValue(playPosition);
		updatePoints();
	}
	
	public void sendPoints(){
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition >= mdList.get(i).size()) playPosition = 0;
			if(!sentFlag.get(i)){
				pv.addPoints(mdList.get(i).get(playPosition),line.get(i),color[i%color.length]);
				sentFlag.set(i,true);
			}
		}
	}

	public void play(){
		playing = true;
		boolean allEnd = true;
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition >= mdList.get(i).size()){ continue; }
			allEnd = false;
		}
		if(allEnd){
			playPosition = 0; 
			sb.setValue(0);
		}
	}
	
	public void update(){
		if(playing){
			next();
		}
		pv.update();
	}
	
	public void draw(){
	}

}

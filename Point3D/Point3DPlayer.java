package Tools.Point3DViewer;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;

import Tools.Data.*;


public class Point3DPlayer extends JPanel implements ActionListener{

	public int WIDTH;
	public int HEIGHT;

	public Point3DViewer pv;

	public int playPosition;
	
	public boolean playing;

	public ArrayList<MotionData> mdList = new ArrayList<MotionData>();
	public ArrayList<Vec3D[]> data = new ArrayList<Vec3D[]>();
	public Point[] line;
	
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
		JPanel buttons = new JPanel();
		
		buttons.add(pl,BorderLayout.EAST);
		buttons.add(pa,BorderLayout.CENTER);
		buttons.add(st,BorderLayout.WEST);
		pv = new Point3DViewer(WIDTH, HEIGHT-60);
		add(pv,BorderLayout.NORTH);
		add(buttons,BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void pause(){ playing = false; }
	
	public void stop(){
		playing = false;
		playPosition = 0;
	}
	
	public void next(){
		playPosition++;
		for(int i = 0; i < mdList.size(); i++){
			Vec3D[] tmp = null;
			// Vec3D tmp = mdList.get(i).get(playPosition);
			if(tmp == null){
				pause();
				return;
			}
			pv.updatePoints(i,tmp);
		}
	}

	public void play(){
		playing = true;
		/*
		int idx = 1;
		while(true){
			try{
				if(idx >=60) break;
				idx++;
				long time = System.currentTimeMillis();
				for(int i = 0; i < 2; i++){
					Vec3D[] test = new Vec3D[6];
					if(i==0){
						test[0] = new Vec3D(idx<<2,0,0);
						test[1] = new Vec3D(0,idx<<2,0);
						test[2] = new Vec3D(0,0,idx<<2);
						test[3] = new Vec3D(-idx<<2,0,0);
						test[4] = new Vec3D(0,-idx<<2,0);
						test[5] = new Vec3D(0,0,-idx<<2);
					}else{
						test[0] = new Vec3D(idx<<2,idx<<2,0);
						test[1] = new Vec3D(0,idx<<2,idx<<2);
						test[2] = new Vec3D(idx<<2,0,idx<<2);
						test[3] = new Vec3D(-idx<<2,0,-idx<<2);
						test[4] = new Vec3D(-idx<<2,-idx<<2,0);
						test[5] = new Vec3D(0,-idx<<2,-idx<<2);
					}
					pv.updatePoints(i,test);
				}
				pv.update();
				time = System.currentTimeMillis()-time;
				if(time < 33) Thread.sleep(33-time);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		*/
	}	
	
	public void setNext(){
		
	}
	
	public void update(){
		if(playing){
			setNext();
		}
		pv.update();
	}
	
	public void draw(){
	}

}

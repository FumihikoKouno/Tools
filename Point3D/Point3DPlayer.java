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
	public ArrayList<Boolean> sentFlag = new ArrayList<Boolean>();

	public ArrayList<MotionData> mdList = new ArrayList<MotionData>();
//	public ArrayList<Vec3D[]> data = new ArrayList<Vec3D[]>();
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
	
	public void loadData(String s){
		MotionData tmp = new MotionData();
		if(!tmp.readFile(s)) return;
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
		updatePoints();
	}
	
	public void updatePoints(){
		for(int i = 0; i < mdList.size(); i++){
			Vec3D[] tmp = mdList.get(i).get(playPosition);
			if(tmp == null){
				pause();
				return;
			}
			pv.updatePoints(i,tmp);
		}
	}
	
	public void next(){
		playPosition++;
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
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition >= mdList.get(i).size()){
				playPosition = 0;
				break;
			}
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

package Tools.Point3D;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
/*
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
*/
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
	
	public static final int BUTTON_HEIGHT = 52;
	
	public Point3DViewer pv;

	public JPanel scrollPanel = new JPanel();
	public ArrayList<JScrollBar> sb = new ArrayList<JScrollBar>();
	
	public ArrayList<Integer> playPosition = new ArrayList<Integer>();

	private JFileChooser jfc = new JFileChooser();

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
		for(int i = 0; i < mdList.size(); i++){
			if(sb.get(i).getValue() != playPosition.get(i)){
				pause();
				playPosition.set(i,sb.get(i).getValue());
			}
		}
		updatePoints();
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
			Option option = new Option(JOptionPane.getFrameForComponent(this),sb.size());
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
		
		setLayout(new BorderLayout());
		
		pv = new Point3DViewer(WIDTH, HEIGHT-BUTTON_HEIGHT);
		add(pv,BorderLayout.NORTH);
		add(scrollPanel,BorderLayout.CENTER);
		add(buttons,BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void reSize(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		pv.reSize(WIDTH,HEIGHT-BUTTON_HEIGHT-sb.size()*20);
		validate();
		repaint();
	}
	
	public void loadData(String s){
		addData(new File("./"+s));
	}

	public void addData(File f){
		MotionData tmp = new MotionData();
		if(!tmp.readFile(f)) return;
		JScrollBar sbTmp = new JScrollBar(JScrollBar.HORIZONTAL);
		sbTmp.setMaximum(tmp.size()-1+sbTmp.getVisibleAmount());
		sbTmp.addAdjustmentListener(this);
		sb.add(sbTmp);
		scrollPanel.setLayout(new GridLayout(sb.size(),1));
		scrollPanel.add(sbTmp);
		playPosition.add(new Integer(0));
		mdList.add(tmp);
		line.add(tmp.getLine());
		sentFlag.add(false);
		sendPoints();
		scrollPanel.validate();
		scrollPanel.repaint();
		reSize(WIDTH,HEIGHT);
	}
	
	public void pause(){
		playing = false;
	}
	
	public void stop(){
		playing = false;
		for(int i = 0; i < mdList.size(); i++){
			playPosition.set(i,0);
			sb.get(i).setValue(0);
		}
		updatePoints();
	}
	
	public void updatePoints(){
		boolean allEnd = true;
		for(int i = 0; i < mdList.size(); i++){
			Vec3D[] tmp = mdList.get(i).get(playPosition.get(i));
			if(tmp == null){ continue; }
			allEnd = false;
			pv.updatePoints(i,tmp);
		}
		if(allEnd){
			pause();
		}
	}
	
	public void printStatus(){
		for(int i = 0; i < mdList.size(); i++){
			System.out.println(i+"= position:"+playPosition.get(i)+"/"+mdList.get(i).size()+", bar:"+sb.get(i).getValue()+"/"+sb.get(i).getMaximum());
		}
	}
	
	public void next(){
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition.get(i) < mdList.get(i).size()-1){
				playPosition.set(i,playPosition.get(i)+1);
				sb.get(i).setValue(playPosition.get(i));
			}
		}
		updatePoints();
	}
	
	public void sendPoints(){
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition.get(i) >= mdList.get(i).size()) continue; //playPosition = 0;
			if(!sentFlag.get(i)){
				pv.addPoints(mdList.get(i).get(playPosition.get(i)),line.get(i),color[i%color.length]);
				sentFlag.set(i,true);
			}
		}
	}

	public void play(){
		playing = true;
		boolean allEnd = true;
		for(int i = 0; i < mdList.size(); i++){
			if(playPosition.get(i) >= mdList.get(i).size()){ continue; }
			allEnd = false;
		}
		if(allEnd){
			stop();
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

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

//	public ArrayList<MotionData> mdList = new ArrayList<MotionData>();
	public ArrayList<Vec3D[]> data = new ArrayList<Vec3D[]>();
	public Point[] line;
	
	public void actionPerformed(ActionEvent e){
		play();
	}
	
	public Point3DPlayer(){
		this(640,640);
	}

	public Point3DPlayer(int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		JButton pl = new JButton("Play");
		pl.addActionListener(this);
		
		JButton st = new JButton("Stop");
		JPanel buttons = new JPanel();
		buttons.add(pl,BorderLayout.EAST);
		buttons.add(st,BorderLayout.WEST);
		pv = new Point3DViewer(WIDTH, HEIGHT-60);
		
		add(pv,BorderLayout.NORTH);
		add(buttons,BorderLayout.SOUTH);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public void play(){
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
	}	
	
	public void start(){
		Vec3D[] test = new Vec3D[6];
		for(int i = 0; i < 6; i++){
			test[i] = new Vec3D();
		}
		pv.addPoints(test.clone(),null,Color.WHITE);
		pv.addPoints(test.clone(),null,Color.RED);
		pv.start();
		update();
	}
	
	public void update() {
		pv.update();
	/*
	if(dbImage == null){
		dbImage = createImage(WIDTH,HEIGHT);
		if(dbImage == null){
			System.out.println("dbImage is null");
			return;
		}else{
			dbg = dbImage.getGraphics();
		}
	}
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0,0,WIDTH,HEIGHT);
		draw();
	*/
	}

	
	public void draw(){
		/*
		try{
			Graphics g = getGraphics();
			if((g != null) && (dbImage != null)){
				g.drawImage(dbImage,0,0,null);
			}
			Toolkit.getDefaultToolkit().sync();
			if(g != null){
				g.dispose();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
	}

}

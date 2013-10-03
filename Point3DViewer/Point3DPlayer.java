package Tools.Point3DViewer;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.BorderLayout;

import Tools.Data.*;

public class Point3DPlayer extends JPanel{

	public int WIDTH;
	public int HEIGHT;

	public Point3DViewer pv;

	public int playPosition;

	public ArrayList<MotionData> mdList = new ArrayList<MotionData>();

	public Point3DPlayer(){
		this(640,640);
	}

	public Point3DPlayer(int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		JButton pl = new JButton("Play");
		JButton st = new JButton("Stop");
		JPanel buttons = new JPanel();
		buttons.add(pl,BorderLayout.EAST);
		buttons.add(st,BorderLayout.WEST);
		pv = new Point3DViewer(WIDTH, HEIGHT-60);

		add(pv,BorderLayout.NORTH);
		add(buttons,BorderLayout.SOUTH);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public void next(){
	
	}	
	
	public void start(){
		pv.start();
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

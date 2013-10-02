package Tools.Point3DViewer;

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

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 640;

    public Point3DViewer pv;
    public Point3DViewer pv2;
	
    public Point3DPlayer() {
	JButton pl = new JButton("Play");
	JButton st = new JButton("Stop");
	JPanel buttons = new JPanel();
	buttons.add(pl,BorderLayout.EAST);
	buttons.add(st,BorderLayout.WEST);
	pv = new Point3DViewer();
	pv2 = new Point3DViewer();
	add(pv,BorderLayout.EAST);
	add(pv2,BorderLayout.WEST);
	add(buttons,BorderLayout.SOUTH);
	setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
	
    public void start(){
	pv.start();
	pv2.start();
    }
	
    public void update() {
	pv.update();
	pv2.update();
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

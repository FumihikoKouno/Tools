package Tools.Point3D;

import java.lang.IndexOutOfBoundsException;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Point;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import Tools.Data.*;

public class Point3DViewer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{

	public int currentID = 0;

	public double SIZE_UNIT_BY_PERSPECTIVE;
	public Vec3D ONE_POINT_PERSPECTIVE_POINT;
	public Vec3D ROTATE_ORIGIN;
	public Vec3D SIZE_ORIGIN;

	public static final double MIN_ZOOM = 0;
	public double zoom = 1;
	
	public int mouseX, mouseY;
	
	public boolean endDrawnConvert = true;
	
	public int viewMethod;
	public static final int ONE_POINT_PERSPECTIVE = 0;
	public static final int NO_PERSPECTIVE = 1;
	
	public ArrayList<Vec3D[]> point = new ArrayList<Vec3D[]>();
	public ArrayList<Vec3D[]> rotatedPoint = new ArrayList<Vec3D[]>();
	public ArrayList<Vec3D> drawnPoint= new ArrayList<Vec3D>();
	public ArrayList<Point[]> line = new ArrayList<Point[]>();
	public ArrayList<Point> ids = new ArrayList<Point>();
	public ArrayList<Color> color = new ArrayList<Color>();

	public ArrayList<Quaternion> qu = new ArrayList<Quaternion>();
	
	public Vec3D origin;

	public static final Vec3D first = new Vec3D(0,0,-1);
	
	private JPopupMenu popupMenu;
	private JPanel menuPanel;
	
	private int WIDTH;
	private int HEIGHT;
	
	private Image dbImage;
	private Graphics dbg;
	
	public void reset(){
		point.clear();
		rotatedPoint.clear();
		color.clear();
		line.clear();
		qu.clear();
	}

	private void setDefaultView(){
		origin = new Vec3D(WIDTH/2,HEIGHT/2,0);
		for(int i = 0; i < point.size(); i++){
			rotatedPoint.set(i,point.get(i).clone());
			qu.set(i,new Quaternion(1,0,0,0));
		}
		update();
	}

	public void updatePoints(int i, Vec3D[] v){
		point.set(i,v);
		autoRotate();
	}
	
	public void updatePoints(int i, Vec3D[] v, Point[] l, Color c){
		point.set(i,v);
		line.set(i,l);
		color.set(i,c);
		autoRotate();
	}
	
	public void addPoints(Vec3D[] v, Point[] l, Color c){
		point.add(v);
		rotatedPoint.add(v.clone());
		color.add(c);
		line.add(l);
		qu.add(new Quaternion(1,0,0,0));
	}

	public Point3DViewer(){
		this(480,480);
	}
	
	public Point3DViewer(int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		viewMethod = ONE_POINT_PERSPECTIVE;
		ROTATE_ORIGIN = new Vec3D(0,0,0);
		ONE_POINT_PERSPECTIVE_POINT = new Vec3D(0,0,500);
		origin = new Vec3D(WIDTH/2.0,HEIGHT/2.0,0);
		SIZE_UNIT_BY_PERSPECTIVE = 50;
		SIZE_ORIGIN = new Vec3D(0,0,300);
	}
	
	public void setRotateOrigin(Vec3D v){ ROTATE_ORIGIN = v; }
	public void setOnePointPerspectivePoint(Vec3D v){ ONE_POINT_PERSPECTIVE_POINT = v; }
	public void setSizeOrigin(Vec3D v){ SIZE_ORIGIN = v; }
	public void setSizeUnit(double d){ SIZE_UNIT_BY_PERSPECTIVE = d; }
	public void setViewMethod(int i){ viewMethod = i; }
	
	public synchronized void update() {
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

		switch(viewMethod){
		case ONE_POINT_PERSPECTIVE:
			setPointByOnePointPerspective();
			break;
		case NO_PERSPECTIVE:
			setPoint();
			break;
		}
		drawPointAndLine();

		draw();
	}
	
	public void autoRotate(){
		for(int i = 0; i < point.size(); i++){
			rotatePoints(qu.get(i), point.get(i), i);
		}
	}

	private void setPointByOnePointPerspective(){
		drawnPoint.clear();
		ids.clear();
		for(int i = 0; i < rotatedPoint.size(); i++){
			Vec3D[] rp = rotatedPoint.get(i);
			for(int j = 0; j < rp.length; j++){
				Vec3D tmp = rp[j].sub(ONE_POINT_PERSPECTIVE_POINT);
				tmp = tmp.times(1.0-(rp[j].getZ()/ONE_POINT_PERSPECTIVE_POINT.getZ()));
				tmp = tmp.add(ONE_POINT_PERSPECTIVE_POINT);
				boolean insert = false;
				for(int k = 0; k < drawnPoint.size(); k++){
					if(tmp.getZ() > drawnPoint.get(k).getZ()){
						drawnPoint.add(k,tmp);
						ids.add(k,new Point(i,j));
						insert = true;
						break;
					}
				}
				if(!insert){
					drawnPoint.add(tmp);
					ids.add(new Point(i,j));
				}
			}
		}
	}

	private void setPoint(){
		drawnPoint.clear();
		ids.clear();
		for(int i = 0; i < rotatedPoint.size(); i++){
			Vec3D[] rp = rotatedPoint.get(i);
			for(int j = 0; j < rp.length; j++){
				boolean insert = false;
				for(int k = 0; k < drawnPoint.size(); k++){
					if(rp[j].getZ() > drawnPoint.get(k).getZ()){
						drawnPoint.add(k,rp[j]);
						ids.add(k,new Point(i,j));
						insert = true;
						break;
					}
				}
				if(!insert){
					drawnPoint.add(rp[j]);
					ids.add(new Point(i,j));
				}
			}
		}
	}

	public void drawPoint(Vec3D p, Color c){
		dbg.setColor(c);
		int x = (int)(origin.getX()+p.getX()*zoom);
		int y = (int)(origin.getY()-p.getY()*zoom);
		dbg.fillOval(x-2,y-2,8,8);
	}
	
	private void drawPointAndLine(){
		for(int i = 0; i < drawnPoint.size(); i++){
			Point id = new Point(ids.get(i).x, ids.get(i).y);
			Vec3D dp = drawnPoint.get(i).clone();
			int x = (int)(origin.getX()+dp.getX()*zoom);
			int y = (int)(origin.getY()-dp.getY()*zoom);
			int size = (int)((SIZE_ORIGIN.getZ()-dp.getZ())/SIZE_UNIT_BY_PERSPECTIVE);
			dbg.setColor(color.get(id.x));
			dbg.fillOval(x-2,y-2,size,size);
			try{
				for(int j = 0; j < line.get(id.x).length; j++){
					Vec3D tmpV;
					Point tmpL = line.get(id.x)[j];
					if(tmpL.x == id.y) tmpV = drawnPoint.get(ids.indexOf(new Point(id.x,tmpL.y))).clone();
					else continue;
					int x2 = (int)(origin.getX()+tmpV.getX()*zoom);
					int y2 = (int)(origin.getY()-tmpV.getY()*zoom);
					int size2 = (int)((SIZE_ORIGIN.getZ()-tmpV.getZ())/SIZE_UNIT_BY_PERSPECTIVE);
					dbg.drawLine(x+size/2,y+size/2,x2+size2/2,y2+size2/2);
				}
			}catch(IndexOutOfBoundsException e){
			}catch(NullPointerException e){}
		}
	}
	
	private void rotatePoints(Quaternion q, Vec3D[] p, int id){
		Vec3D[] rp = rotatedPoint.get(id);
		if(rp == null || rp.length != p.length) rp = new Vec3D[p.length];
		for(int i = 0; i < p.length; i++){
			rp[i] = q.rotate(p[i].sub(ROTATE_ORIGIN));
			rp[i] = rp[i].add(ROTATE_ORIGIN);
		}
	}
	
	public void draw(){
		drawPointAndLine();
		drawPoint(ROTATE_ORIGIN,Color.RED);
		drawPoint(ONE_POINT_PERSPECTIVE_POINT,Color.BLUE);
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
	}
	/**
	 * mouse methods
	 */
	public void mousePressed(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseDragged(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		double xDiff = mx-mouseX;
		double yDiff = my-mouseY;
		if((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0){
			double abs = Math.max(WIDTH,HEIGHT);
			Vec3D from = new Vec3D(0,0,-1);
			Vec3D to = new Vec3D(xDiff,-yDiff,-Math.sqrt(abs*abs-(xDiff*xDiff+yDiff*yDiff)));
			Quaternion rot = new Quaternion(from,to);
			for(int i = 0; i < point.size(); i++){
				qu.set(i,qu.get(i).mul(rot));
			}
			autoRotate();
		}
		if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0){
			origin = origin.add(new Vec3D(xDiff,yDiff,0));
		}
		mouseX = mx;
		mouseY = my;
	}

	public void mouseWheelMoved(MouseWheelEvent e){
		zoom -= e.getWheelRotation()/10.0;
		if(zoom < MIN_ZOOM) zoom = MIN_ZOOM;
	}
	public void mouseEntered(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){
	}
	public void mouseReleased(MouseEvent e){
	}
	public void mouseMoved(MouseEvent e){
	}
	public void mouseClicked(MouseEvent e){
	}
}

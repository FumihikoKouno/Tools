package Tools.Point3DViewer;

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
	
    public ArrayList<Vec3D[]> point = new ArrayList<Vec3D[]>();
    public ArrayList<Vec3D[]> rotatedPoint = new ArrayList<Vec3D[]>();
    public Vec3D[] drawnPoint;
    public Point[] drawnLine;
    public ArrayList<Point[]> line = new ArrayList<Point[]>();

    public ArrayList<Color> color = new ArrayList<Color>();
    Color drawnColor;

    public Vec3D origin;

    public static final Vec3D first = new Vec3D(0,0,-1);
    public ArrayList<Vec3D> eye = new ArrayList<Vec3D>();
	
    private int WIDTH;
    private int HEIGHT;
	
    private Image dbImage;
    private Graphics dbg;

    public void reset(){
	point.clear();
	rotatedPoint.clear();
	color.clear();
	line.clear();
	eye.clear();
    }

    public void setDefaultView(){
	origin = new Vec3D(WIDTH/2,HEIGHT/2,0);
	for(int i = 0; i < point.size(); i++){
	    rotatedPoint.set(i,point.get(i).clone());
	    eye.set(i,first.clone());
	}
    }

    public void addPoints(Vec3D[] v, Point[] l, Color c){
	point.add(v);
	rotatedPoint.add(v.clone());
	color.add(c);
	line.add(l);
	eye.add(first.clone());
    }

    public Point3DViewer(){
	this(480,480);
    }
	
    public Point3DViewer(int width, int height) {
	WIDTH = width;
	HEIGHT = height;

	Vec3D[] tpoint = new Vec3D[5];
	Point[] tline = new Point[8];

	tpoint[0] = new Vec3D(0,0,-100);
	tpoint[1] = new Vec3D(100,100,100);
	tpoint[2] = new Vec3D(-100,100,100);
	tpoint[3] = new Vec3D(-100,-100,100);
	tpoint[4] = new Vec3D(100,-100,100);
		
	tline[0] = new Point(0,1);
	tline[1] = new Point(0,2);
	tline[2] = new Point(0,3);
	tline[3] = new Point(0,4);
	tline[4] = new Point(1,2);
	tline[5] = new Point(1,4);
	tline[6] = new Point(2,3);
	tline[7] = new Point(3,4);

	addPoints(tpoint,tline,Color.WHITE);
      	addPoints(tpoint.clone(),tline.clone(),Color.RED);

	addMouseListener(this);
	addMouseMotionListener(this);
	addMouseWheelListener(this);

       	setPreferredSize(new Dimension(WIDTH, HEIGHT));

	ROTATE_ORIGIN = new Vec3D(0,0,0);
	ONE_POINT_PERSPECTIVE_POINT = new Vec3D(0,0,500);
	origin = new Vec3D(WIDTH/2.0,HEIGHT/2.0,0);
	SIZE_UNIT_BY_PERSPECTIVE = 50;
	SIZE_ORIGIN = new Vec3D(0,0,200);

	//	rotatePoints(new Quaternion(1, new Vec3D()), point);
    }
	
    public void start(){
	update();
    }
	
    public void update() {
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
	  
	for(int i = 0; i < point.size(); i++){
	    //setPoint(i);
	    setPointByOnePointPerspective(i);
	    drawPointAndLine();
	}
	draw();
    }

    public void setPointByOnePointPerspective(int id){
	Vec3D[] rp = rotatedPoint.get(id);
	drawnLine = line.get(id);
	drawnColor = color.get(id);
	drawnPoint = new Vec3D[rp.length];
	for(int i = 0; i < rp.length; i++){
	    drawnPoint[i] = rp[i].sub(ONE_POINT_PERSPECTIVE_POINT);
	    drawnPoint[i] = drawnPoint[i].times(1.0-(rp[i].getZ()/ONE_POINT_PERSPECTIVE_POINT.getZ()));
	    drawnPoint[i] = drawnPoint[i].add(ONE_POINT_PERSPECTIVE_POINT);
	}
    }

    public void setPoint(int id){
	drawnPoint = rotatedPoint.get(id);
	drawnLine = line.get(id);
	drawnColor = color.get(id);
    }

    public void drawPoint(Vec3D p, Color c){
	dbg.setColor(c);
	int x = (int)(origin.getX()+p.getX()*zoom);
	int y = (int)(origin.getY()-p.getY()*zoom);
	dbg.fillOval(x-2,y-2,8,8);
    }
    
    public void drawPointAndLine(){
	dbg.setColor(drawnColor);
	for(int i = 0; i < drawnPoint.length; i++){
	    int x = (int)(origin.getX()+drawnPoint[i].getX()*zoom);
	    int y = (int)(origin.getY()-drawnPoint[i].getY()*zoom);
	    int size = (int)((SIZE_ORIGIN.getZ()-drawnPoint[i].getZ())/SIZE_UNIT_BY_PERSPECTIVE);
	    dbg.fillOval(x-2,y-2,size,size);
	    for(int j = 0; j < drawnLine.length; j++){
		int tmpI = 0;
		if(drawnLine[j].x == i) tmpI = drawnLine[j].y;
		else continue;
		int x2 = (int)(origin.getX()+drawnPoint[tmpI].getX()*zoom);
		int y2 = (int)(origin.getY()-drawnPoint[tmpI].getY()*zoom);
		int size2 = (int)((SIZE_ORIGIN.getZ()-drawnPoint[tmpI].getZ())/SIZE_UNIT_BY_PERSPECTIVE);
		dbg.drawLine(x+size/2,y+size/2,x2+size2/2,y2+size2/2);
	    }
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
	if((e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0){
	    currentID = (currentID+1)%point.size();
	    //origin = new Vec3D(mouseX,mouseY,0);
	    setDefaultView();
	    update();
	}
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
		eye.set(i,rot.rotate(eye.get(i)));
		rotatePoints(rot, rotatedPoint.get(i), i);
	    }
	}
	if((e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0){
	    double abs = Math.max(WIDTH,HEIGHT);
	    Vec3D from = new Vec3D(0,0,-1);
	    Vec3D to = new Vec3D(xDiff,-yDiff,-Math.sqrt(abs*abs-(xDiff*xDiff+yDiff*yDiff)));
	    Quaternion rot = new Quaternion(from,to);
	    eye.set(currentID,rot.rotate(eye.get(currentID)));
	    rotatePoints(rot, rotatedPoint.get(currentID), currentID);
	}
	if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0){
	    origin = origin.add(new Vec3D(xDiff,yDiff,0));
	}
	mouseX = mx;
	mouseY = my;
	update();
    }

    public void mouseWheelMoved(MouseWheelEvent e){
	zoom -= e.getWheelRotation()/10.0;
	if(zoom < MIN_ZOOM) zoom = MIN_ZOOM;
	update();
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

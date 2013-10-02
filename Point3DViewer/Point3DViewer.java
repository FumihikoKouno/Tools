package Tools.Point3DViewer;

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


    public double SIZE_UNIT_BY_PERSPECTIVE;
    public Vec3D ONE_POINT_PERSPECTIVE_POINT;
    public Vec3D ROTATE_ORIGIN;
    public Vec3D SIZE_ORIGIN;

    public static final double MIN_ZOOM = 0;
    public double zoom = 1;
	
    public int mouseX, mouseY;
	
    public Vec3D[] point;
    public Vec3D[] rotatedPoint;
    public Vec3D[] drawnPoint;

    public Vec3D origin = new Vec3D(WIDTH/2.0,HEIGHT/2.0,0);

    public Point[] line;
    public static final Vec3D first = new Vec3D(0,0,-1);
    public Vec3D eye = new Vec3D(0,0,-1);
	
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;
	
    private Image dbImage;
    private Graphics dbg;
	
    private int frame;
	
    public Point3DViewer() {
	point = new Vec3D[5];
	line = new Point[8];

	point[0] = new Vec3D(0,0,-100);
	point[1] = new Vec3D(100,100,100);
	point[2] = new Vec3D(-100,100,100);
	point[3] = new Vec3D(-100,-100,100);
	point[4] = new Vec3D(100,-100,100);
		
	line[0] = new Point(0,1);
	line[1] = new Point(0,2);
	line[2] = new Point(0,3);
	line[3] = new Point(0,4);
	line[4] = new Point(1,2);
	line[5] = new Point(1,4);
	line[6] = new Point(2,3);
	line[7] = new Point(3,4);

	addMouseListener(this);
	addMouseMotionListener(this);
	addMouseWheelListener(this);

	setPreferredSize(new Dimension(WIDTH, HEIGHT));

	ROTATE_ORIGIN = new Vec3D(0,0,0);
	ONE_POINT_PERSPECTIVE_POINT = new Vec3D(0,0,500);

	SIZE_UNIT_BY_PERSPECTIVE = 50;
	SIZE_ORIGIN = new Vec3D(0,0,200);
	rotatePoints(new Quaternion(1, new Vec3D()), point);
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
	  
       	//setPoint();
	setPointByOnePointPerspective();
	    
	draw();
    }

    public void setPointByOnePointPerspective(){
	drawnPoint = new Vec3D[rotatedPoint.length];
	for(int i = 0; i < rotatedPoint.length; i++){
	    drawnPoint[i] = rotatedPoint[i].sub(ONE_POINT_PERSPECTIVE_POINT);
	    drawnPoint[i] = drawnPoint[i].times(1.0-(rotatedPoint[i].getZ()/ONE_POINT_PERSPECTIVE_POINT.getZ()));
	    drawnPoint[i] = drawnPoint[i].add(ONE_POINT_PERSPECTIVE_POINT);
	}
    }

    public void setPoint(){
	drawnPoint = rotatedPoint;
    }

    public void drawPoint(Vec3D p, Color c){
	dbg.setColor(c);
	int x = (int)(origin.getX()+p.getX()*zoom);
	int y = (int)(origin.getY()-p.getY()*zoom);
	dbg.fillOval(x-2,y-2,8,8);
    }
    
    public void drawPointAndLine(){
	dbg.setColor(Color.WHITE);
	for(int i = 0; i < drawnPoint.length; i++){
	    int x = (int)(origin.getX()+drawnPoint[i].getX()*zoom);
	    int y = (int)(origin.getY()-drawnPoint[i].getY()*zoom);
	    int size = (int)((SIZE_ORIGIN.getZ()-drawnPoint[i].getZ())/SIZE_UNIT_BY_PERSPECTIVE);
	    System.out.println(i+":"+drawnPoint[i].getZ()+","+size);
	    dbg.fillOval(x-2,y-2,size,size);
	    for(int j = 0; j < line.length; j++){
		int tmpI = 0;
		if(line[j].x == i) tmpI = line[j].y;
		else continue;
		int x2 = (int)(origin.getX()+drawnPoint[tmpI].getX()*zoom);
		int y2 = (int)(origin.getY()-drawnPoint[tmpI].getY()*zoom);
		int size2 = (int)((SIZE_ORIGIN.getZ()-drawnPoint[tmpI].getZ())/SIZE_UNIT_BY_PERSPECTIVE);
		dbg.drawLine(x+size/2,y+size/2,x2+size2/2,y2+size2/2);
	    }
	}
    }
    
    private void rotatePoints(Quaternion q, Vec3D[] p){
	if(rotatedPoint == null || rotatedPoint.length != p.length) rotatedPoint = new Vec3D[p.length];
	for(int i = 0; i < p.length; i++){
	    rotatedPoint[i] = q.rotate(p[i].sub(ROTATE_ORIGIN));
	    rotatedPoint[i] = rotatedPoint[i].add(ROTATE_ORIGIN);
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
	    origin = new Vec3D(mouseX,mouseY,0);
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
	    eye = rot.rotate(eye);
	    rotatePoints(rot, rotatedPoint);
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

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

class Point3DViewer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{

	public static final int MIN_ZOOM = 10;
	public int zoom = 100;
	
	public int mouseX, mouseY;
	
	public Vec3D[] point;
	public Point[] line;
	public static final Vec3D first = new Vec3D(0,0,1);
	public Vec3D eye = new Vec3D(0,0,1);
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private Image dbImage;
	private Graphics dbg;
	
	private int[] test = new int[3];
	
	private int frame;
	
	public Point3DViewer() {
		point = new Vec3D[8];
		line = new Point[12];
		point[0] = new Vec3D(-1,-1,-1);
		point[1] = new Vec3D(-1,-1,1);
		point[2] = new Vec3D(-1,1,1);
		point[3] = new Vec3D(-1,1,-1);
		point[4] = new Vec3D(1,1,-1);
		point[5] = new Vec3D(1,1,1);
		point[6] = new Vec3D(1,-1,1);
		point[7] = new Vec3D(1,-1,-1);
		
		line[0] = new Point(0,1);
		line[1] = new Point(0,3);
		line[2] = new Point(0,7);
		line[3] = new Point(1,2);
		line[4] = new Point(1,6);
		line[5] = new Point(2,3);
		line[6] = new Point(2,5);
		line[7] = new Point(3,4);
		line[8] = new Point(4,5);
		line[9] = new Point(4,7);
		line[10] = new Point(5,6);
		line[11] = new Point(6,7);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void start(){
		frame = 0;
		while(true){
			frame++;
			try{
				Thread.sleep(30);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			update();
		}
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
		
		setPoint();
		
		draw();
	}
	
	public void setPoint(){
		Color[] color = new Color[8];
		color[0] = Color.WHITE;
		color[1] = Color.RED;
		color[2] = Color.YELLOW;
		color[3] = Color.BLUE;
		color[4] = Color.GREEN;
		color[5] = Color.ORANGE;
		color[6] = Color.PINK;
		color[7] = Color.CYAN;
		Quaternion r = new Quaternion(first, eye.unit());
		
		Vec3D[] tmp = new Vec3D[point.length];
		Point[] tmpP = new Point[line.length];
		for(int i = 0; i < line.length; i++){
			tmpP[i] = line[i];
		}
		for(int i = 0; i < point.length; i++){
			tmp[i] = r.rotate(point[i]);
		}
		for(int i = 0; i < point.length-1; i++){
			for(int j = i+1; j < point.length; j++){
				if(tmp[i].getZ() > tmp[j].getZ()){
					Vec3D swapVec = tmp[i];
					tmp[i] = tmp[j];
					tmp[j] = swapVec;
					Color swapColor = color[i];
					color[i] = color[j];
					color[j] = swapColor;
					for(int k = 0; k < line.length; k++){
						if(tmpP[k].x == i){
							tmpP[k] = new Point(j,tmpP[k].y);
						}else if(tmpP[k].x == j){
							tmpP[k] = new Point(i,tmpP[k].y);
						}
						if(tmpP[k].y == i){
							tmpP[k] = new Point(tmpP[k].x,j);
						}else if(tmpP[k].y == j){
							tmpP[k] = new Point(tmpP[k].x,i);
						}
					}
				}
			}
		}
		for(int i = 0; i < point.length; i++){
			dbg.setColor(color[i]);
			int x = (int)(tmp[i].getX()*zoom);
			int y = (int)(tmp[i].getY()*zoom);
			dbg.fillOval((x-2+WIDTH/2),(y-2+HEIGHT/2),10,10);

			for(int j = 0; j < line.length; j++){
				int tmpI = 0;
				if(tmpP[j].x == i){
					tmpI = tmpP[j].y;
				}else{
					continue;
				}
				int x2 = (int)(tmp[tmpI].getX()*zoom);
				int y2 = (int)(tmp[tmpI].getY()*zoom);
				dbg.drawLine(x+3+WIDTH/2,y+3+HEIGHT/2,x2+3+WIDTH/2,y2+3+HEIGHT/2);
			}
		}
	}
	
	public void draw(){
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
	 * こっからマウス関係のメソッド
	 */
	public void mousePressed(MouseEvent e){
		mouseX = e.getX()-WIDTH/2;
		mouseY = e.getY()-HEIGHT/2;
	}
	public void mouseDragged(MouseEvent e){
		int mx = e.getX()-WIDTH/2;
		int my = e.getY()-HEIGHT/2;
		/*
		double xDiff = 1.0*(mx-mouseX)/zoom;
		double yDiff = 1.0*(my-mouseY)/zoom;
		*/
		double xDiff = mx-mouseX;
		double yDiff = my-mouseY;
		
		double abs = 300;
		Vec3D from = new Vec3D(mouseX,mouseY,Math.sqrt(abs*abs-(mouseX*mouseX+mouseY*mouseY)));
		Vec3D to = new Vec3D(mouseX+xDiff,mouseY+yDiff,0);
		double tx = to.getX();
		double ty = to.getY();
		if(abs < to.abs()){
			Vec3D unit = to.times(abs/to.abs());
			to = unit.times(2).sub(to);
			to.setZ(Math.sqrt((tx*tx+ty*ty)-abs*abs));
		}else{
			to.setZ(Math.sqrt(abs*abs-(tx*tx+ty*ty)));
		}
		Quaternion rot = new Quaternion(from,to);
		eye = rot.rotate(eye);
		
//		System.out.println(from+" : " + to);
		System.out.println("eye : " + eye);
		mouseX = mx;
		mouseY = my;
	}
	public void mouseWheelMoved(MouseWheelEvent e){
		zoom -= e.getWheelRotation()*10;
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

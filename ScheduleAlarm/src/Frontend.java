import javax.swing.JFrame;

public class Frontend extends JFrame{
	public Frontend(){
		this.add(new JobAddPanel());
		this.pack();
	}
	
	public void run(){
		this.setVisible(true);
	}
	
	public static void main(String[] args){
		Frontend frontend = new Frontend();
		frontend.run();
	}
}

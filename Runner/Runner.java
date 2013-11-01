package Tools.Runner;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;

public class Runner extends JPanel implements ActionListener{
	private int WIDTH, HEIGHT;
	private JTextField path = new JTextField("",5);
	
	public Runner(int w, int h){
		init();
		WIDTH = w;
		HEIGHT = h;
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
	}
	public void init(){
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout());
		pathPanel.add(new JLabel("KiMoiss Path:"));
		pathPanel.add(path);
		
		JButton runButton = new JButton("Run");
		runButton.addActionListener(this);

		add(pathPanel);
		add(runButton);
	}
	public void run(){
		Runtime rt = Runtime.getRuntime();
		String str = path.getText();
		try{
			rt.exec(str);
		}catch(IOException e){
		}
	}
	
	public void actionPerformed(ActionEvent e){
		String str = e.getActionCommand();
		if(str.equals("Run")){
			run();
		}
	}
	
}

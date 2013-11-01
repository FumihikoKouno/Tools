package Tools.Data;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MotionDataConverter extends JPanel implements ActionListener{
	public int WIDTH,HEIGHT;
	public JFileChooser fileChooser = new JFileChooser();
	
	public MotionData[] data = new MotionData[3];
	public JButton[] selectFileButton = new JButton[3];
	public JLabel[] fileNameLabel = new JLabel[3];
	public JLabel[] fileLengthLabel = new JLabel[3];
	public JPanel[] panels = new JPanel[3];
	
	public JButton convertButton = new JButton("Convert");
	
	public MotionDataConverter(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		init();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
	}
	
	public void resize(){
		int ppX,ppY;
		int mdcX,mdcY;
		int runnerX,runnerY;
	}
	
	public void init(){
		setLayout(new BorderLayout());
		JPanel parts = new JPanel();
		parts.setLayout(new GridLayout(1,3));
		for(int i = 0; i < 3; i++){
			if(i == 2){
				selectFileButton[i] = new JButton("Save File");
			}else{
				selectFileButton[i] = new JButton("Select File");
			}
			selectFileButton[i].addActionListener(this);
			data[i] = new MotionData();
			fileNameLabel[i] = new JLabel("File Name : null");
			fileLengthLabel[i] = new JLabel("0");
		}
		
		panels[0] = new JPanel();
		panels[0].setLayout(new GridLayout(3,1));
		panels[0].add(fileNameLabel[0]);
		panels[0].add(fileLengthLabel[0]);
		panels[0].add(selectFileButton[0]);
		panels[0].setBorder(new TitledBorder("Model Data"));
		
		panels[1] = new JPanel();
		panels[1].setLayout(new GridLayout(3,1));
		panels[1].add(fileNameLabel[1]);
		panels[1].add(fileLengthLabel[1]);
		panels[1].add(selectFileButton[1]);
		panels[1].setBorder(new TitledBorder("User Data"));
		
		panels[2] = new JPanel();
		panels[2].setLayout(new GridLayout(3,1));
		panels[2].add(fileNameLabel[2]);
		panels[2].add(fileLengthLabel[2]);
		panels[2].add(selectFileButton[2]);
		panels[2].setBorder(new TitledBorder("Result Data"));
		
		for(int i = 0; i < 3; i++){
			parts.add(panels[i]);
		}
		
		convertButton.addActionListener(this);
		
		add(parts,BorderLayout.CENTER);
		add(convertButton,BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent e){
		for(int i = 0; i < 2; i++){
			if(e.getSource() == selectFileButton[i]){
				if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
					File tmp = fileChooser.getSelectedFile();
					if(!data[i].readFile(tmp)) return;
					fileNameLabel[i].setText("File Name : " + tmp.getName());
					fileLengthLabel[i].setText("" + data[i].size() + " frame");
				}
			}
		}
		if(e.getSource() == selectFileButton[2]){
			try{
				if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
					File tmp = fileChooser.getSelectedFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
					bw.write(""+data[2]);
					fileNameLabel[2].setText("File Name : " + tmp.getName());
					fileLengthLabel[2].setText("" + data[2].size() + " frame");
					bw.close();
				}
			}catch(NullPointerException npe){
			}catch(IOException ie){
			}
		}
		if(e.getSource() == convertButton){
			try{
				data[2] = data[1].convertAll(data[0]);
				fileNameLabel[2].setText("File Name : New File");
				fileLengthLabel[2].setText("" + data[2].size() + " frame");
			}catch(NullPointerException npe){
			}
		}
	}
	
	public void reSize(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		validate();
		repaint();
	}
}

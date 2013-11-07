package Tools.Runner;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.io.BufferedInputStream;

import Tools.Data.MotionDataConverter;

public class Runner extends JPanel implements ActionListener{
	private int WIDTH, HEIGHT;
	
	private MotionDataConverter mdc;
	
	private JButton runButton = new JButton("Run");
	private JEditorPane out = new JEditorPane();

	public Runner(MotionDataConverter mdc){
		this.mdc = mdc;
		init();
	}
	public void init(){
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		runButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					run();
				}
			}
		);
		add(runButton);
		add(out);
	}
	
	
	public void run(){
		Runtime rt = Runtime.getRuntime();
		ArrayList<String> command = new ArrayList<String>();
		command.add(Option.kPath);
		command.add("-f "+Option.fps);
		switch(Option.mode){
		case Option.MODEL_MODE:
			command.add("-m m");
			command.add("-sm "+Option.saveModel);
			break;
		case Option.USER_MODE:
			command.add("-m u");
			command.add("-mo "+Option.modelPath);
			command.add("-su "+Option.saveUser);
			command.add("-sc "+Option.saveConvert);
			break;
		}
		try{
			Process process = rt.exec(command.toArray(new String[0]));
			String tmp = "exec ";
			for(int i = 0; i < command.size(); i++){
				tmp += command.get(i) + " ";
			}
			tmp += "\n";
			System.out.println(tmp);
			byte[] output = new byte[1];
			BufferedInputStream in = new BufferedInputStream(process.getInputStream());
			while(in.read(output) >= 0){
				tmp += new String(output);
			}
			System.out.println(tmp);
			out.setText(tmp);
			revalidate();
			repaint();

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e){
		String str = e.getActionCommand();
		if(str.equals("Run")){
			run();
		}
	}
	
}

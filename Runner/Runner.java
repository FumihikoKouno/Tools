// TODO: Runボタン以外はツールバーからのオプションに
package Tools.Runner;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
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

import Tools.Data.MotionDataConverter;

public class Runner extends JPanel implements ActionListener{
	private int WIDTH, HEIGHT;
	
	private MotionDataConverter mdc;
	
	private JFileChooser fc = new JFileChooser();
	private JTextField path = new JTextField("",10);
	private JRadioButton asUser = new JRadioButton("User Mode",true);
	private JTextField modelFilePath = new JTextField("ModelData.dat");
	private JTextField savedUserFileName = new JTextField("UserData.dat");
	private JTextField savedResultFileName = new JTextField("ConvertedData.dat");
	private JTextField savedModelFileName = new JTextField("ModelData.dat");
	private JRadioButton asModel = new JRadioButton("Model Mode",false);
	private JButton selectProgramFile = new JButton("Select File");
	private JButton selectModelFile = new JButton("Select File");
	private JButton runButton = new JButton("Run");

	public Runner(MotionDataConverter mdc){
		this.mdc = mdc;
		init();
	}
	public void init(){
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pathPanel.add(new JLabel("KiMoiss Path:"));
		pathPanel.add(path);
		pathPanel.add(selectProgramFile);
		/*
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(asUser);
		modeGroup.add(asModel);
		
		JPanel userModePanel = new JPanel();
		userModePanel.setLayout(new GridLayout(5,1));
		userModePanel.add(asUser);
		JPanel userModeModelPanel = new JPanel();
		userModeModelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userModeModelPanel.add(new JLabel("Model File : "));
		userModeModelPanel.add(modelFilePath);
		userModePanel.add(userModeModelPanel);
		JPanel userModeSelectModelPanel = new JPanel();
		userModeSelectModelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userModeSelectModelPanel.add(new JLabel("Model Data File : "));
		userModeSelectModelPanel.add(modelFilePath);
		userModeSelectModelPanel.add(selectModelFile);
		userModePanel.add(userModeSelectModelPanel);
		JPanel userModeSavedUserPanel = new JPanel();
		userModeSavedUserPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userModeSavedUserPanel.add(new JLabel("Saved User File Name : "));
		userModeSavedUserPanel.add(savedUserFileName);
		userModePanel.add(userModeSavedUserPanel);
		JPanel userModeResultPanel = new JPanel();
		userModeResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userModeResultPanel.add(new JLabel("Saved Converted File Name : "));
		userModeResultPanel.add(savedResultFileName);
		userModePanel.add(userModeResultPanel);
		
		JPanel modelModePanel = new JPanel();
		modelModePanel.add(asModel);
		modelModePanel.add(savedModelFileName);
		
		runButton.addActionListener(this);
*/
		add(pathPanel);
//		add(userModePanel);
//		add(modelModePanel);
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

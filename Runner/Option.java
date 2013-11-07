package Tools.Runner;

import java.util.ArrayList;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFileChooser;

import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Tools.Data.*;

public class Option extends JDialog{
	public static int fps = 30;
	public static String saveModel = "ModelData.dat";
	public static String saveUser = "UserData.dat";
	public static String saveConvert = "ConvertedData.dat";
	public static String modelPath = "Model File Path";
	public static String kPath = "";
	public static final int MODEL_MODE = 0;
	public static final int USER_MODE = 1;
	public static int mode = MODEL_MODE;
	
	public JFileChooser fc = new JFileChooser();
	public JTextField path = new JTextField(kPath,10);
	public JRadioButton asUser = new JRadioButton("User Mode",false);
	public JTextField modelFilePath = new JTextField(modelPath);
	public JTextField savedUserFileName = new JTextField(saveUser);
	public JTextField savedResultFileName = new JTextField(saveConvert);
	public JTextField fpsText = new JTextField(fps+"");
	public JTextField savedModelFileName = new JTextField(saveModel);
	public JRadioButton asModel = new JRadioButton("Model Mode",true);
	public JButton selectModelFile = new JButton("Select File");
	
	public Option(JFrame frame){
		super(frame,true);
		init();
	}
	
	public void setValues(){
		path.setText(kPath);
		asUser.setSelected(mode==USER_MODE);
		modelFilePath.setText(modelPath);
		savedUserFileName.setText(saveUser);
		savedResultFileName.setText(saveConvert);
		fpsText.setText(fps+"");
		savedModelFileName.setText(saveModel);
		asModel.setSelected(mode==MODEL_MODE);
	}
	
	public void setMode(){
		savedUserFileName.setEditable(asUser.isSelected());
		savedResultFileName.setEditable(asUser.isSelected());
		modelFilePath.setEditable(asUser.isSelected());
		selectModelFile.setEnabled(asUser.isSelected());
		savedModelFileName.setEditable(asModel.isSelected());
	}
	
	public void close(boolean b){
		if(b){
			try{
				fps = Integer.parseInt(fpsText.getText());
				saveModel = savedModelFileName.getText();
				saveUser = savedUserFileName.getText();
				saveConvert = savedResultFileName.getText();
				kPath = path.getText();
				if(asModel.isSelected()){
					mode = MODEL_MODE;
				}else{
					mode = USER_MODE;
				}
			}catch(NumberFormatException e){
				setValues();
			}
		}else{
			setValues();
		}
		setVisible(false);
	}
	
	public void init(){
		setTitle("Kimoiss Option");
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pathPanel.add(new JLabel("Kimoiss Path : "));
		pathPanel.add(path);
		JButton selectPath = new JButton("Select File");
		selectPath.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					selectPath();
				}
			}
		);
		pathPanel.add(selectPath);
		
		JPanel fpsPanel = new JPanel();
		fpsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fpsPanel.add(new JLabel("FPS : "));
		fpsPanel.add(fpsText);
		
		JPanel commonSetting = new JPanel();
		commonSetting.setLayout(new GridLayout(2,1));
		commonSetting.add(pathPanel);
		commonSetting.add(fpsPanel);
		
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new GridLayout(1,2));
		
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(asUser);
		modeGroup.add(asModel);
		
		asUser.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setMode();
				}
			}
		);
		asModel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setMode();
				}
			}
		);
		
		JPanel modelPanel = new JPanel();
		
		JPanel modelSetting = new JPanel();
		JPanel saveModelFileNamePanel = new JPanel();
		saveModelFileNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		saveModelFileNamePanel.add(new JLabel("Output Model File Name : "));
		saveModelFileNamePanel.add(savedModelFileName);
		
		modelSetting.add(saveModelFileNamePanel);
		modelSetting.setBorder(new EtchedBorder());
		
		modelPanel.setLayout(new BorderLayout());
		modelPanel.add(asModel,BorderLayout.NORTH);
		modelPanel.add(modelSetting,BorderLayout.CENTER);
		
		JPanel userSetting = new JPanel();
		userSetting.setLayout(new GridLayout(3,1));
		userSetting.setBorder(new EtchedBorder());
		
		JPanel modelPathPanel = new JPanel();
		modelPathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		modelPathPanel.add(new JLabel("Model File Path : "));
		modelPathPanel.add(modelFilePath);
		selectModelFile.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					selectModelFile();
				}
			}
		);
		modelPathPanel.add(selectModelFile);
		
		JPanel saveUserFileNamePanel = new JPanel();
		saveUserFileNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		saveUserFileNamePanel.add(new JLabel("Output User File Name : "));
		saveUserFileNamePanel.add(savedUserFileName);
		
		JPanel saveResultFileNamePanel = new JPanel();
		saveResultFileNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		saveResultFileNamePanel.add(new JLabel("Output Converted File Name : "));
		saveResultFileNamePanel.add(savedResultFileName);
		
		userSetting.add(modelPathPanel);
		userSetting.add(saveUserFileNamePanel);
		userSetting.add(saveResultFileNamePanel);
		userSetting.setBorder(new EtchedBorder());
		
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());
		userPanel.add(asUser,BorderLayout.NORTH);
		userPanel.add(userSetting,BorderLayout.CENTER);
		
		settingPanel.add(modelPanel);
		settingPanel.add(userPanel);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton ok = new JButton("OK");
		ok.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					close(true);
				}
			}
		);
		JButton cancel = new JButton("CANCEL");
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					close(false);
				}
			}
		);
		
		buttons.add(ok);
		buttons.add(cancel);
		
		container.add(commonSetting,BorderLayout.NORTH);
		container.add(settingPanel,BorderLayout.CENTER);
		container.add(buttons,BorderLayout.SOUTH);
		
		setMode();
		pack();
	}
	
	public void selectPath(){
		if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			path.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}
	public void selectModelFile(){
		if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			modelFilePath.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}
	
	
}
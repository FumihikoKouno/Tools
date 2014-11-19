package Tools.Point3D;

import java.util.ArrayList;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import javax.swing.border.EtchedBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Tools.Data.*;

public class Option extends JDialog{
	public static int perspective = 0;
	public static int fps = 30;
	public static Vec3D rot = new Vec3D();
	public static Vec3D onePP = new Vec3D(0,0,500);
	public static boolean viewR = false;
	public static boolean viewO = false;
	
	public static final int NO_PERSPECTIVE = 0;
	public static final int ONE_POINT_PERSPECTIVE = 1;
	public static final int TWO_POINT_PERSPECTIVE = 2;

	private JTextField rotX = new JTextField(Double.toString(rot.getX()),5);
	private JTextField rotY = new JTextField(Double.toString(rot.getY()),5);
	private JTextField rotZ = new JTextField(Double.toString(rot.getZ()),5);

	private JTextField oneX = new JTextField(Double.toString(onePP.getX()),5);
	private JTextField oneY = new JTextField(Double.toString(onePP.getY()),5);
	private JTextField oneZ = new JTextField(Double.toString(onePP.getZ()),5);

	private JTextField fpsText = new JTextField(Integer.toString(fps),5);

	private JRadioButton noPers;
	private JRadioButton onePers;
	private JRadioButton twoPers;

	private JRadioButton viewRot = new JRadioButton("View Rotate Origin Point", viewR);
	private JRadioButton viewOne = new JRadioButton("View One Point Persective Point", viewO);

	public Option(JFrame frame){
		super(frame,true);
		setTitle("Player Option");
		init();
	}
	
	public void setValues(){
		noPers.setSelected(perspective==NO_PERSPECTIVE);
		onePers.setSelected(perspective==ONE_POINT_PERSPECTIVE);
		viewRot.setSelected(viewR);
		viewOne.setSelected(viewO);
		rotX.setText(Double.toString(rot.getX()));
		rotY.setText(Double.toString(rot.getY()));
		rotZ.setText(Double.toString(rot.getZ()));
		
		oneX.setText(Double.toString(onePP.getX()));
		oneY.setText(Double.toString(onePP.getY()));
		oneZ.setText(Double.toString(onePP.getZ()));
		
		fpsText.setText(Integer.toString(fps));
	}

	public void close(boolean b){
		if(b){
			if(noPers.isSelected()) perspective = NO_PERSPECTIVE;
			if(onePers.isSelected()) perspective = ONE_POINT_PERSPECTIVE;
			//		if(twoPers.isSelected()) perspective = TWO_POINT_PERSPECTIVE;
			
			viewR = viewRot.isSelected();
			viewO = viewOne.isSelected();
			
			try{
				double x = Double.parseDouble(rotX.getText());
				double y = Double.parseDouble(rotY.getText());
				double z = Double.parseDouble(rotZ.getText());
				rot = new Vec3D(x,y,z);
			}catch(NumberFormatException ne){
				setValues();
			}
			try{
				double x = Double.parseDouble(oneX.getText());
				double y = Double.parseDouble(oneY.getText());
				double z = Double.parseDouble(oneZ.getText());
				onePP = new Vec3D(x,y,z);
			}catch(NumberFormatException ne){
				setValues();
			}
			try{
				fps = Integer.parseInt(fpsText.getText());
			}catch(NumberFormatException ne){
				setValues();
			}
		}else{
			setValues();
		}
		setVisible(false);
	}

	public void init(){
		Container container = getContentPane();
		noPers = new JRadioButton("No Perspective", perspective==NO_PERSPECTIVE);
		onePers = new JRadioButton("One Point Perspective", perspective==ONE_POINT_PERSPECTIVE);
		//	twoPers = new JRadioButton("Two Point Perspective", perspective==TWO_POINT_PERSPCETIVE);
		ButtonGroup pers = new ButtonGroup();
		JPanel persPanel = new JPanel();
		persPanel.setBorder(new EtchedBorder());
		persPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		pers.add(noPers);
		pers.add(onePers);
		persPanel.add(noPers);
		persPanel.add(onePers);

		JPanel viewPointPanel = new JPanel();
		viewPointPanel.setBorder(new EtchedBorder());
		viewPointPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewPointPanel.add(viewRot);
		viewPointPanel.add(viewOne);

		JButton ok = new JButton("OK");
		ok.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					close(true);
				}
			}
		);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					close(false);
				}
			}
		);

		JPanel btns = new JPanel();
		btns.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btns.add(ok);
		btns.add(cancel);

		JLabel rotOrigin = new JLabel("Rotate Origin : (");
		JLabel rotEnd = new JLabel(")");

		JPanel rotPanel = new JPanel();
		rotPanel.setBorder(new EtchedBorder());
		rotPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rotPanel.add(rotOrigin);
		rotPanel.add(rotX);
		rotPanel.add(rotY);
		rotPanel.add(rotZ);
		rotPanel.add(rotEnd);

		JLabel oneOrigin = new JLabel("One Vanishing Point : (");
		JLabel oneEnd = new JLabel(")");

		JPanel onePanel = new JPanel();
		onePanel.setBorder(new EtchedBorder());
		onePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		onePanel.add(oneOrigin);
		onePanel.add(oneX);
		onePanel.add(oneY);
		onePanel.add(oneZ);
		onePanel.add(oneEnd);
		
		JPanel fpsPanel = new JPanel();
		fpsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fpsPanel.add(new JLabel("fps:"));
		fpsPanel.add(fpsText);
		
		container.setLayout(new GridLayout(6,1));

		container.add(fpsPanel);
		container.add(persPanel);
		container.add(rotPanel);
		container.add(onePanel);
		container.add(viewPointPanel);
		container.add(btns);
		pack();
	}
}

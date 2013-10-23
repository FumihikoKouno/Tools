package Tools.Point3D;

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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Tools.Data.*;

public class Option extends JDialog implements ActionListener{
    public static int perspective = 0;
    public static Vec3D rot = new Vec3D();
    public static Vec3D onePP = new Vec3D();
    public static boolean viewR = false;
    public static boolean viewO = false;
    public static boolean shareRot = true;

    public static final int NO_PERSPECTIVE = 0;
    public static final int ONE_POINT_PERSPECTIVE = 1;
    public static final int TWO_POINT_PERSPECTIVE = 2;

    private JTextField rotX = new JTextField(Double.toString(rot.getX()),5);
    private JTextField rotY = new JTextField(Double.toString(rot.getY()),5);
    private JTextField rotZ = new JTextField(Double.toString(rot.getZ()),5);

    private JTextField oneX = new JTextField(Double.toString(onePP.getX()),5);
    private JTextField oneY = new JTextField(Double.toString(onePP.getY()),5);
    private JTextField oneZ = new JTextField(Double.toString(onePP.getZ()),5);

    private JRadioButton noPers;
    private JRadioButton onePers;
    private JRadioButton twoPers;

    private JRadioButton viewRot;
    private JRadioButton viewOne;
    private JRadioButton share = new JRadioButton("Share Rotation",shareRot);

    Option(){
	super();
	init();
    }
    Option(Frame frame){
	super(frame,true);
       	init();
    }

    public void actionPerformed(ActionEvent e){
	String s = e.getActionCommand();
	if(s == "OK"){
	    if(noPers.isSelected()) perspective = NO_PERSPECTIVE;
	    if(onePers.isSelected()) perspective = ONE_POINT_PERSPECTIVE;
	    //	    if(twoPers.isSelected()) perspective = TWO_POINT_PERSPECTIVE;
	    
	    viewR = viewRot.isSelected();
	    viewO = viewOne.isSelected();

	    shareRot = share.isSelected();
	    try{

	    }catch(NumberFormatException ne){
	    }
	    try{
		double x = Double.parseDouble(rotX.getText());
		double y = Double.parseDouble(rotY.getText());
		double z = Double.parseDouble(rotZ.getText());
		rot = new Vec3D(x,y,z);
	    }catch(NumberFormatException ne){
	    }
	    try{
		double x = Double.parseDouble(oneX.getText());
		double y = Double.parseDouble(oneY.getText());
		double z = Double.parseDouble(oneZ.getText());
		onePP = new Vec3D(x,y,z);
	    }catch(NumberFormatException ne){
	    }
	    setVisible(false);
	}
	if(s == "Cancel"){
	    setVisible(false);
	}
    }

    public void init(){
	Container container = getContentPane();
        noPers = new JRadioButton("No Perspective", perspective==NO_PERSPECTIVE);
	onePers = new JRadioButton("One Point Perspective", perspective==ONE_POINT_PERSPECTIVE);
	//	twoPers = new JRadioButton("Two Point Perspective", perspective==TWO_POINT_PERSPCETIVE);
	ButtonGroup pers = new ButtonGroup();
	JPanel persPanel = new JPanel();
	persPanel.setLayout(new FlowLayout());

	pers.add(noPers);
	pers.add(onePers);
	persPanel.add(noPers);
	persPanel.add(onePers);

        viewRot = new JRadioButton("View Rotate Origin Point", viewR);
	viewOne = new JRadioButton("View One Point Persective Point", viewO);

	JPanel viewPointPanel = new JPanel();
	viewPointPanel.setLayout(new FlowLayout());
	viewPointPanel.add(viewRot);
	viewPointPanel.add(viewOne);

	JButton ok = new JButton("OK");
	ok.addActionListener(this);
	JButton cancel = new JButton("Cancel");
	cancel.addActionListener(this);

	JPanel btns = new JPanel();
	btns.add(ok);
	btns.add(cancel);

	JPanel sharePanel = new JPanel();
	sharePanel.add(share);

	JLabel rotOrigin = new JLabel("Rotate Origin : (");
	JLabel rotEnd = new JLabel(")");

	JPanel rotPanel = new JPanel();
	rotPanel.setLayout(new FlowLayout());
	rotPanel.add(rotOrigin);
	rotPanel.add(rotX);
	rotPanel.add(rotY);
	rotPanel.add(rotZ);
	rotPanel.add(rotEnd);

	JLabel oneOrigin = new JLabel("One Origin : (");
	JLabel oneEnd = new JLabel(")");

	JPanel onePanel = new JPanel();
	onePanel.setLayout(new FlowLayout());
	onePanel.add(oneOrigin);
	onePanel.add(oneX);
	onePanel.add(oneY);
	onePanel.add(oneZ);
	onePanel.add(oneEnd);

	container.setLayout(new GridLayout(6,1));

	container.add(persPanel);
	container.add(rotPanel);
	container.add(onePanel);
	container.add(viewPointPanel);
	container.add(sharePanel);
	container.add(btns);
	pack();
    }
}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import parser.Parser;
import viewer.Viewer;
import expression.Node;

public class Test extends JFrame{
	
	private JTextField x = new JTextField("input x");
	private JTextField y = new JTextField("input y");
	private JTextField z = new JTextField("input z");
	private JTextField v = new JTextField("input variable");
	private JTextField p = new JTextField("input parameter");
	private ThreeDGraphViewer viewer = new ThreeDGraphViewer();
	private JTextArea tf = new JTextArea(30,10);
	
	public void init()
	{		
		JPanel texts = new JPanel();
		JPanel xPane = new JPanel();
		xPane.add(new JLabel("x : "));
		xPane.add(x);
		JPanel yPane = new JPanel();
		yPane.add(new JLabel("y : "));
		yPane.add(y);
		JPanel zPane = new JPanel();
		zPane.add(new JLabel("z : "));
		zPane.add(z);
		JPanel vPane = new JPanel();
		vPane.add(new JLabel("variable : "));
		vPane.add(v);
		JPanel pPane = new JPanel();
		pPane.add(new JLabel("parameter : "));
		pPane.add(p);
		texts.setLayout(new FlowLayout());
		texts.add(xPane);
		texts.add(yPane);
		texts.add(zPane);
		texts.add(vPane);
		texts.add(pPane);
		JButton draw = new JButton("AddGraph");
		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expX = x.getText();
				String expY = y.getText();
				String expZ = z.getText();
				String[] str = {expX,expY,expZ};
				viewer.addAxes(str, 0.0, 10.0, 0.1, 0.5, Color.BLUE);
				repaint();
			}
		});

		JButton addVariable = new JButton("AddVariable");
		addVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expV = v.getText();
				String[] divide = expV.split(":");
				viewer.addVariable(divide[0], divide[1]);
				tf.setText(viewer.toString());
			}
		});
		
		JButton addParameter = new JButton("AddParameter");
		addParameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expP = p.getText();
				String[] divide = expP.split(":");
				viewer.addParameter(divide[0], divide[1]);
				tf.setText(viewer.toString());
			}
		});
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.clear();
				tf.setText(viewer.toString());
			}
		});
		
		JButton changeMode = new JButton("changeMode");
		changeMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.viewInterval(!viewer.getIntervalMode());
				tf.setText(viewer.toString());
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.add(draw);
		buttons.add(addVariable);
		buttons.add(addParameter);
		buttons.add(reset);
		buttons.add(changeMode);
		
		add(viewer,BorderLayout.NORTH);
		add(texts,BorderLayout.CENTER);
		add(tf,BorderLayout.WEST);
		add(buttons,BorderLayout.SOUTH);
		
		repaint();
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		new Test().init();
	}

}
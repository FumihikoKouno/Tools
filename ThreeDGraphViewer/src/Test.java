import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
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
	
	private JTextField xTextField = new JTextField("t");
	private JTextField yTextField = new JTextField("t");
	private JTextField zTextField = new JTextField("t");
	private JTextField varTextField = new JTextField();
	private JTextField parTextField = new JTextField("p[a,0,1]:[1,5]");
	private JTextField lowerTimeTextField = new JTextField("0");
	private JTextField upperTimeTextField = new JTextField("5");
	private JTextField timeIntervalTextField = new JTextField("0.2");
	private JTextField parIntervalTextField = new JTextField("0.2");
	private JTextField colorTextField = new JTextField("0000ff");
	private ThreeDGraphViewer viewer = new ThreeDGraphViewer();
	private JTextArea consoleArea = new JTextArea();
	JButton changeMode = new JButton("Mode:single");
	
	public void init()
	{
		// texts
		xTextField.setPreferredSize(new Dimension(75,25));
		yTextField.setPreferredSize(new Dimension(75,25));
		zTextField.setPreferredSize(new Dimension(75,25));
		varTextField.setPreferredSize(new Dimension(75,25));
		parTextField.setPreferredSize(new Dimension(75,25));
		lowerTimeTextField.setPreferredSize(new Dimension(50,25));
		upperTimeTextField.setPreferredSize(new Dimension(50,25));
		timeIntervalTextField.setPreferredSize(new Dimension(50,25));
		parIntervalTextField.setPreferredSize(new Dimension(50,25));
		colorTextField.setPreferredSize(new Dimension(50,25));
		JPanel texts = new JPanel();
		JPanel xPane = new JPanel();
		xPane.setLayout(new GridLayout(1,2));
		xPane.add(new JLabel("x : "));
		xPane.add(xTextField);
		JPanel yPane = new JPanel();
		yPane.add(new JLabel("y : "));
		yPane.setLayout(new GridLayout(1,2));
		yPane.add(yTextField);
		JPanel zPane = new JPanel();
		zPane.setLayout(new GridLayout(1,2));
		zPane.add(new JLabel("z : "));
		zPane.add(zTextField);
		JPanel tPane = new JPanel();
		tPane.setLayout(new GridLayout(1,3));
		tPane.add(new JLabel("t : "));
		tPane.add(lowerTimeTextField);
		tPane.add(upperTimeTextField);
		JPanel tiPane = new JPanel();
		tiPane.setLayout(new GridLayout(1,2));
		tiPane.add(new JLabel("t interval : "));
		tiPane.add(timeIntervalTextField);
		JPanel piPane = new JPanel();
		piPane.setLayout(new GridLayout(1,2));
		piPane.add(new JLabel("par interval : "));
		piPane.add(parIntervalTextField);
		JPanel vPane = new JPanel();
		vPane.setLayout(new GridLayout(1,2));
		vPane.add(new JLabel("variable : "));
		vPane.add(varTextField);
		JPanel pPane = new JPanel();
		pPane.setLayout(new GridLayout(1,2));
		pPane.add(new JLabel("parameter : "));
		pPane.add(parTextField);
		JPanel cPane = new JPanel();
		cPane.setLayout(new GridLayout(1,2));
		cPane.add(new JLabel("color : "));
		cPane.add(colorTextField);
		texts.setLayout(new GridLayout(3,3));
		texts.add(xPane);
		texts.add(yPane);
		texts.add(zPane);
		texts.add(vPane);
		texts.add(pPane);
		texts.add(tPane);
		texts.add(tiPane);
		texts.add(piPane);
		texts.add(cPane);
		
		// buttons
		JButton draw = new JButton("AddGraph");
		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expX = xTextField.getText();
				String expY = yTextField.getText();
				String expZ = zTextField.getText();
				String lt = lowerTimeTextField.getText();
				String ut = upperTimeTextField.getText();
				double ti = Double.parseDouble(timeIntervalTextField.getText());
				double pi = Double.parseDouble(parIntervalTextField.getText());
				Color c = new Color(Integer.parseInt(colorTextField.getText(),16));
				String[] str = {expX,expY,expZ};
				viewer.addAxes(str, lt, ut, ti, pi, c);
				repaint();
			}
		});

		JButton addVariable = new JButton("RegisterVariable");
		addVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expV = varTextField.getText();
				String[] divide = expV.split(":");
				viewer.addVariable(divide[0], divide[1]);
				consoleArea.setText(viewer.toString());
			}
		});
		
		JButton addParameter = new JButton("RegisterParameter");
		addParameter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String expP = parTextField.getText();
				String[] divide = expP.split(":");
				viewer.addParameter(divide[0], divide[1]);
				consoleArea.setText(viewer.toString());
			}
		});
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.clear();
				consoleArea.setText(viewer.toString());
			}
		});
		
		JButton viewReset = new JButton("ResetView");
		viewReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewer.resetView();
				consoleArea.setText(viewer.toString());
			}
		});
		
		changeMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(viewer.getIntervalMode())
				{
					viewer.viewInterval(false);
					viewer.viewSeveralLines(false);
					changeMode.setText("Mode:single");
					return;
				}
				if(viewer.getSeveralMode())
				{
					viewer.viewSeveralLines(false);
					viewer.viewInterval(true);
					changeMode.setText("Mode:interval");
					return;
				}
				viewer.viewSeveralLines(true);
				changeMode.setText("Mode.several");
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,3));
		buttons.add(draw);
		buttons.add(addVariable);
		buttons.add(addParameter);
		buttons.add(reset);
		buttons.add(changeMode);
		buttons.add(viewReset);
		
		JPanel inputs = new JPanel();
		
		inputs.setLayout(new GridLayout(2,1));
		inputs.add(texts);
		inputs.add(buttons);
		
		consoleArea.setPreferredSize(new Dimension(300,500));
		
		add(viewer,BorderLayout.CENTER);
		add(consoleArea,BorderLayout.EAST);
		add(inputs,BorderLayout.SOUTH);
		
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

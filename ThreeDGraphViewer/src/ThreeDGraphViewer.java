import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parser.Parser;
import viewer.Viewer;
import expression.Node;
import expression.Parameter;
import expression.Variable;

public class ThreeDGraphViewer extends JPanel{
	
	Viewer viewer;
	Parser parser;
	ArrayList<Variable> variables;
	ArrayList<Parameter> parameters;
	
	ArrayList<Variable[]> axes;
	
	int nextSendIndex = 0;
	
	public ThreeDGraphViewer()
	{
		init();
	}
	
	public void init()
	{
		axes = new ArrayList<Variable[]>();
		viewer = new Viewer();
		parser = new Parser();
		variables = new ArrayList<Variable>();
		parameters = new ArrayList<Parameter>();
		add(viewer);
	}
	
	public void clear()
	{
		viewer.clear();
		axes.clear();
		parser.reset();
		variables.clear();
		parameters.clear();
	}
	
	public void setParameters()
	{
		parameters = parser.getParameters();
	}
	
	public void viewInterval(boolean b)
	{
		viewer.viewInterval(b);
	}
	
	public boolean getIntervalMode()
	{
		return viewer.getIntervalMode();
	}
	
	public void setVariables()
	{
		variables = parser.getVariables();
	}
	
	public boolean addAxes(String[] name, double lt, double ut, double iv, double piv, Color c)
	{
		return setAxes(Integer.MAX_VALUE,name,lt,ut,iv,piv,c);
	}
	
	public boolean setAxes(int index, String[] name, double lt, double ut, double iv, double piv, Color c)
	{
		Variable[] axis = new Variable[3];
		for(int i = 0; i < name.length; i++)
		{
			parser.setString(name[i]);
			Variable var = (Variable)parser.variable();
			if(var!=null && parser.finished())
			{
				for(int j = 0; j < variables.size(); j++)
				{
					if(var.equals(variables.get(j)))
					{
						axis[i] = variables.get(j);
					}
				}
			}
		}
		if(axis[0]!=null
		&& axis[1]!=null
		&& axis[2]!=null)
		{
			if(axes.size()<=index)
			{
				axes.add(axis);
				viewer.addGraph(axis,lt,ut,iv,piv,c);
			}
			else
			{
				axes.set(index,axis);
				viewer.setGraph(index,axis,lt,ut,iv,piv,c);
			}
			return true;
		}
		return false;
	}
	
	public void addParameter(String name, String exp)
	{
		String parameterDefinition = name + ":" + exp;
		parser.setString(parameterDefinition);
		parser.parseParameter();
		if(!parser.finished())
		{
			System.err.println("Parsing error : " + parameterDefinition);
			return;
		}
		setParameters();
	}
	
	public void addVariable(String name, String exp)
	{
		String variableDefinition = name + ":" + exp;
		parser.setString(variableDefinition);
		parser.parseVariable();
		if(!parser.finished())
		{
			System.err.println("Parsing error : " + variableDefinition);
			return;
		}
		setVariables();
	}
	
	public String toString()
	{
		String lineSeparator = System.getProperty("line.separator");
		String str = "********** Parameters **********" + lineSeparator ;
		for(int i = 0; i < parameters.size(); i++)
		{
			str += parameters.get(i) + lineSeparator;
		}
		str += lineSeparator + "********** Variables *********" + lineSeparator;
		for(int i = 0; i < variables.size(); i++)
		{
			str += variables.get(i) + lineSeparator;
		}
		return str;
	}
}

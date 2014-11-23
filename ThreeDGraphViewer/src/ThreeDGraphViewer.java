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
	ArrayList<Parser> parser;
	ArrayList<Variable> variables;
	ArrayList<Parameter> parameters;
	
	int currentCase = 1;
	
	ArrayList<Node[]> axes;
	
	int nextSendIndex = 0;
	
	public ThreeDGraphViewer()
	{
		init();
	}
	
	public void init()
	{
		axes = new ArrayList<Node[]>();
		viewer = new Viewer();
		parser = new ArrayList<Parser>();
		parser.add(new Parser());
		variables = new ArrayList<Variable>();
		parameters = new ArrayList<Parameter>();
		add(viewer);
	}
	
	public void clear()
	{
		viewer.clear();
		axes.clear();
		parser.clear();
		parser.add(new Parser());
		variables.clear();
		parameters.clear();
	}
	
	public void resetView()
	{
		viewer.resetViewing();
	}
	
	public void setParameters()
	{
		parameters = parser.get(currentCase-1).getParameters();
	}
	
	public void viewInterval(boolean b)
	{
		viewer.viewInterval(b);
	}
	
	public void viewSeveralLines(boolean b)
	{
		viewer.viewSeveralLines(b);
	}
	
	public boolean getIntervalMode()
	{
		return viewer.getIntervalMode();
	}
	
	public boolean getSeveralMode()
	{
		return viewer.getSeveralMode();
	}
	
	public void setVariables()
	{
		variables = parser.get(currentCase-1).getVariables();
	}
	
	public boolean addAxes(String[] name, String lt, String ut, double iv, double piv, Color c)
	{
		return setAxes(Integer.MAX_VALUE,name,lt,ut,iv,piv,c);
	}
	
	public boolean setAxes(int index, String[] name, String lt, String ut, double iv, double piv, Color c)
	{
		Node[] axis = new Node[3];
		for(int i = 0; i < name.length; i++)
		{
			parser.get(currentCase-1).setString(name[i]);
			Node n = parser.get(currentCase-1).expression();
			if(n!=null && parser.get(currentCase-1).finished())
			{
				for(int j = 0; j < variables.size(); j++)
				{
					if(n.equals(variables.get(j)))
					{
						axis[i] = variables.get(j);
						break;
					}
				}
				if(axis[i]==null)
				{
					axis[i] = n;
				}
			}
		}
		parser.get(currentCase-1).setString(lt);
		Node lowerTime = parser.get(currentCase-1).expression();
		parser.get(currentCase-1).setString(ut);
		Node upperTime = parser.get(currentCase-1).expression();
		if(axis[0]!=null
		&& axis[1]!=null
		&& axis[2]!=null)
		{
			if(axes.size()<=index)
			{
				axes.add(axis);
				viewer.addGraph(axis,lowerTime,upperTime,iv,piv,c);
			}
			else
			{
				axes.set(index,axis);
				viewer.setGraph(index,axis,lowerTime,upperTime,iv,piv,c);
			}
			return true;
		}
		return false;
	}
	public void setParameterRatio(String par, double r)
	{
		parser.get(currentCase-1).setString(par);
		Parameter p = (Parameter)parser.get(currentCase-1).parameter();
		if(p != null) setParameterRatio(p,r);
	}
	
	public void setParameterRatio(Parameter par, double r)
	{
		for(int i = 0; i < parameters.size(); i++)
		{
			System.out.println(parameters.get(i));
			if(par.equals(parameters.get(i)))
			{
				parameters.get(i).setParameter(par,r);
				resetView();
				return;
			}
		}
	}
	
	public void setParameterRatio(String name, int der, int id, double r)
	{
		Parameter par = new Parameter(name, der, id, null);
		setParameterRatio(par,r);
	}
	
	public void addParameter(String name, String exp)
	{
		String parameterDefinition = name + ":" + exp;
		parser.get(currentCase-1).setString(parameterDefinition);
		parser.get(currentCase-1).parseParameter();
		if(!parser.get(currentCase-1).finished())
		{
			System.err.println("Parsing error : " + parameterDefinition);
			return;
		}
		setParameters();
	}
	
	public void setCase(int id)
	{
		if(id > parser.size())
		{
			System.err.println("Case " + id + " is not registerd");
			return;
		}
		currentCase = id;
		setVariables();
		setParameters();
	}
	
	public void makeNewCase()
	{
		parser.add(new Parser());
		currentCase=parser.size();
		setVariables();
		setParameters();
	}
	
	public void addVariable(String name, String exp)
	{
		String variableDefinition = name + ":" + exp;
		parser.get(currentCase-1).setString(variableDefinition);
		parser.get(currentCase-1).parseVariable();
		if(!parser.get(currentCase-1).finished())
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

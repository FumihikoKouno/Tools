package parser.python_node;

import java.util.List;

public class Node
{
	protected List<Node> args;
	protected int argMax;
	
	public Node()
	{
		argMax = Integer.MAX_VALUE;
	}
	
	public List<Node> getArgs()
	{
		return args;
	}
	
	public Node search(Node n)
	{
		if(this.equals(n))
		{
			return this;
		}
		return null;
	}
	public void addArg(Node n)
	{
		if(args.size() < argMax)
		{
			args.add(n);
		}
		else
		{
			throw new IndexOutOfBoundsException(
					"The number of args for " + 
					this.getClass() + 
					" is full.");
		}
	}
	
	public boolean removeArg(Node n)
	{
		return args.remove(n);
	}
	
	public Node getArg(int i)
	{
		return args.get(i);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this.getClass() == obj.getClass())
		{
			Node node = (Node)obj;
			boolean hasSameArgs = true;
			for(int argI = 0; argI < args.size(); argI++)
			{
				hasSameArgs = hasSameArgs && 
						getArg(argI).equals(node.getArg(argI));
				if(!hasSameArgs)
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
}

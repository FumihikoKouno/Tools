package parser.python_node;

public class Method extends Node {
	public Method()
	{
		argMax = Integer.MAX_VALUE;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		int arg_size = 0;
		int default_arg_size = 0;
		boolean variable_argument = false;
		boolean keyword_argument = false;
		
		for(Node arg : args)
		{
			if(arg instanceof Argument)
			{
				arg_size++;
			}
		}
		// TODO implement
		return true;
	}
}

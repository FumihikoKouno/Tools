package parser;
import parser.Lexer;
import expression.*;
import java.util.ArrayList;;

public class Parser {
	private Lexer lexer;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	
	public Parser(String str)
	{
		lexer = new Lexer(str);
	}
	public boolean parseVariable()
	{
		int index = lexer.getPosition();
		Node var = variable();
		if(var!=null)
		{
			if(lexer.getToken()==Lexer.Token.COLON)
			{
				Node exp = expression();
				if(exp!=null)
				{
					for(Variable variable : variables)
					{
						if(variable.equals(var))
						{
							if(variable.getChild()==null)
							{
								variable.setChild(exp);
								return true;
							}
							else
							{
								System.out.println("Redefinition variable : " + var);
								return false;
							}
						}
					}
					Variable v = (Variable)var;
					v.setChild(exp);
					variables.add(v);
					return true;
				}
			}
		}
		lexer.setPosition(index);
		return false;
	}
	
	public boolean parseParameter()
	{
		int index = lexer.getPosition();
		Node param = parameter();
		if(param!=null)
		{
			if(lexer.getToken()==Lexer.Token.COLON)
			{
				Node iv = interval();
				if(iv!=null)
				{
					for(Parameter parameter : parameters)
					{
						if(parameter.equals(param))
						{
							if(parameter.getChild()==null)
							{
								parameter.setChild(iv);
								return true;
							}
							else
							{
								System.out.println("Redefinition parameter : " + param);
								return false;
							}
						}
					}
					Parameter par = (Parameter)param;
					par.setChild(iv);
					parameters.add(par);
					return true;
				}
			}
		}
		lexer.setPosition(index);
		return false;
	}
	
	public boolean finished()
	{
		return lexer.finished();
	}
	
	public void setString(String str)
	{
		lexer.setString(str);
		reset();
	}
	
	public void reset()
	{
		lexer.setPosition(0);
	}
	
	/**
	 * expression := term
	 *             | term "+" expression
	 *             | term "-" expression
	 */
	public Node expression()
	{
		Node lhs = term();
		if(lhs!=null)
		{
			int index = lexer.getPosition();
			if(lexer.getToken()==Lexer.Token.PLUS)
			{
				Node rhs = expression(); 
				if(rhs!=null)
				{
					return new Add(lhs,rhs);
				}
			}
			lexer.setPosition(index);
			if(lexer.getToken()==Lexer.Token.MINUS)
			{
				Node rhs = expression(); 
				if(rhs!=null)
				{
					return new Sub(lhs,rhs);
				}
			}
			lexer.setPosition(index);
		}
		return lhs;
	}
	
	/**
	 * term := power
	 *       | power "*" term
	 *       | power "/" term
	 */
	public Node term()
	{
		Node lhs = power();
		if(lhs!=null)
		{
			int index = lexer.getPosition();
			if(lexer.getToken()==Lexer.Token.TIMES)
			{
				Node rhs = term(); 
				if(rhs!=null)
				{
					return new Mul(lhs,rhs);
				}
			}
			lexer.setPosition(index);
			if(lexer.getToken()==Lexer.Token.DIVIDE)
			{
				Node rhs = term(); 
				if(rhs!=null)
				{
					return new Div(lhs,rhs);
				}
			}
			lexer.setPosition(index);
		}
		return lhs;
	}
	
	/**
	 * power := negaposi "^" power
	 *        | negaposi
	 */
	public Node power()
	{
		Node fact = negaposi();
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.POW)
		{
			Node pow = power();
			if(pow!=null)
			{
				return new Pow(fact,pow);
			}
		}
		lexer.setPosition(index);
		return fact;
	}
	/**
	 * negaposi = "+" factor
	 *          | "-" factor
	 *          | factor
	 */
	public Node negaposi()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.PLUS)
		{
			Node fact = factor();
			if(fact!=null)
			{
				return new Positive(fact);
			}
		}
		lexer.setPosition(index);
		if(lexer.getToken()==Lexer.Token.MINUS)
		{
			Node fact = factor();
			if(fact!=null)
			{
				return new Negative(fact);
			}
		}
		lexer.setPosition(index);
		return factor();
	}
	
	/**
	 * factor := function
	 *         | number
	 *         | parameter
	 *         | t
	 *         | variable
	 */
	public Node factor()
	{
		Node ret;
		// function
		ret = function();
		if(ret!=null) return ret;
		ret = number();
		if(ret!=null) return ret;
		ret = parameter();
		if(ret!=null) return ret;
		ret = t();
		if(ret!=null) return ret;
		ret = variable();
		if(ret!=null) return ret;
		return null;
	}
	
	/**
	 * function := "sin" "[" expression "]"
	 *           | "cos" "[" expression "]"
	 *           | "tan" "[" expression "]"
	 *           | "log" "[" expression "]"
	 */
	public Node function()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.IDENTIFIER)
		{
			String name = lexer.getString();
			if(lexer.getToken()==Lexer.Token.LEFT_BRACKETS)
			{
				Node exp = expression();
				if(exp!=null)
				{
					if(lexer.getToken()==Lexer.Token.RIGHT_BRACKETS)
					{
						if(name.equals("sin")) return new Sin(exp);
						if(name.equals("cos")) return new Cos(exp);
						if(name.equals("tan")) return new Tan(exp);
						if(name.equals("log")) return new Log(exp);
					}
				}
			}
		}
		lexer.setPosition(index);
		return null;
	}
	
	/**
	 * parameter := "p" "[" variable "," integer "," integer "]"
	 */
	public Node parameter()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.IDENTIFIER)
		{
			if(lexer.getString().equals("p"))
			{
				if(lexer.getToken()==Lexer.Token.LEFT_BRACKETS)
				{
					Node var = variable();
					if(var!=null)
					{
						String name = lexer.getString();
						if(lexer.getToken()==Lexer.Token.COMMA)
						{
							if(lexer.getToken()==Lexer.Token.NUM
							&& !lexer.getString().contains("."))
							{
								int derivative = Integer.parseInt(lexer.getString());
								if(lexer.getToken()==Lexer.Token.COMMA)
								{
									if(lexer.getToken()==Lexer.Token.NUM
									&& !lexer.getString().contains("."))
									{
										int phase = Integer.parseInt(lexer.getString());
										if(lexer.getToken()==Lexer.Token.RIGHT_BRACKETS)
										{
											Parameter parsed = new Parameter(name,derivative,phase,null);
											for(Parameter pars : parameters)
											{
												if(pars.equals(parsed))
												{
													return pars;
												}
											}
											parameters.add(parsed);
											return parsed;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		lexer.setPosition(index);
		return null;
	}
	
	
	/**
	 * variable := identifier
	 */
	public Node variable()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.IDENTIFIER)
		{
			Variable v = new Variable(lexer.getString(),null);
			for(Variable var : variables)
			{
				if(var.equals(v)) return var;
			}
			variables.add(v);
			return v;
		}
		lexer.setPosition(index);
		return null;
	}
	
	/**
	 * t := "t"
	 */
	public Node t()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.IDENTIFIER
		&& lexer.getString().equals("t"))
		{
			return new T();
		}
		lexer.setPosition(index);
		return null;
	}
	
	/**
	 * number
	 */
	public Node number()
	{
		int index = lexer.getPosition();
		if(lexer.getToken()==Lexer.Token.NUM)
		{
			return new Num(Double.parseDouble(lexer.getString()));
		}
		lexer.setPosition(index);
		return null;
	}
	
	/**
	 * interval := ("(" | "[") expression "," expression ( ")" | "]" )
	 */
	public Node interval()
	{
		int index = lexer.getPosition();
		Lexer.Token lp = lexer.getToken();
		if(lp==Lexer.Token.LEFT_PARENTHESIS
		|| lp==Lexer.Token.LEFT_BRACKETS)
		{
			Node exp1 = expression();
			if(exp1!=null)
			{
				if(lexer.getToken()==Lexer.Token.COMMA)
				{
					Node exp2 = expression();
					if(exp2!=null)
					{
						Lexer.Token rp = lexer.getToken();
						if(rp==Lexer.Token.RIGHT_PARENTHESIS
						|| rp==Lexer.Token.RIGHT_BRACKETS)
						{
							Interval iv = new Interval(exp1,exp2);
							if(lp==Lexer.Token.LEFT_PARENTHESIS) iv.lhsClosed(false);
							else iv.lhsClosed(true);
							if(rp==Lexer.Token.RIGHT_PARENTHESIS) iv.rhsClosed(false);
							else iv.rhsClosed(true);
							return iv;
						}
					}
				}
			}
		}
		lexer.setPosition(index);
		return null;
	}
}

import java.io.File;
import java.io.FileNotFoundException;

import parser.Lexer;

public class Test {
	public static void main(String[] args)
	{
		try {
			Lexer lexer = new Lexer(new File("src/Test.java"));
			while(lexer.hasNext())
			{
				double a_2 = 3.24;
				System.out.print(lexer.getToken().toString());
				System.out.print(":");
				System.out.println(lexer.getTokenString());
				lexer.next();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

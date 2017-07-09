import java.awt.Dimension;

import javax.swing.JFrame;

import utils.Common;
import utils.YamlToken;
import utils.YamlParser;
public class Frontend extends JFrame{
	private static final long serialVersionUID = 1L;

	public Frontend(){
		this.setPreferredSize(new Dimension(300, 300));
		this.add(Common.console);
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		YamlParser yamlParser = new YamlParser("test.txt");
		YamlToken token = yamlParser.getResult();
		Common.console.setText(token.toString());
	}
	
	public static void init(){
		Common.init();
	}
	
	public static void main(String[] args) {
		init();
		Frontend frontend = new Frontend();
	}

}

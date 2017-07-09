package panels;

import javax.swing.JPanel;

import utils.Common;
import utils.YamlParser;
import utils.YamlToken;

public class RestAPIListPanel extends JPanel{
	YamlToken yamlToken;
	public RestAPIListPanel(){
		YamlParser yamlParser = new YamlParser(Common.RESTAPI_FILENAME);
		yamlToken = yamlParser.getResult();
	}
}

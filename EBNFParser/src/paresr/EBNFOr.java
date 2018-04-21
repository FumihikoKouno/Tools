package paresr;

import java.util.ArrayList;
import java.util.List;

public class EBNFOr extends EBNFNode {
	private List<List<EBNFNode>> contents = new ArrayList<List<EBNFNode>>();
	
	public EBNFOr(List<List<EBNFNode>> contents){
		this.contents = contents;
	}
	
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}
	
	public List<List<EBNFNode>> getContents(){
		return contents;
	}
}

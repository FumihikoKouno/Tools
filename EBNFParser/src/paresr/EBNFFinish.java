package paresr;

public class EBNFFinish extends EBNFNode {
	private String content;
	public EBNFFinish(String content){
		this.content = content;
	}
	
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public String getContent(){
		return content;
	}
}

package parser;

public class EBNFOption extends EBNFNode {
	private EBNFNode content;
	public EBNFOption(EBNFNode content){
		this.content = content;
	}
	
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public EBNFNode getContent(){
		return content;
	}
}

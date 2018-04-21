package paresr;

public class EBNFRepeat extends EBNFNode {
	private EBNFNode content;
	public EBNFRepeat(EBNFNode content){
		this.content = content;
	}

	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public EBNFNode getContent(){
		return content;
	}
}

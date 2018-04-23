package parser;

public class EBNFTimes extends EBNFNode {
	private int count;
	private EBNFNode content;
	public EBNFTimes(int count, EBNFNode content){
		this.count = count;
		this.content = content;
	}

	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public int getCount(){
		return count;
	}

	public EBNFNode getContent(){
		return content;
	}
}

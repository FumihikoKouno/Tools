package parser;

public class EBNFExcept extends EBNFNode {
	private EBNFNode left;
	private EBNFNode right;
	public EBNFExcept(EBNFNode left, EBNFNode right){
		this.left = left;
		this.right = right;
	}
	
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public EBNFNode getLeft(){
		return left;
	}

	public EBNFNode getRight(){
		return right;
	}
}

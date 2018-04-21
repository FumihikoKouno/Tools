package paresr;

public class EBNFNode {
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}
}

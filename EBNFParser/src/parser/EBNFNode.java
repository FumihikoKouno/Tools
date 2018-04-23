package parser;

public abstract class EBNFNode {
	public abstract void accept(EBNFVisitor visitor);
}

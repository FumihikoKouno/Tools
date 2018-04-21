package paresr;


public class EBNFDefinition extends EBNFNode{
	private String name;
	private EBNFOr definitions;
	public EBNFDefinition(String name, EBNFOr definitions){
		this.name = name;
		this.definitions = definitions;
	}
	
	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}

	public String getName(){
		return name;
	}
	
	public EBNFOr getContents(){
		return definitions;
	}
}

package paresr;

public class EBNFDefinitionCall extends EBNFNode{
	private String name;
	public EBNFDefinitionCall(String name){
		this.name = name;
	}

	public void accept(EBNFVisitor visitor){
		visitor.visit(this);
	}
	
	public String getName(){
		return name;
	}
}

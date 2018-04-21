package paresr;

import java.util.List;

public class EBNFTreePrinter extends EBNFVisitor {
	private int indent = 0;
	
	private String addIndent(String str){
		String indentString = "";
		for(int index = 0; index < indent; index++){
			indentString = indentString + " ";
		}
		return indentString + str;
	}
	
	private void print(String str){
		System.out.println(str);
	}
	
	@Override
	public void visit(EBNFDefinition node){
		print(addIndent("def " + node.getName()));
		indent = indent + 2;
		node.getContents().accept(this);
		indent = indent - 2;
	}

	@Override
	public void visit(EBNFDefinitionCall node){
		print(addIndent("call " + node.getName()));
	}

	@Override
	public void visit(EBNFExcept node){
		node.getLeft().accept(this);
		indent = indent + 2;
		print(addIndent("-"));
		indent = indent - 2;
		node.getRight().accept(this);
	}

	@Override
	public void visit(EBNFFinish node){
		print(addIndent("\"" + node.getContent() + "\""));
	}

	@Override
	public void visit(EBNFOption node){
		print(addIndent("["));
		indent = indent + 2;
		node.getContent().accept(this);;
		indent = indent - 2;
		print(addIndent("]"));
	}

	@Override
	public void visit(EBNFRepeat node){
		print(addIndent("{"));
		indent = indent + 2;
		node.getContent().accept(this);
		indent = indent - 2;
		print(addIndent("}"));
	}

	@Override
	public void visit(EBNFTimes node){
		print(addIndent(String.valueOf(node.getCount()) + " * "));
		indent = indent + 2;
		node.getContent().accept(this);
		indent = indent - 2;
	}

	public void visit(EBNFOr node){
		int count = 0;
		List<List<EBNFNode>> orContents = node.getContents();
		for(List<EBNFNode> contents : orContents){
			count++;
			for(EBNFNode content : contents){
				content.accept(this);
			}
			if(count < orContents.size()){
				indent = indent + 2;
				print(addIndent("|"));
				indent = indent - 2;
			}
		}
	}
}

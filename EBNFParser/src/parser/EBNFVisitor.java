package parser;

import java.util.List;

public class EBNFVisitor {
	public void visit(EBNFNode node){
	}

	public void visit(EBNFDefinitionCall node){
	}
	
	public void visit(EBNFFinish node){
	}

	public void visit(EBNFDefinition node){
		node.getContents().accept(this);
	}

	public void visit(EBNFExcept node){
		node.getLeft().accept(this);
		node.getRight().accept(this);
	}

	public void visit(EBNFOption node){
		node.getContent().accept(this);
	}

	public void visit(EBNFRepeat node){
		node.getContent().accept(this);
	}

	public void visit(EBNFTimes node){
		node.getContent().accept(this);
	}

	public void visit(EBNFOr node){
		for(List<EBNFNode> contents : node.getContents()){
			for(EBNFNode content : contents){
				content.accept(this);
			}
		}
	}
}

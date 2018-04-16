package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class OperationNode{
	
	public enum OperationType{
		IF_THEN,
		IF_ELSE,
		IF_NOT,
		LOOP_FIRST,
		LOOP_MID,
		LOOP_FINAL,
		LOOP_NOT,
		TRY,
		CATCH,
		CASE,
		METHOD,
		ROOT,
	};
	
	private int id;
	private OperationType type;
	private ASTNode node;
	private List<OperationNode> children;
	
	public OperationNode(int operationId, ASTNode operationAstNode, OperationType operationType){
		this.id = operationId;
		this.type = operationType;
		this.node = operationAstNode;
		children = new ArrayList<OperationNode>();
	}
	
	public int getId(){
		return this.id;
	}
	
	public OperationType getType(){
		return this.type;
	}
	
	public ASTNode getNode(){
		return this.node;
	}
	
	public boolean addChild(OperationNode childNode){
		return this.children.add(childNode);
	}
	
	public List<OperationNode> getChildren(){
		return this.children;
	}
	
}

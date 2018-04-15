package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class OperationRouteVisitor extends ASTVisitor{
	
	private static int operationId = 0;

	@Override
	public boolean visit(DoStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(EnhancedForStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(ForStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(IfStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(SwitchCase node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(TryStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(WhileStatement node){
		setDepsMap(node);
		return super.visit(node);
	}
	@Override
	public boolean visit(MethodDeclaration node){
		setDepsMap(node);
		return super.visit(node);
	}
	
	private void setDepsMap(ASTNode node){
		operationId = operationId + 1;
		if (Common.javaDeps == null){
			Map<ASTNode, String> rootNode = new HashMap<ASTNode, String>();
			rootNode.put(node.getRoot(), "Root");
			Common.javaDeps = new MultipleLinkedList<Map<ASTNode, String>>(rootNode);
		}
		if(Common.checkedNode == null){
			Common.checkedNode = new ArrayList<ASTNode>();
			Common.checkedNode.add(node.getRoot());
		}
		
		List<Map<ASTNode, String>> addList = new ArrayList<Map<ASTNode, String>>();
		Map<ASTNode, String> addMap = null;
		if(node instanceof MethodDeclaration){
			Common.checkedNode.add(node);
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "Method");
			addList.add(addMap);
		}
		if(node instanceof DoStatement ||
				node instanceof EnhancedForStatement ||
				node instanceof WhileStatement ||
				node instanceof ForStatement){
			Common.checkedNode.add(node);
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "LoopFirst_" + operationId);
			addList.add(addMap);
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "LoopMid_" + operationId);
			addList.add(addMap);
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "LoopFinal_" + operationId);
			addList.add(addMap);
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "LoopOut_" + operationId);
			addList.add(addMap);
		}
		if(node instanceof IfStatement){
			IfStatement ifNode = (IfStatement)node;
			addMap = new HashMap<ASTNode, String>();
			addMap.put(ifNode.getThenStatement(), "IfThen_" + operationId); 
			addList.add(addMap);
			Common.checkedNode.add(ifNode.getThenStatement());
			if(ifNode.getElseStatement() != null){
				addMap = new HashMap<ASTNode, String>();
				addMap.put(ifNode.getElseStatement(), "IfElse_" + operationId);
				addList.add(addMap);
				Common.checkedNode.add(ifNode.getElseStatement());
			}else{
				addMap = new HashMap<ASTNode, String>();
				addMap.put(ifNode.getThenStatement(), "IfNotIn_" + operationId);
				addList.add(addMap);
				
			}
		}
		if(node instanceof SwitchCase){
			addMap = new HashMap<ASTNode, String>();
			Common.checkedNode.add(node);
			addMap.put(node, "Case_" + operationId);
			addList.add(addMap);
		}
		if(node instanceof TryStatement){
			TryStatement tryNode = (TryStatement)node;
			addMap = new HashMap<ASTNode, String>();
			addMap.put(node, "Try_" + operationId);
			addList.add(addMap);
			Common.checkedNode.add(node);
			for(Object objectNode : tryNode.catchClauses()){
				CatchClause catchNode = (CatchClause)objectNode;
				Common.checkedNode.add(catchNode);
				addMap = new HashMap<ASTNode, String>();
				addMap.put(catchNode, "Catch_" + operationId);
				addList.add(addMap);
			}
		}
		
		ASTNode parent = node.getParent();
		while(parent != null){
			if(Common.checkedNode.contains(parent)){
				for(MultipleLinkedList<Map<ASTNode, String>> parentNode : ASTNodeUtilities.getChildrenByMapKey(Common.javaDeps, parent)){
					for(Map<ASTNode, String> childNode : addList){
						parentNode.addChild(childNode);
					}
				}
				return;
			}
			parent = parent.getParent();	
		}
	}
}

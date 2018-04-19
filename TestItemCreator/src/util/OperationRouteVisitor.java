package util;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import util.OperationNode.OperationType;

public class OperationRouteVisitor extends ASTVisitor{
	
	private static int operationId = 0;
	
	private static OperationNode operationNode;
	private static List<ASTNode> checkedNode;
	
	public static OperationNode getOperationNode(){
		return operationNode;
	}
	
	public static void reset(){
		operationNode = null;
		checkedNode = null;
	}

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
		if (operationNode == null){
			operationNode = new OperationNode(0, node.getRoot(), OperationType.ROOT);
		}
		if(checkedNode == null){
			checkedNode = new ArrayList<ASTNode>();
			checkedNode.add(node.getRoot());
		}
		
		List<OperationNode> addList = new ArrayList<OperationNode>();
		if(node instanceof MethodDeclaration){
			checkedNode.add(node);
			addList.add(new OperationNode(0, node, OperationType.METHOD));
		}
		if(node instanceof DoStatement ||
				node instanceof EnhancedForStatement ||
				node instanceof WhileStatement ||
				node instanceof ForStatement){
			checkedNode.add(node);
			addList.add(new OperationNode(operationId, node, OperationType.LOOP_FIRST));
			addList.add(new OperationNode(operationId, node, OperationType.LOOP_MID));
			addList.add(new OperationNode(operationId, node, OperationType.LOOP_FINAL));
			addList.add(new OperationNode(operationId, node, OperationType.LOOP_NOT));
		}
		if(node instanceof IfStatement){
			IfStatement ifNode = (IfStatement)node;
			Statement thenNode = ifNode.getThenStatement();
			Statement elseNode = ifNode.getElseStatement();
			addList.add(new OperationNode(operationId, thenNode, OperationType.IF_THEN));
			checkedNode.add(thenNode);
			if(elseNode != null){
				addList.add(new OperationNode(operationId, elseNode, OperationType.IF_ELSE));
				checkedNode.add(elseNode);
			}else{
				addList.add(new OperationNode(operationId, thenNode, OperationType.IF_NOT));
			}
		}
		if(node instanceof SwitchCase){
			checkedNode.add(node);
			addList.add(new OperationNode(operationId, node, OperationType.CASE));
		}
		if(node instanceof TryStatement){
			TryStatement tryNode = (TryStatement)node;
			Statement bodyNode = tryNode.getBody();
			addList.add(new OperationNode(operationId, bodyNode, OperationType.TRY));
			checkedNode.add(bodyNode);
			for(Object objectNode : tryNode.catchClauses()){
				CatchClause catchNode = (CatchClause)objectNode;
				checkedNode.add(catchNode);
				addList.add(new OperationNode(operationId, catchNode, OperationType.CATCH));
			}
		}
		
		ASTNode parent = node.getParent();
		while(parent != null){
			if(checkedNode.contains(parent)){
				for(OperationNode parentNode : ASTNodeUtilities.getChildrenByMapKey(operationNode, parent)){
					for(OperationNode childNode : addList){
						parentNode.addChild(childNode);
					}
				}
				return;
			}
			parent = parent.getParent();	
		}
	}
}

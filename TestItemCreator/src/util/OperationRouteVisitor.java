package util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class OperationRouteVisitor extends ASTVisitor{

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
	
	private void setDepsMap(ASTNode node){
		if (Common.javaDeps == null){
			Common.javaDeps = new MultipleLinkedList<ASTNode>(node.getRoot());
		}
		ASTNode parent = node.getParent();
		while(parent != null){
			if(parent instanceof MethodDeclaration ||
					parent instanceof DoStatement ||
					parent instanceof EnhancedForStatement ||
					parent instanceof ForStatement ||
					parent instanceof IfStatement ||
					parent instanceof SwitchCase ||
					parent instanceof TryStatement ||
					parent instanceof WhileStatement){
				MultipleLinkedList<ASTNode> parentNode = Common.javaDeps.searchChildren(parent);
				if(parentNode == null){
					Common.javaDeps.addChild(parent);
				}
				parentNode = Common.javaDeps.searchChildren(parent);
				parentNode.addChild(node);
				return;
			}
			parent = parent.getParent();	
		}
		Common.javaDeps.addChild(node);
	}
}

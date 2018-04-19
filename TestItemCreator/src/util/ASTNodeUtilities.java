package util;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class ASTNodeUtilities {
	
	public static List<OperationNode> getChildrenByMapKey(OperationNode node, ASTNode key){
		List<OperationNode> result = new ArrayList<OperationNode>();
		for(OperationNode childNode : node.getChildren()){
			result.addAll(getChildrenByMapKey(childNode, key));
		}
		if(node.getNode().equals(key)){
			result.add(node);
		}
		return result;
	}

	public static boolean isElseStatement(IfStatement ifParent, ASTNode node){
		ASTNode parent = node.getParent();
		ASTNode elseStatement = ifParent.getElseStatement();
		try{
			while(!parent.equals(elseStatement) && ifParent.equals(parent)){
				parent = parent.getParent();
			}
			if(parent.equals(ifParent)){
				return false;
			}else{
				return true;
			}
		}catch(NullPointerException e){
			return false;
		}
	}
	
	public static boolean isCatchStatement(TryStatement tryParent, ASTNode node){
		ASTNode parent = node.getParent();
		@SuppressWarnings("unchecked")
		List<ASTNode> catchStatements = tryParent.catchClauses();
		try{
			while(!catchStatements.contains(parent) && tryParent.equals(parent)){
				parent = parent.getParent();
			}
			if(parent.equals(tryParent)){
				return false;
			}else{
				return true;
			}
		}catch(NullPointerException e){
			return false;
		}
	}
	
}

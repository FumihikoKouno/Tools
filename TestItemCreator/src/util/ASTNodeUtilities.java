package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class ASTNodeUtilities {
	
	public static List<MultipleLinkedList<Map<ASTNode, String>>> getChildrenByMapKey(MultipleLinkedList<Map<ASTNode, String>> node, ASTNode key){
		List<MultipleLinkedList<Map<ASTNode, String>>> result = new ArrayList<MultipleLinkedList<Map<ASTNode, String>>>();
		for(MultipleLinkedList<Map<ASTNode, String>> childNode : node.getChildren()){
			result.addAll(getChildrenByMapKey(childNode, key));
		}
		if(node.getContent().containsKey(key)){
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

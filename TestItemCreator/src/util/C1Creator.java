package util;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;

public class C1Creator {
	private CompilationUnit unit;
	private String[] source;
	private Map<Integer, List<String>> testCaseMap = new HashMap<Integer, List<String>>();
	
	public C1Creator(CompilationUnit unit, String[] source){
		this.unit = unit;
		this.source = source;
	}
	
	public List<TestCase> createTestCase(OperationNode node){
		List<TestCase> result = new ArrayList<TestCase>();
		abcdefg
		List<List<List<Map<Integer, String>>>> childCases = new ArrayList<List<List<Map<Integer, String>>>>();
		List<List<Map<Integer, String>>> childCase = null;
		
		String nodeString = null;
		int nodeStringUnderPosition = -1;
		
		for(ASTNode key : nodeList.getContent().keySet()){
			nodeString = nodeList.getContent().get(key);
			nodeStringUnderPosition = nodeString.indexOf("_");
			if(nodeStringUnderPosition == -1){
				nodeStringUnderPosition = nodeString.length();
			}
		}

		if(nodeList.getChildren() == null || nodeList.getChildren().size() == 0){
			List<Map<Integer, String>> currentCase = new ArrayList<Map<Integer, String>>();
			Map<Integer, String> currentMap = new HashMap<Integer, String>();
			for(ASTNode key : nodeList.getContent().keySet()){
				currentMap.put(getLineNumber(nodeList), nodeList.getContent().get(key).substring(0, nodeStringUnderPosition));
			}
			currentCase.add(currentMap);
			result.add(currentCase);
			return result;
		}

		int operationID = -1;
		for(MultipleLinkedList<Map<ASTNode, String>> child : nodeList.getChildren()){
			Map<ASTNode, String> childNode = child.getContent();
			for(ASTNode childKey : childNode.keySet()){
				String childString = childNode.get(childKey);
				int childStringUnderPosition = childString.indexOf("_");
				if(childStringUnderPosition != -1){
					int childId = Integer.parseInt(childString.substring(childStringUnderPosition + 1));
					if(operationID != childId){
						if(childCase != null){
							childCases.add(childCase);
						}
						childCase = new ArrayList<List<Map<Integer, String>>>();
						operationID = childId;
					}
				}
				List<List<Map<Integer, String>>> grandChildCases = createTestCase(child);
				for(List<Map<Integer, String>> grandChildCase : grandChildCases){
					childCase.add(new ArrayList<Map<Integer, String>>(grandChildCase));
				}
			}
		}
		if(childCase != null && childCase.size() != 0){
			childCases.add(childCase);
		}
		
		for(List<List<Map<Integer, String>>> addedChildCases : childCases){
			if(result.size() == 0){
				result = addedChildCases;
			}else{
				List<List<Map<Integer, String>>> tmpResults = new ArrayList<List<Map<Integer, String>>>();
				List<Map<Integer, String>> tmpResult = new ArrayList<Map<Integer, String>>();
				for(List<Map<Integer, String>> resultCase : result){
					for(List<Map<Integer, String>> addedChildCase : addedChildCases){
						tmpResult = new ArrayList<Map<Integer, String>>(resultCase);
						for(Map<Integer, String> oneCase : addedChildCase){
							tmpResult.add(oneCase);
						}
						tmpResults.add(tmpResult);
					}
				}
				result = tmpResults;
			}
		}
		
		for(List<Map<Integer, String>> resultMap : result){
			Map<Integer, String> nodeCase = new HashMap<Integer, String>();
			nodeCase.put(getLineNumber(nodeList), nodeString.substring(0, nodeStringUnderPosition));
			resultMap.add(nodeCase);
		}
		
		System.out.println(result);
		return result;
	}
	
	public void createTestCaseForMethod(){
		for(OperationNode node : Common.javaDeps.getChildren()){
			//System.out.println(getLineNumber(method) + " : " + createTestCase(method));
			createTestCase(node);
		}
	}
	
	private int getLineNumber(MultipleLinkedList<Map<ASTNode, String>> node){
		for(ASTNode key : node.getContent().keySet()){
			return unit.getLineNumber(key.getStartPosition());
		}
		return -1;
	}
}

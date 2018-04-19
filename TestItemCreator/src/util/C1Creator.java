package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class C1Creator {
	private CompilationUnit unit;
	private String source;
	private Map<Integer, List<TestCase>> testCases;
	
	public C1Creator(File file){
		this.source = "";
		try(Scanner scanner = new Scanner(file)){
			while(scanner.hasNext()){
				this.source = this.source + scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.source.trim();
	}
	
	public C1Creator(String fileName){
		this(new File(fileName));
	}
	
	public Map<Integer, List<TestCase>> create(){
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		OperationRouteVisitor.reset();
		parser.setSource(source.toCharArray());
		unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		unit.accept(new OperationRouteVisitor());
		createTestCase(OperationRouteVisitor.getOperationNode());
		return this.getTestCases();
	}
	
	private List<TestCase> createTestCaseChild(OperationNode node){
		List<TestCase> result = new ArrayList<TestCase>();
		List<List<TestCase>> childCases = new ArrayList<List<TestCase>>();
		List<TestCase> childCase = new ArrayList<TestCase>();
		
		if(node.getChildren() == null || node.getChildren().size() == 0){
			result.add(new TestCase(getLineNumber(node), node.getType()));
			return result;
		}

		int operationID = -1;
		for(OperationNode child : node.getChildren()){
			if(operationID != child.getId()){
				if(childCase.size() > 0){
					childCases.add(childCase);
				}
				childCase = new ArrayList<TestCase>();
				operationID = child.getId();
			}
			List<TestCase> grandChildCases = createTestCaseChild(child);
			for(TestCase grandChildCase : grandChildCases){
				childCase.add(grandChildCase);
			}
		}
		if(childCase != null && childCase.size() != 0){
			childCases.add(childCase);
		}
		
		for(List<TestCase> addedChildCases : childCases){
			if(result.size() == 0){
				result = addedChildCases;
			}else{
				List<TestCase> newResult = new ArrayList<TestCase>();
				for(TestCase resultCase : result){
					for(TestCase addedChildCase : addedChildCases){
						TestCase testCase = new TestCase(resultCase);
						testCase.addAll(addedChildCase);
						newResult.add(testCase);
					}
				}
				result = newResult;
			}
		}
		
		for(TestCase resultCase : result){
			resultCase.add(getLineNumber(node), node.getType());
			resultCase.sort();
		}
		
		return result;
	}
	
	private void createTestCase(OperationNode root){
		testCases = new HashMap<Integer, List<TestCase>>();
		for(OperationNode node : root.getChildren()){
			createTestCaseChild(node);
			testCases.put(getLineNumber(node), createTestCaseChild(node));
		}
	}
	
	public Map<Integer, List<TestCase>> getTestCases(){
		return this.testCases;
	}
	
	private int getLineNumber(OperationNode node){
		return unit.getLineNumber(node.getNode().getStartPosition());
	}
	
	public String toString(){
		String result = "";
		Map<Integer, List<TestCase>> cases = getTestCases();
		if(cases == null){
			return "Test Cases have not created yet. Please execute create()";
		}
		Set<Integer> methodLineNumbers = cases.keySet();
		String[] sources = this.source.split("\n");
		for(int index = 0; index < sources.length; index++){
			result = result + (index + 1) + "\t\"" + sources[index] + "\"";
			if(methodLineNumbers.contains(index + 1)){
				for(TestCase testCase : cases.get(index + 1)){
					result = result + "\t\t" + testCase + "\n";
				}
			}else{
				result = result + "\n";
			}
		}
		return result;
	}
}

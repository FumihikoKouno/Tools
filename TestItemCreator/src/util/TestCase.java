package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.OperationNode.OperationType;

public class TestCase {
	
	private static class LineCondition{
		private int lineNumber;
		private OperationType type;
		public static final Map<OperationType, String> TEST_STRING = new HashMap<OperationType, String>();
		
		static{
			TEST_STRING.put(OperationType.IF_THEN, "if then");
			TEST_STRING.put(OperationType.IF_ELSE, "if else");
			TEST_STRING.put(OperationType.IF_NOT, "if not");
			TEST_STRING.put(OperationType.LOOP_FIRST, "loop first");
			TEST_STRING.put(OperationType.LOOP_MID, "loop mid");
			TEST_STRING.put(OperationType.LOOP_FINAL, "loop final");
			TEST_STRING.put(OperationType.LOOP_NOT, "loop not");
			TEST_STRING.put(OperationType.TRY, "try");
			TEST_STRING.put(OperationType.CATCH, "catch");
			TEST_STRING.put(OperationType.CASE, "case");
			TEST_STRING.put(OperationType.METHOD, "execute method");
			TEST_STRING.put(OperationType.ROOT, "root");
		}
		
		private LineCondition(int conditionLineNumber, OperationType conditionType){
			this.lineNumber = conditionLineNumber;
			this.type = conditionType;
		}
		
		private int getLineNumber(){
			return this.lineNumber;
		}

		private OperationType getType(){
			return this.type;
		}
		
		public String toString(){
			return getLineNumber() + " : " + TEST_STRING.get(getType());
		}
		
	}
	
	private List<LineCondition> conditions = new ArrayList<LineCondition>();
	
	public TestCase(int lineNumber, OperationType type){
		add(lineNumber, type);
	}
	
	public TestCase(TestCase testCase){
		addAll(testCase);
	}
	
	public boolean add(int lineNumber, OperationType type){
		return conditions.add(new LineCondition(lineNumber, type));
	}

	public boolean addAll(TestCase testCase){
		boolean result = true;
		for(LineCondition condition : testCase.conditions){
			result = result && add(condition.getLineNumber(), condition.getType());
		}
		return result;
	}
	
	public void sort(){
		List<LineCondition> sortedConditions = new ArrayList<LineCondition>();
		boolean insert;
		for(LineCondition condition : this.conditions){
			insert = false;
			for(int index = 0; index < sortedConditions.size(); index++){
				if(sortedConditions.get(index).getLineNumber() > condition.getLineNumber()){
					sortedConditions.add(index, condition);
					insert = true;
					break;
				}
			}
			if(!insert){
				sortedConditions.add(condition);
			}
		}
		this.conditions = sortedConditions;
	}
	
	public String toString(){
		String result = "";
		if(conditions.size() == 1){
			return conditions.get(0).toString();
		}
		for(LineCondition condition : conditions){
			if(condition.getType() != OperationType.METHOD){
				result = result + condition.toString() + "\t";
			}
		}
		return result;
	}
	
}

package parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EBNFContentParser extends EBNFVisitor {
	
	private List<EBNFNode> definitions;
	private String target;
	private int index = 0;
	private Stack<ParseResult> returnNodes = new Stack<ParseResult>();
	
	private class ParseResult{
		private boolean result;
		private List<Node> nodes;
		private int failedIndex;
		private String failCause;
		
		private ParseResult(boolean result, List<Node> nodes){
			this.result = result;
			this.nodes = nodes;
		}
		
		private ParseResult(boolean result, int failedIndex, String failCause){
			this.result = result;
			this.failedIndex = failedIndex;
			this.failCause = failCause;
		}
		
		private String getFailCause(){
			return failCause;
		}
		
		private int getFailedIndex(){
			return failedIndex;
		}
		
		private boolean getResult(){
			return result;
		}
		
		private List<Node> getNode(){
			return nodes;
		}
		
		public String toString(){
			if(result){
				return "Success: " + this.nodes;
			}else{
				return "Fail: " + this.failedIndex + " : " + this.failCause;
			}
		}
	}
	
	public EBNFContentParser(String target, List<EBNFNode> definitions){
		this.target = target;
		this.definitions = definitions;
	}
	
	public Node parse(String matchRuleName) throws ParseException{
		Node returnNode = new Node("Root");
		for(EBNFNode definition : definitions){
			if(((EBNFDefinition)definition).getName().equals(matchRuleName)){
				definition.accept(this);
				break;
			}
		}
		ParseResult result = returnNodes.pop();
		System.out.println(result);
		System.out.println(index);
		if(result.getResult()){
			for(Node successResultNode : result.getNode()){
				returnNode.addChildren(successResultNode);
			}
		}else{
			throw new ParseException(result.getFailCause(), result.getFailedIndex());
		}
		return returnNode;
	}
	
	@Override
	public void visit(EBNFDefinition node){
		node.getContents().accept(this);
		ParseResult result = returnNodes.pop();
		System.out.println(result);
		System.out.println(index);
		if(result.getResult()){
			Node resultNode = new Node("<" + node.getName() + ">");
			for(Node successResultNode : result.getNode()){
				resultNode.addChildren(successResultNode);
			}
			List<Node> resultNodeList = new ArrayList<Node>();
			resultNodeList.add(resultNode);
			returnNodes.push(new ParseResult(true, resultNodeList)); 
		}else{
			returnNodes.push(new ParseResult(false, result.getFailedIndex(), result.getFailCause())); 
		}
	}

	@Override
	public void visit(EBNFDefinitionCall node){
		for(EBNFNode definition : definitions){
			if(((EBNFDefinition)definition).getName().equals(node.getName())){
				definition.accept(this);
			}
		}
	}

	@Override
	public void visit(EBNFExcept node){
		int startIndex = index;
		EBNFNode right = node.getRight();
		right.accept(this);
		ParseResult result = returnNodes.pop();
		System.out.println(result);
		System.out.println(index);
		if(result.getResult()){
			index = startIndex;
			returnNodes.push(new ParseResult(false, index, "Not Expected " + right.toString()));
			return;
		}
		EBNFNode left = node.getLeft();
		left.accept(this);
		result = returnNodes.pop();
		System.out.println(result);
		System.out.println(index);
		if(result.getResult()){
			returnNodes.push(new ParseResult(true, result.getNode()));
		}else{
			returnNodes.push(new ParseResult(false, result.getFailedIndex(), "Not match " + left.toString()));
		}
	}

	@Override
	public void visit(EBNFFinish node){
		String content = node.getContent();
		List<Node> nodes = new ArrayList<Node>();
		for(int contentIndex = 0; contentIndex < content.length(); contentIndex++){
			try{
				if(target.charAt(index + contentIndex) != content.charAt(contentIndex)){
					returnNodes.push(new ParseResult(false, index + contentIndex, "Not match " + node.toString()));
					return;
				}
			}catch(IndexOutOfBoundsException e){
				returnNodes.push(new ParseResult(false, index + contentIndex, "Not match " + node.toString()));
				return;
			}
		}
		index = index + content.length();
		nodes.add(new Node(content));
		returnNodes.push(new ParseResult(true, nodes));
	}

	@Override
	public void visit(EBNFOption node){
		EBNFNode content = node.getContent();
		content.accept(this);
		ParseResult result = returnNodes.pop();
		System.out.println(result);
		System.out.println(index);
		if(result.getResult()){
			returnNodes.push(new ParseResult(true, result.getNode()));
		}else{
			returnNodes.push(new ParseResult(true, null));
		}
	}

	@Override
	public void visit(EBNFOr node){
		List<Node> resultNode = new ArrayList<Node>();
		int maxIndex = 0;
		int startIndex = index;
		int failedIndex = 0;
		for(List<EBNFNode> contents : node.getContents()){
			index = startIndex;
			boolean parseFail = false;
			List<Node> orNode = new ArrayList<Node>();
			for(EBNFNode content : contents){
				content.accept(this);
				ParseResult result = returnNodes.pop();
				System.out.println(result);
				System.out.println(index);
				if(result.getResult()){
					for(Node successResultNode : result.getNode()){
						orNode.add(successResultNode);
					}
				}else{
					parseFail = true;
					if(failedIndex < result.getFailedIndex()){
						failedIndex = result.getFailedIndex();
					}
					break;
				}
			}

			if(!parseFail){
				if(maxIndex < index){
					maxIndex = index;
					resultNode = orNode;
				}
			}
		}

		if(resultNode.size() > 0){
			index = maxIndex;
			returnNodes.push(new ParseResult(true, resultNode));
		}else{
			index = startIndex;
			returnNodes.push(new ParseResult(false, failedIndex, "Not match " + node.toString()));
		}
	}

	@Override
	public void visit(EBNFRepeat node){
		EBNFNode content = node.getContent();
		List<Node> resultNode = new ArrayList<Node>();
		boolean parseSuccess = true;
		ParseResult result;
		while(parseSuccess){
			content.accept(this);
			result = returnNodes.pop();
			System.out.println(result);
			System.out.println(index);
			parseSuccess = result.getResult();
			if(parseSuccess){
				for(Node successResult : result.getNode()){
					resultNode.add(successResult);
				}
			}
		}
		returnNodes.push(new ParseResult(true, resultNode));
	}

	@Override
	public void visit(EBNFTimes node){
		int startIndex = index;
		ParseResult result;
		List<Node> resultNode = new ArrayList<Node>();
		for(int loopCount = 0; loopCount < node.getCount(); loopCount++){
			node.getContent().accept(this);
			result = returnNodes.pop();
			System.out.println(result);
			System.out.println(index);
			if(result.getResult()){
				for(Node successResultNode : result.getNode()){
					resultNode.add(successResultNode);
				}
			}else{
				returnNodes.push(new ParseResult(false, index, "Not match " + node.toString()));
				index = startIndex;
				return;
			}
		}
		returnNodes.push(new ParseResult(true, resultNode));
	}

}

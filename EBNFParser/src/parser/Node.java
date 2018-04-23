package parser;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String tag;
	private List<Node> children = new ArrayList<Node>();
	private Node parent;
	
	public Node(String tag){
		this.tag = tag;
	}

	public Node(Node node){
		this.tag = node.tag;
		this.children = node.children;
		this.parent = node.parent;
	}
	
	public boolean addChildren(Node node){
		return this.children.add(node);
	}
	
	public void setParent(Node node){
		parent = node;
	}
	
	public void settag(String tag){
		this.tag = tag;
	}
	
	public List<Node> getChildren(){
		return this.children;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public void accept(NodeVisitor visitor){
		visitor.visit(this);
	}
	
	public String toString(){
		if(this.children.size() > 0){
			return this.tag + " -> " + this.children;
		}else{
			return this.tag;
		}
	}
}

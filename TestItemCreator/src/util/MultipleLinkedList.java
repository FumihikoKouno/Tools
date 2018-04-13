package util;

import java.util.ArrayList;
import java.util.List;

public class MultipleLinkedList<T> {
	private T content;
	private List<MultipleLinkedList<T>> children = new ArrayList<MultipleLinkedList<T>>();
	
	public MultipleLinkedList(T newContent){
		this.content = newContent;
	}
	
	public List<MultipleLinkedList<T>> getChildren(){
		return this.children;
	}
	
	public boolean addChild(T newContent){
		return children.add(new MultipleLinkedList<T>(newContent));
	}

	public boolean addChild(MultipleLinkedList<T> newContent){
		return children.add(newContent);
	}
	
	public T getContent(){
		return this.content;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof MultipleLinkedList){
			return this.content.equals(((MultipleLinkedList) o).getContent());
		}
		return false;
	}
	
	public MultipleLinkedList<T> searchChildren(T targetContent){
		if(this.content.equals(targetContent)){
			return this;
		}
		for(MultipleLinkedList<T> child : this.children){
			MultipleLinkedList<T> result = child.searchChildren(targetContent);
			if(result != null){
				return result;
			}
		}
		return null;
	}
}

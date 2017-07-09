package utils;

import java.util.Arrays;
import java.util.List;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class DiffContainer {
	private List<String> oldSources;
	private List<String> newSources;
	
	private List<Delta> diffs;
	
	public static void main(String[] args){
		List<String> original = Arrays.asList("zzz", "aaa", "bbb", "ccc",        "ddd");
		List<String> revised  = Arrays.asList(       "b2",  "ccc", "eee", "ddd", "fff", "zzz", "fff");
		DiffContainer diff = new DiffContainer(original.toArray(new String[0]), revised.toArray(new String[0]));
		System.out.println(diff);
		System.out.println(diff.getAddLineCount());
		System.out.println(diff.getChangeNewLineCount());
		System.out.println(diff.getChangeOldLineCount());
		System.out.println(diff.getDeleteLineCount());
	}

	public DiffContainer(String[] oldSources, String[] newSources){
		this.oldSources = Arrays.asList(oldSources);
		this.newSources = Arrays.asList(newSources);
		createDiff();
	}
	
	private void createDiff(){
		Patch patch = DiffUtils.diff(this.oldSources, this.newSources);
		this.diffs = patch.getDeltas();
	}
	
	public int getAddLineCount(){
		int count = 0;
		for(Delta diff : this.diffs){
			Chunk oldDiff = diff.getOriginal();
			Chunk newDiff = diff.getRevised();
			if(oldDiff.getSize() == 0 &&
				newDiff.getSize() > 0){
				count += newDiff.getLines().size();
			}
		}
		return count;
	}
	
	public int getChangeNewLineCount(){
		int count = 0;
		for(Delta diff : this.diffs){
			Chunk oldDiff = diff.getOriginal();
			Chunk newDiff = diff.getRevised();
			if(oldDiff.getSize() > 0 &&
				newDiff.getSize() > 0){
				count += newDiff.getLines().size();
			}
		}
		return count;
	}
	
	public int getChangeOldLineCount(){
		int count = 0;
		for(Delta diff : this.diffs){
			Chunk oldDiff = diff.getOriginal();
			Chunk newDiff = diff.getRevised();
			if(oldDiff.getSize() > 0 &&
				newDiff.getSize() > 0){
				count += oldDiff.getLines().size();
			}
		}
		return count;
	}
	
	public int getDeleteLineCount(){
		int count = 0;
		for(Delta diff : this.diffs){
			Chunk oldDiff = diff.getOriginal();
			Chunk newDiff = diff.getRevised();
			if(oldDiff.getSize() > 0 &&
				newDiff.getSize() == 0){
				count += oldDiff.getLines().size();
			}
		}
		return count;
	}
	
	public String toString(){
		String str = "";
		for(Delta diff : this.diffs){
			Chunk oldDiff = diff.getOriginal();
			Chunk newDiff = diff.getRevised();
			if(oldDiff.getSize() > 0 && newDiff.getSize() > 0){
				str = str + oldDiff.getPosition() + "c" + newDiff.getPosition() + "\n";
			}else if(oldDiff.getSize() > 0){
				str = str + oldDiff.getPosition() + "d" + newDiff.getPosition() + "\n";
			}else if(newDiff.getSize() > 0){
				str = str + oldDiff.getPosition() + "a" + newDiff.getPosition() + "\n";
			}else{
				str = str + oldDiff.getPosition() + "?" + newDiff.getPosition() + "\n";
			}
			str = str + "====================" + "\n";
			for(Object oldString : oldDiff.getLines()){
				str = str + "- " + oldString + "\n";
			}
			str = str + "--------------------" + "\n";
			for(Object newString : newDiff.getLines()){
				str = str + "+ " + newString + "\n";
			}
			str = str + "====================" + "\n\n";
		}
		return str;
	}
}

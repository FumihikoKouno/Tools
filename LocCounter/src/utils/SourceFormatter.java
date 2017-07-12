package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SourceFormatter {
	private File sourceFile;
	private String startMultiComment;
	private String endMultiComment;
	private String startSingleComment;
	private String source;
	
	/**
	 * Execute for this class.
	 * @param args
	 */
	public static void main(String[] args){
		SourceFormatter sf = new SourceFormatter(new File("src/utils/SourceFormatter.java"));
		sf.setMultiComment("/*", "*/");
		sf.setSingleComment("//");
		sf.removeMultiComments();
		sf.removeSingleComments();
		sf.strip(" ");
		sf.strip("\t");
		sf.removeAllBlankLines();
		System.out.println(sf);
		System.out.println(sf.getLOC());
	}
	
	/**
	 * Constractor
	 * @param sourceFile: source file
	 */
	public SourceFormatter(File sourceFile){
		this.sourceFile = sourceFile;
		this.load();
	}
	
	/**
	 * Set multiple comment characters.
	 * @param start: start characters of multiple comments.
	 * @param end: end characters of multiple comments.
	 */
	public void setMultiComment(String start, String end){
		this.startMultiComment = start;
		this.endMultiComment = end;
	}
	
	/**
	 * Set Single comment characters.
	 * @param comment: comment characters.
	 */
	public void setSingleComment(String comment){
		this.startSingleComment = comment;
	}
	
	/**
	 * Count lines of current source codes.
	 */
	public int getLOC(){
		int count = 0;
		int position = -1;
		while((position = this.source.indexOf("\n", position + 1)) > 0){
			count++;
		}
		return count + 1;
	}
	
	/**
	 * Load source codes from the input file.
	 */
	private void load(){
		this.source = "";
		try(Scanner scanner = new Scanner(this.sourceFile)) {
			while(scanner.hasNextLine()){
				this.source += scanner.nextLine() + "\n";
			}
			this.source = this.source.substring(0, this.source.length()-1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Strip source codes.
	 * @param str
	 */
	private void strip(String str){
		headStrip(str);
		tailStrip(str);
	}
	
	/**
	 * Strip tail of source codes.
	 * @param str
	 */
	private void tailStrip(String str){
		// Remove tail str
		while(this.source.contains(str + "\n")){
			this.source = this.source.replace(str + "\n", "\n");
		}
	}
	
	/**
	 * Strip head of source codes.
	 * @param str
	 */
	private void headStrip(String str){
		// Remove head str
		while(this.source.contains("\n" + str)){
			this.source = this.source.replace("\n" + str, "\n");
		}
	}
	
	/**
	 * Remove all spaces.
	 */
	private void removeAllSpaces(){
		this.source = this.source.replaceAll(" ", "");
	}
	
	/**
	 * Remove all tags.
	 */
	private void removeAllTags(){
		this.source = this.source.replaceAll("\t", "");
	}
	
	/**
	 * Remove blank lines.
	 */
	private void removeAllBlankLines(){
		while(this.source.contains("\n\n")){
			this.source = this.source.replace("\n\n", "\n");
		}
		while(this.source.endsWith("\n")){
			this.source = this.source.substring(0, this.source.length() - 1);
		}
	}

	/**
	 * Remove single comments.
	 */
	private void removeSingleComments(){
		String result = "";
		char stringTag = 0;
		boolean isEscape = false;
		for(int index = 0; index < this.source.length(); index++){
			char current = this.source.charAt(index);
			if(isEscape){
				isEscape = false;
			}else{
				if(current == '\\'){
					isEscape = true;
				}
				if(current == '"' || current == '\''){
					if(stringTag == current){
						stringTag = 0;
					}else if(stringTag == 0){
						stringTag = current;
					}
				}
			}
			if(stringTag == 0){
				try{
					if(this.startSingleComment.equals(
							this.source.substring(
									index,
									index + this.startSingleComment.length()
							)
						)
					){
						if(this.source.indexOf("\n", index) > 0){
							index = this.source.indexOf("\n", index) - 1;
							continue;
						}else{
							index = this.source.length();
							break;
						}
					}
				}catch(StringIndexOutOfBoundsException e){
					result = result + this.source.substring(index);
					index = this.source.length();
					break;
				}
			}
			result = result + current;
		}
		this.source = result;
	}

	/**
	 * Remove multiple comments.
	 */
	private void removeMultiComments(){
		String result = "";
		char stringTag = 0;
		boolean isEscape = false;
		boolean inComment = false;
		for(int index = 0; index < this.source.length(); index++){
			char current = this.source.charAt(index);
			if(isEscape){
				isEscape = false;
			}else{
				if(!inComment){
					if(current == '\\'){
						isEscape = true;
					}
					if(current == '"' || current == '\''){
						if(stringTag == current){
							stringTag = 0;
						}else if(stringTag == 0){
							stringTag = current;
						}
					}
				}
			}
			if(stringTag == 0){
				try{
					if(this.startMultiComment.equals(
								this.source.substring(
										index, 
										index + this.startMultiComment.length()
								)
						) && !inComment
					){
						inComment = true;
						index = index + this.startMultiComment.length() - 1;
						continue;
					}else if(this.endMultiComment.equals(
								this.source.substring(
										index, 
										index + this.endMultiComment.length()
								)
						) && inComment
					){
						inComment = false;
						index = index + this.endMultiComment.length() - 1;
						continue;
					}
				}catch(StringIndexOutOfBoundsException e){
					result = result + this.source.substring(index);
					index = this.source.length();
					break;
				}
			}
			if((!inComment) || (current == '\n')){
				result = result + current;
			}
		}
		this.source = result;
	}

	/**
	 * Formatted string.
	 */
	public String toString(){
		return this.source;
	}
}
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SourceFormatter {
	private File sourceFile;
	private String startComment;
	private String endComment;
	private String comment;
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
		this.startComment = start;
		this.endComment = end;
	}
	
	/**
	 * Set Single comment characters.
	 * @param comment: comment characters.
	 */
	public void setSingleComment(String comment){
		this.comment = comment;
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
		// Remove head str
		while(this.source.contains("\n" + str)){
			this.source = this.source.replace("\n" + str, "\n");
		}
		// Remove tail str
		while(this.source.contains(str + "\n")){
			this.source = this.source.replace(str + "\n", "\n");
		}
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
		while(true){ 
			int startIndex = this.source.indexOf(this.comment);
			int endIndex = this.source.indexOf("\n", startIndex + this.comment.length());
			if (startIndex < 0){
				break;
			}
			if (endIndex < 0){
				this.source = this.source.substring(0, startIndex);
			}else{
				this.source = this.source.substring(0, startIndex) +
								this.source.substring(endIndex);
			}
		}
	}

	/**
	 * Remove multiple comments.
	 */
	private void removeMultiComments(){
		while(true){ 
			int startIndex = this.source.indexOf(this.startComment);
			int endIndex = this.source.indexOf(this.endComment, 
												startIndex + this.startComment.length());
			if (startIndex < 0 || endIndex < 0){
				break;
			}
			if(this.source.substring(startIndex, endIndex).contains("\n")){
				this.source = this.source.substring(0, startIndex) +
								"\n" +
								this.source.substring(endIndex + this.endComment.length());
			}else{
				this.source = this.source.substring(0, startIndex) +
								this.source.substring(endIndex + this.endComment.length());
			}
		}
	}

	/**
	 * Formatted string.
	 */
	public String toString(){
		return this.source;
	}
}
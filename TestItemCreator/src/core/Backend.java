package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import util.Common;
import util.MultipleLinkedList;
import util.OperationRouteVisitor;

public class Backend {

	public static void print(String msg){
		System.out.println(msg);
	}
	
	public static void main(String[] args){
		if(args.length != 1){
			print("This program need Java source File path.");
			return;
		}
		
		String sourceCode = "";
		try(Scanner scanner = new Scanner(new File(args[0]))){
			while(scanner.hasNextLine()){
				sourceCode = sourceCode + scanner.nextLine() + "\n";
			}
		}catch(FileNotFoundException e){
			print("File not found.");
			return;
		}
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(sourceCode.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		unit.accept(new OperationRouteVisitor());
		
		allPrint(unit, Common.javaDeps, 0);
	}
	
	public static void allPrint(CompilationUnit unit, MultipleLinkedList<ASTNode> node, int indent){
		String indentString = "";
		for(int index = 0; index < indent; index++){
			indentString = indentString + " ";
		}
		System.out.print(indentString);
		System.out.print(unit.getLineNumber(node.getContent().getStartPosition()));
		System.out.println(" : " + node.getContent().getNodeType());
		for(MultipleLinkedList<ASTNode> child : node.getChildren()){
			allPrint(unit, child, indent+2);
		}
	}
	
}

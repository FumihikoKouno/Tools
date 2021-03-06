package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;

import util.C1Creator;
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
			if(true){
				System.out.println();
				if(false){
					System.out.println();
				}else{
					System.out.println();
				}
			}else{
				if(false){
					System.out.println();
				}
			}
			print("File not found.");
			return;
		}
		
		C1Creator creator = new C1Creator(args[0]);
		creator.create();
		System.out.println(creator);
		//allPrint(unit, Common.javaDeps, 0);
	}
	
	public static void allPrint(CompilationUnit unit, MultipleLinkedList<Map<ASTNode, String>> node, int indent){
		String indentString = "";
		for(int index = 0; index < indent; index++){
			indentString = indentString + " ";
		}
		System.out.print(indentString);
		for(ASTNode astNode : node.getContent().keySet()){
			System.out.print(unit.getLineNumber(astNode.getStartPosition()));
			System.out.println(" : " + node.getContent().get(astNode));
			for(MultipleLinkedList<Map<ASTNode, String>> child : node.getChildren()){
				allPrint(unit, child, indent+2);
			}
		}
	}
	
}

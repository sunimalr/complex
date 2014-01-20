package inter;

import java.util.ArrayList;

import lex.Lexer;

public class Node extends AbstractNode{

	int lexline = 0;
	public String op;
	public AbstractNode left,right;
	
	public Node() {
		lexline = Lexer.line;
	}
	
	public Node(String op, AbstractNode left, AbstractNode right){
		super.value=AbstractNode.statVal;
		this.op=op;
		this.left=left;
		this.right=right;
	}

	void error(String s) {
		throw new Error("near line " + lexline + ": " + s);
	}

	static int labels = 0;

	public int newlabel() {
		return ++labels;
	}

	public void emitlabel(int i) {
		System.out.print("L" + i + ":");
	}

	public void emit(String s) {
		System.out.println("\t" + s);
	}
}

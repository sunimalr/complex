package main;

import java.io.IOException;

import parser.Parser;
import stack.StackMachine;
import lex.Lexer;

public class Main {

	public static void main(String[] args) throws IOException {
		Lexer lexer=new Lexer();
        StackMachine stackMachine=new StackMachine();
        Parser parser=new Parser(lexer,stackMachine);
        parser.P();
        System.out.println("Finished Successfully");

	}
}

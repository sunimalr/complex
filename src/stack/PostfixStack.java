package stack;

import java.util.Stack;

import lex.Token;

public class PostfixStack {

	public Stack<Token> stack;

	public PostfixStack() {
		stack = new Stack<Token>();
	}

	public void push(Token token) {
		stack.push(token);
	}

	public Token pop() {
		return stack.pop();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

}

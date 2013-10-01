package lex;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
	public int line = 1;
	public char next=' ';
	private Hashtable<String, Word> wordTable = new Hashtable<String, Word>();
	
	public Lexer(){
		
	}
	
	private void reserve(Word word){
		wordTable.put(word.lexeme, word);
	}
	
	public Token scan() throws IOException{
		return new Token(0);
	}
}

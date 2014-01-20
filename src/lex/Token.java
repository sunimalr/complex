package lex;

public class Token {
	public final int tag;
	public String type;
	public String lexeme;

	public Token(int tag) {
		this.tag = tag;
	}
	
	public String toString(){
		return "" + (char)tag;
	}
}

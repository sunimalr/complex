package lex;

public class Word extends Token{
	public final String lexeme;

	public Word(int tok, String str) {
		super(tok);
		this.lexeme = new String(str);
	}
}

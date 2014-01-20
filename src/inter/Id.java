package inter;

import lex.Token;

public class Id extends Token {
	public final String lexeme;
	public Object value;

	public Id(int t, String s) {
		super(t);
		lexeme = new String(s);
		super.lexeme=lexeme;
		value = 0;
	}
}

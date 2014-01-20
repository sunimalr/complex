package lex;

public class Num extends Token{
	public final int val;
	
	public Num(int v) {
		super(Tag.NUM);
		this.val = v;
		lexeme=Integer.toString(v);
        type="int";
	}

	public String toString(){
		return "" + val;
	}
}

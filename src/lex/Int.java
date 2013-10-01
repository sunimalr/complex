package lex;

public class Int extends Token{
	public final int val;

	public Int(int val) {
		super(Tag.INT);
		this.val = val;
	}
}

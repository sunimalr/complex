package lex;

public class Flo extends Token{
	public final float val;
		
	public Flo(float val) {
		super(Tag.FLOAT);
		this.val = val;
		}
}

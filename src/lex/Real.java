package lex;

public class Real extends Token{

	public final float value;
	public Real(float v) {
        super(Tag.FLOAT) ;
        value = v;
        lexeme=Float.toString(value);
        type="float";
    }

    public String tostring(){
        return "" + value;
    }
}

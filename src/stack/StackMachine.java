package stack;

import inter.Id;
import lex.Num;
import lex.Real;
import lex.Token;

public class StackMachine {
	public PostfixStack stack;
	public Object value;

	public StackMachine() {
		stack = new PostfixStack();
	}

	public void evaluate(String op) {
		Token top = stack.pop();
		Token secondTop = null;
		if (!stack.isEmpty()) {
			secondTop = stack.pop();
		}
		Object val1 = null;
		Object val2 = null;
		Object result = null;

		if (top != null && secondTop != null) {
			if (top instanceof Id) {
				Id id = (Id) top;
				val1 = id.value;
			} else if (top instanceof Num) {
				Num num = (Num) top;
				val1 = num.val;
			} else if (top instanceof Real) {
				Real real = (Real) top;
				val1 = real.value;
			}

			if (secondTop instanceof Id) {
				Id id = (Id) secondTop;
				val2 = id.value;
			} else if (secondTop instanceof Num) {
				Num num = (Num) secondTop;
				val2 = num.val;
			} else if (secondTop instanceof Real) {
				Real real = (Real) secondTop;
				val2 = real.value;
			}

			if (op != null) {
				if (op.equals("*")) {
					if (val1 instanceof Integer && val2 instanceof Integer) {
						result = (Integer) val1 * (Integer) val2;
					} else if (val1 instanceof Integer && val2 instanceof Float) {
						result = (Integer) val1 * (Float) val2;
					} else if (val1 instanceof Float && val2 instanceof Integer) {
						result = (Float) val1 * (Integer) val2;
					} else if (val1 instanceof Float && val2 instanceof Float) {
						result = (Float) val1 * (Float) val2;
					}
				}else if (op.equals("+")) {
					if (val1 instanceof Integer && val2 instanceof Integer) {
						result = (Integer) val1 + (Integer) val2;
					} else if (val1 instanceof Integer && val2 instanceof Float) {
						result = (Integer) val1 + (Float) val2;
					} else if (val1 instanceof Float && val2 instanceof Integer) {
						result = (Float) val1 + (Integer) val2;
					} else if (val1 instanceof Float && val2 instanceof Float) {
						result = (Float) val1 + (Float) val2;
					}
				}
			}
			
			if(result instanceof Integer){
				Num num=new Num((Integer)result);
				stack.push(num);
			}else{
				Real real=new Real((Float)result);
				value=result;
				stack.push(real);
			}
		}else{
			if(top instanceof Id){
				Id id=(Id)top;
				value=id.value;
			}else if(top instanceof Num){
				Num num=(Num) top;
				value=num.val;
			}else if(top instanceof Real){
				Real real=(Real) top;
				value=real.value;
			}
		}
	}

}

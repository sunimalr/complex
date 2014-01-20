package parser;

import inter.AbstractNode;
import inter.Id;

import java.io.IOException;
import java.util.ArrayList;

import stack.StackMachine;
import symbols.Env;
import symbols.Type;
import lex.Lexer;
import lex.Num;
import lex.Real;
import lex.Tag;
import lex.Token;

public class Parser {

	// public static String lookahead;
	private Lexer lexer;
	private Token look;
	Env top = null;
	int usedStorage = 0; // Storage used for declarations
	private int skipFlag = -1;
	private Id skipId;
	private StackMachine stackMachine;
	private Id currentSymbol;

	public StringBuffer postFixBuffer = new StringBuffer();
	private ThreeAddressCodeGenerator threeAddressCodeGenerator;

	public Parser(Lexer lex, StackMachine sm) throws IOException {
		this.lexer = lex;
		this.stackMachine = sm;
		this.threeAddressCodeGenerator = new ThreeAddressCodeGenerator();
		move();
	}

	private void move() throws IOException {
		look = lexer.scan();
	}

	private void error(String str) {
		throw new Error("line " + lexer.line + ":" + str);
	}

	private void match(int t) throws IOException {
		if (look.tag == t && skipFlag == -1) {
			move();
		} else if (skipFlag == 1) {
			skipFlag = -1;
		} else {
			error("Syntax Error");
		}
	}

	public void P() throws IOException {
		top = new Env(top);
		D();
		L();
	}

	public void D() throws IOException {
		Type b = B();
		N(b);
		lexer.setCurrentType(null);
		match(';');
		D1();
	}

	public Type B() throws IOException {
		Type b = (Type) look;
		match(Tag.BASIC);
		return b;
	}

	public void D1() throws IOException {
		if (look.tag == Tag.BASIC) {
			D();
		} else {
		}
	}

	public void N(Type type) throws IOException {
		Token t = look;
		Id id = (Id) t;
		top.put(t, id);
		usedStorage = usedStorage + type.width;
		match(Tag.ID);
		N1(type);
	}

	public void N1(Type type) throws IOException {
		if (look.tag == ',') {
			match(',');
			Token t = look;
			Id id = (Id) t;
			top.put(t, id);
			usedStorage = usedStorage + type.width;
			match(Tag.ID);
			N1(type);
		} else {
		}
	}

	public void L() throws IOException {
		S();
		stackMachine.evaluate(null);
		if (currentSymbol != null) {
			currentSymbol.value = stackMachine.value;
			postFixBuffer.append("\n");
			if (currentSymbol.type == "int"
					&& stackMachine.value instanceof Float) {
				postFixBuffer
						.append("Error: Type mismatch (Narrowing convention)"
								+ currentSymbol.type + " and"
								+ Type.Float.toString() + "\n");
			}
			if (currentSymbol.type == "float"
					&& stackMachine.value instanceof Integer) {
				postFixBuffer
						.append("Warning: Type mismatch (Widening convention)"
								+ currentSymbol.type + " and"
								+ Type.Float.toString() + "\n");
				// THIS IS OK
			}

			postFixBuffer.append(currentSymbol.lexeme + "="
					+ currentSymbol.value + "\n");
		} else {
			postFixBuffer.append("\n");
			postFixBuffer.append(stackMachine.value);
		}
		currentSymbol = null;
		AbstractNode.used = new ArrayList<AbstractNode>();// New set of nodes
															// for new stmt
		AbstractNode.statVal = 0;
        System.out.println("Postfix notation and value of the statement");
		System.out.println(postFixBuffer);
		System.out.println();
		postFixBuffer=new StringBuffer();
		match(';');
		L1();
	}

	public void L1() throws IOException {
		if (look.tag == Tag.ID || look.tag == Tag.NUM || look.tag == Tag.FLOAT
				|| look.tag == '(') {
			L();
		} else {

		}
	}

	 public void S() throws IOException {
	        AbstractNode node;
	        AbstractNode exprn;
	        Id assId;
	        if (look.tag == '(' || look.tag == Tag.NUM) {
	            node=E();
	        }
	        else if(look.tag==Tag.ID){
	            currentSymbol= (Id) look;
	            match(Tag.ID);
	            if(look.tag=='=') {
	                match('=');
	                exprn=E();
	                node=threeAddressCodeGenerator.getNode(threeAddressCodeGenerator.getLeafNode(currentSymbol),exprn , "="); //3AC for assignment
	            } else {
	                skipId=currentSymbol;
	                currentSymbol=null;
	                skipFlag=1;
	                node=E();
	            }
	        } else {
	            throw new Error("Syntax Error");
	        }
	    }
	
	public AbstractNode E() throws IOException {
        AbstractNode node = null;
        AbstractNode termnode = null;
        termnode=T();
        node=E1(termnode);
        return node;
    }


    public AbstractNode E1(AbstractNode termnodeinh) throws IOException {
        AbstractNode node;  // node which represents the operation so far
        AbstractNode snode;  // Synthesized attribute
        AbstractNode pretn=termnodeinh; //Inherited Previous Nonterminal
        AbstractNode curtn;
        if(look.tag=='+') {
            match('+');
            curtn=T();
            postFixBuffer.append("+");
            stackMachine.evaluate("+");
            node=threeAddressCodeGenerator.getNode(pretn, curtn, "+");
            snode=E1(node);
        }
        else {
            snode=termnodeinh;
        }
        return snode;
    }

    public AbstractNode T() throws IOException {
        AbstractNode node = null;
        AbstractNode factnode = null;
        factnode=F();
        node=T1(factnode);
        return node;
    }

    public AbstractNode T1(AbstractNode factnodeinh) throws IOException {
        AbstractNode node; 
        AbstractNode snode;  // Synthesized attribute
        AbstractNode prefn=factnodeinh; //Inherited Previous Nonterminal
        AbstractNode curfn; //Current factor node
        if(look.tag=='*') {
            match('*');
            curfn=F();
            postFixBuffer.append("*");
            stackMachine.evaluate("*");
            node=threeAddressCodeGenerator.getNode(prefn, curfn, "*");
            snode=T1(node);
        } else {
            snode=factnodeinh;
        }
        return snode;
    }

    public AbstractNode F() throws IOException {
        AbstractNode abstractNode=null;
        if(look.tag=='('){
            match('(');
            abstractNode=E();
            match(')');
        } else if (look.tag==Tag.ID) {
            Id word=(Id) look;
            String workLex=word.lexeme;
            stackMachine.stack.push(word);
            match(Tag.ID);
            abstractNode=threeAddressCodeGenerator.getLeafNode(word);
            postFixBuffer.append(workLex);

        } else if( look.tag==Tag.NUM) {
            Num num=(Num) look;
            String IntNum=num.toString();
            match(Tag.NUM);
            stackMachine.stack.push(num);
            abstractNode=threeAddressCodeGenerator.getLeafNode(num);
            postFixBuffer.append(IntNum);
        } else if(look.tag==Tag.FLOAT) {
            Real real=(Real) look;
            String floatNum =real.tostring();
            match(Tag.FLOAT);
            stackMachine.stack.push(real);
            abstractNode=threeAddressCodeGenerator.getLeafNode(real);
            postFixBuffer.append(floatNum);
        } else if(skipId!=null && skipFlag==1) {
            Id word=(Id) skipId;
            String workLex=word.lexeme;
            stackMachine.stack.push(word);
            match(Tag.ID);
            abstractNode=threeAddressCodeGenerator.getLeafNode(word);
            postFixBuffer.append(workLex);
            skipId=null;
        } else {
            throw new Error("Syntax Error");
        }
        return abstractNode;
    }
	
	

}

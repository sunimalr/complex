package parser;

import java.io.IOException;

import lex.Num;
import lex.Real;
import lex.Tag;
import lex.Token;

import inter.AbstractNode;
import inter.Id;
import inter.Node;

public class ThreeAddressCodeGenerator {

	public StringBuffer threeAddressCodes = new StringBuffer();

	Node getNode(AbstractNode l, AbstractNode r, String op) throws IOException {

		Node n = null;

		for (int i = 0; i < AbstractNode.used.size(); i++) {

			if (AbstractNode.used.get(i) instanceof Node) {
				n = (Node) AbstractNode.used.get(i);
				if (n.op.equals(op) && l == n.left && r == n.right) {
					return n;
				}
			}
		}
		if (!op.equals("=")) { // widen is done for both operands if not
								// assignment
			if (widen(l, typeMax(l, r))) {
				AbstractNode.statVal++;
				l = new Node("(float)", l, null); // make a unary op for casting
				l.type = "float";
				generateCode(l); // generate code for temporary
			}
			if (widen(r, typeMax(l, r))) {
				AbstractNode.statVal++;
				r = new Node("(float)", r, null); // make a unary op for casting
				r.type = "float";
				generateCode(r); // generate code for temporary
			}
		} else { // if assignment only right side is updated
			if (widen(r, typeMax(l, r))) {
				AbstractNode.statVal++;
				r = new Node("(float)", r, null); // make a unary op for casting
				r.type = "float";
				generateCode(r); // generate code for temporary
			}

		}

		AbstractNode.statVal++; // give a new number to the temporary
		n = new Node(op, l, r);

		if (!n.op.equals("=")) {
			n.type = typeMax(l, r); // in assignment we can't change type
		}
		generateCode(n);

		AbstractNode.used.add(n);

		return n;
	}

	LeafNode getLeafNode(Token token) throws IOException {

		LeafNode l;
		for (int i = 0; i < AbstractNode.used.size(); i++) {

			if (AbstractNode.used.get(i) instanceof LeafNode) {
				l = (LeafNode) AbstractNode.used.get(i);
				if (token == (l.tok))
					return l;

			}
		}
		l = new LeafNode(token);
		l.type = token.type;
		AbstractNode.used.add(l);
		return l;
	}

	private void generateCode(AbstractNode inNode) throws IOException {

		LeafNode l;
		Node n;
		Id id = null, assId = null;
		Num num;
		String leftstr, rightstr = null;
		n = (Node) inNode;
		if (n.right != null) {

			if (!n.op.equals("=")) {
				leftstr = "t" + n.left.value;
				rightstr = "t" + n.right.value;
				if (n.left instanceof LeafNode) { // if the child is LeafNode
													// print its lexeme
					l = (LeafNode) n.left;
					if (l.tok.tag == Tag.ID) {
						id = (Id) l.tok;
						leftstr = id.lexeme;
					} else {
						if (l.tok instanceof Num) {
							num = (Num) l.tok;
							leftstr = num.lexeme;
						}

						if (l.tok instanceof Real) {
							Real real = (Real) l.tok;
							leftstr = real.lexeme;
						}
					}
				}
				if (n.right instanceof LeafNode) {
					l = (LeafNode) n.right;
					if (l.tok.tag == Tag.ID) {
						id = (Id) l.tok;
						rightstr = id.lexeme;
					} else {
						if (l.tok instanceof Num) {
							num = (Num) l.tok;
							rightstr = num.lexeme;
						}

						if (l.tok instanceof Real) {
							Real real = (Real) l.tok;
							rightstr = real.lexeme;
						}
					}
				}

				System.out.println("t" + n.value + "= " + leftstr + n.op
						+ rightstr);

			} else { // an assignment

				l = (LeafNode) n.left;
				assId = (Id) l.tok; // left of assignment is definitely id

				if (assId.type.equals("int") && n.right.type.equals("float")) {
					throw new Error("Narrowing conversion!");
				}
				if (!(n.right instanceof LeafNode)) // if the right side is node
				{
					rightstr = "t" + n.right.value;
				} else {

					l = (LeafNode) n.right;
					if (l.tok.tag == Tag.ID) {
						id = (Id) l.tok;
						leftstr = id.lexeme;
					} else {
						if (l.tok instanceof Num) {
							num = (Num) l.tok;
							rightstr = num.lexeme;
						}
						if (l.tok instanceof Real) {
							Real real = (Real) l.tok;
							rightstr = real.lexeme;
						}
					}
				}
				System.out.println(assId.lexeme + "= " + rightstr);

			}
			System.out.println();
		} else {
			if (n.left instanceof LeafNode) {
				l = (LeafNode) n.left;
				System.out.println("t" + n.value + "= " + n.op + l.tok.lexeme);
			} else {
				System.out.println("t" + n.value + "= " + n.op + "t"
						+ n.left.value);
			}
			System.out.println();
		}
	}

	private boolean widen(AbstractNode n, String maxtype) throws IOException {// returns
																				// true
																				// if
																				// a
																				// widening
																				// conversion
																				// should
																				// be
																				// done

		if (maxtype.equals(n.type))
			return false;
		else {
			return true;
		}
	}

	private String typeMax(AbstractNode l, AbstractNode r) {

		String max = "int";

		if (r.type.equals("float") || l.type.equals("float")) {
			max = "float";
		}
		return max;
	}
}

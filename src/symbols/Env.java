package symbols;

import inter.Id;

import java.util.Hashtable;

import lex.Token;

public class Env {
	private Hashtable table;
	protected Env prevEnv;

	public Env(Env prevEnv) {
		super();
		this.table = new Hashtable();
		this.prevEnv = prevEnv;
	}

	public void put(Token w, Id i) {
		table.put(w, i);
	}

	public Id get(Token w) {
		for (Env e = this; e != null; e = e.prevEnv) {
			Id found = (Id) (e.table.get(w));
			if (found != null)
				return found;
		}
		return null;
	}
}

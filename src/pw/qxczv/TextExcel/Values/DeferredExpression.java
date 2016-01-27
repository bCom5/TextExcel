package pw.qxczv.TextExcel.Values;

import pw.qxczv.TextExcel.Spreadsheet;
import pw.qxczv.TextExcel.AST.Expression;

public class DeferredExpression extends Value {
	public Expression xpr;
	
	public DeferredExpression(Expression x) {
		xpr = x;
	}
	
	@Override
	public Value resolve(Spreadsheet s) {
		return xpr.evaluate(s);
	}
	
	@Override
	public int compareTo(Value o) {
		// Comparing a deferred expression always returns less because there is no Spreadsheet to evaluate relative to
		return -1;
	}

}

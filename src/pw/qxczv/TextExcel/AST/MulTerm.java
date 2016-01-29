package pw.qxczv.TextExcel.AST;

import pw.qxczv.TextExcel.Spreadsheet;
import pw.qxczv.TextExcel.Values.Value;
import pw.qxczv.TextExcel.Values.ErrorValue;
import pw.qxczv.TextExcel.Values.Number;

public class MulTerm extends Expression {

	public Expression left, right;
	
	public MulTerm(Expression l, Expression r) {
		left = l; right = r;
	}
	
	@Override
	public Value evaluate(Spreadsheet s) {
		Number lv = (Number)(left.evaluate(s).resolve(s));
		Number rv = (Number)(right.evaluate(s).resolve(s));
		if(lv == null || rv == null) {
			return new ErrorValue("Cannot multiply non-numeric values!");
		}
		return new Number(lv.v * rv.v);
	}

}
package pw.qxczv.TextExcel.AST;

import java.util.ArrayList;
import java.util.List;

import pw.qxczv.TextExcel.Spreadsheet;
import pw.qxczv.TextExcel.Values.Function;
import pw.qxczv.TextExcel.Values.Value;

public class FunctionInvocationExpression extends Expression {
	public Expression func;
	public List<Expression> args;
	
	public FunctionInvocationExpression(Expression f, List<Expression> a) {
		func = f;
		args = a;
	}
	
	@Override
	public Value evaluate(Spreadsheet s) {
		Function F = (Function)(func.evaluate(s).resolve(s));
		ArrayList<Value> vargs = new ArrayList<>();
		for(Expression x : args) vargs.add(x.evaluate(s).resolve(s));
		return F.apply(s, vargs);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(func + " ");
		for(Expression a : args) sb.append(a + " ");
		return sb.toString();
	}

}

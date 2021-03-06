package pw.qxczv.TextExcel;
import java.util.ArrayList;
import java.util.List;

import pw.qxczv.TextExcel.AST.Expression;
import pw.qxczv.TextExcel.AST.Identifier;
import pw.qxczv.TextExcel.AST.SubExpression;
import pw.qxczv.TextExcel.Values.CellReference;
import pw.qxczv.TextExcel.Values.ErrorValue;
import pw.qxczv.TextExcel.Values.Function;
import pw.qxczv.TextExcel.Values.LValue;
import pw.qxczv.TextExcel.Values.TrueValue;
import pw.qxczv.TextExcel.Values.RegionReference;
import pw.qxczv.TextExcel.Values.StringValue;
import pw.qxczv.TextExcel.Values.Value;
import pw.qxczv.TextExcel.Values.Number;

public class BuiltinFunctions {

	static class PrintFunc extends Function {
		private static final long serialVersionUID = 1L;
		public PrintFunc() {
			super(new ArrayList<String>(),null);
		}
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			try {
				s.print();
			} catch(StackOverflowError e) {
				System.out.println("Error: Stack Overflow; Self-referencing formulas?");
			}
			return null;
		}
	}
	static class ClearFunc extends Function {
		private static final long serialVersionUID = 1L;
		public ClearFunc() {
			super(new ArrayList<String>(),null);
		}
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			if(args.size() > 0) {
				LValue v = (LValue)(args.get(0).evaluate(s));
				v.assign(s, null);
			}
			else s.clear();
			return null;
		}
	}
	static class EscFunc extends Function {
		private static final long serialVersionUID = 1L;
		public EscFunc() {
			super(new ArrayList<String>(), null);
			argnames.add("expression");
		}
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			return args.get(0).evaluate(s).resolve(s);
		}
	}
	static class CompFunc extends Function {
		private static final long serialVersionUID = 1L;
		public static enum ComparisonOp {
			less, lesseq, eq, grtr, grtreq, noteq
		}
		public ComparisonOp op;
		public CompFunc(ComparisonOp OP) {
			super(new ArrayList<String>(), null);
			argnames.add("op1");
			argnames.add("op2");
			op = OP;
		}
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			Value a = args.get(0).evaluate(s).resolve(s);
			Value b = args.get(1).evaluate(s).resolve(s);
			switch(op) {
			case less:			return a.compareTo(b) < 0 ? TrueValue.get() : null;
			case lesseq:		return a.compareTo(b) <= 0 ? TrueValue.get() : null;
			case eq:			return a.compareTo(b) == 0 ? TrueValue.get() : null;
			case grtr:			return a.compareTo(b) > 0 ? TrueValue.get() : null;
			case grtreq:		return a.compareTo(b) >= 0 ? TrueValue.get() : null;
			case noteq:			return a.compareTo(b) != 0 ? TrueValue.get() : null;
			default: 			return null;
			}
		}
	}
	static class BoolNotFunc extends Function {
		private static final long serialVersionUID = 1L;
		public BoolNotFunc() {
			super(new ArrayList<String>(), null);
			argnames.add("expression");
		}
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			return args.get(0).evaluate(s).resolve(s) == null ? TrueValue.get() : null;
		}
	}
	static class sumReg extends Function{
		private static final long serialVersionUID = 1L;

		public sumReg() {
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			SubExpression sx = (SubExpression)args.get(0);
			Value lv = sx.left.evaluate(s);
			Value rv = sx.right.evaluate(s);
			if(lv.getClass() == CellReference.class && rv.getClass() == CellReference.class){
				return (new RegionReference(((CellReference)lv).colIdx, ((CellReference)lv).rowIdx, ((CellReference)rv).colIdx, ((CellReference)rv).rowIdx)).sum(s);
			} else {
				return new ErrorValue("Cannot find sum of anything but a cell region");
			}
		}
	}
	static class avgReg extends Function{
		private static final long serialVersionUID = 1L;

		public avgReg() {
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			SubExpression sx = (SubExpression)args.get(0);
			Value lv = sx.left.evaluate(s);
			Value rv = sx.right.evaluate(s);
			if(lv.getClass() == CellReference.class && rv.getClass() == CellReference.class){
				return (new RegionReference(((CellReference)lv).colIdx, ((CellReference)lv).rowIdx, ((CellReference)rv).colIdx, ((CellReference)rv).rowIdx)).average(s);
			} else {
				return new ErrorValue("Cannot find sum of anything but a cell region");
			}
		}
	}
	static class newSheet extends Function{
		private static final long serialVersionUID = 1L;

		public newSheet(){
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			try{
			s.newSize((int)(((Number) args.get(0).evaluate(s).resolve(s)).v),(int)(((Number) args.get(1).evaluate(s).resolve(s)).v));
			}catch(IndexOutOfBoundsException e){
				System.out.println("<Please Type Two Numebers After New>");
			}
			return null;
		}
	}
	static class saveSheet extends Function{
		private static final long serialVersionUID = 1L;

		public saveSheet(){
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			try{
				s.Save(((StringValue) args.get(0).evaluate(s).resolve(s)).v);
			}catch (ClassCastException e){
				System.out.println("<Please Put Name In Quotes>");
			}catch (NullPointerException e){
				System.out.println("<Please Put Name In Quotes");
			}
			return null;
		}
	}
	static class loadSheet extends Function{
		private static final long serialVersionUID = 1L;

		public loadSheet(){
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			try{
			s.load(((StringValue) args.get(0).evaluate(s).resolve(s)).v);
			}catch (ClassCastException e){
				System.out.println("<Please Put Name In Quotes>");
			}catch (NullPointerException e){
				System.out.println("<Please Put Name In Quotes");
			}
			return null;
		}
	}
	static class deleteSheet extends Function{
		private static final long serialVersionUID = 1L;

		public deleteSheet(){
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			try{
			s.delete(((StringValue) args.get(0).evaluate(s).resolve(s)).v);
			}catch (ClassCastException e){
				System.out.println("<Please Put Name In Quotes>");
			}catch (NullPointerException e){
				System.out.println("<Please Put Name In Quotes");
			}
			return null;
		}
	}
	static class deleteFolder extends Function{
		private static final long serialVersionUID = 1L;

		public deleteFolder(){
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			Spreadsheet.deleteFolder();
			return null;
		}
	}
	static class sortLine extends Function{
		private static final long serialVersionUID = 1L;
		
		public sortLine(){
			super(new ArrayList<String>(),null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args){
			try {
				if(args.get(0).getClass() == Identifier.class){
					Identifier id = (Identifier)args.get(0);
					s.sort(id.nm.toUpperCase().charAt(0));
					return null;
				}
				Value temp = args.get(0).evaluate(s).resolve(s);
				if(temp.getClass() == Number.class){
					s.sort((int)(((Number) temp).v));
				} 
			} catch(ClassCastException e) {
				System.out.println("Error: " + e.getMessage() + "\nEnsure that the row/column only contains numbers or strings\n");
			}
			
			return null;
		}
	}
	static class helpFunc extends Function {
		private static final long serialVersionUID = 1L;

		public helpFunc() {
			super(new ArrayList<String>(), null);
		}
		
		@Override
		public Value apply(Spreadsheet s, List<Expression> args) {
			if(args.size() == 0) {
				System.out.println(HelpSystem.mainHelpMessage());
			} else if(args.size() == 1) {
				System.out.println(HelpSystem.helpMessage(
						((pw.qxczv.TextExcel.AST.Identifier)(args.get(0))).nm));
			}
			return null;
		}
	}
	public static void apply(Spreadsheet s) {
		s.setValue("print", new PrintFunc());
		s.setValue("clear", new ClearFunc());
		s.setValue("esc", new EscFunc());
		s.setValue("<", new CompFunc(CompFunc.ComparisonOp.less));
		s.setValue("<eq", new CompFunc(CompFunc.ComparisonOp.lesseq));
		s.setValue("eq", new CompFunc(CompFunc.ComparisonOp.eq));
		s.setValue(">", new CompFunc(CompFunc.ComparisonOp.grtr));
		s.setValue(">eq", new CompFunc(CompFunc.ComparisonOp.grtreq));
		s.setValue("!eq", new CompFunc(CompFunc.ComparisonOp.noteq));
		s.setValue("!", new BoolNotFunc());
		s.setValue("sum", new sumReg());
		s.setValue("avg", new avgReg());
		s.setValue("new", new newSheet());
		s.setValue("save", new saveSheet());
		s.setValue("load", new loadSheet());
		s.setValue("kill", new deleteFolder());
		s.setValue("delete", new deleteSheet());
		s.setValue("sort", new sortLine());
		s.setValue("help", new helpFunc());
	}
}

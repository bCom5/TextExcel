package pw.qxczv.TextExcel;

import java.util.Scanner;

import pw.qxczv.TextExcel.AST.Expression;
import pw.qxczv.TextExcel.AST.Parser;
import pw.qxczv.TextExcel.Values.CellReference;
import pw.qxczv.TextExcel.Values.Value;

public class Main {

    public static void main(String[] args) {
    	Spreadsheet s = new Spreadsheet(7,12);
    	Scanner userInput = new Scanner(System.in);
    	Parser p = new Parser();
    	System.out.println("Enter Command->");
    	while(userInput.hasNextLine()) {
    		String ln = userInput.nextLine();
    		if(ln.equals("print")){
    			s.print();
    		}
    		else if (ln.equals("exit")){
    			System.out.print("--Goodbye--");
    			break;
    		} 
    		else if (ln.equals("clear")){
    			System.out.println("--Cleared--");
    			s.clear();
    		} else {
	    		try {
					Expression x = p.parse(ln);
					Value v = x.evaluate(s);
					if(v != null) {
						if(v.getClass() == CellReference.class) {
							Value nv = ((CellReference)v).rawDeref(s);
							System.out.println(v.toString() + " = " + ((nv == null) ? "<empty>" : nv.toString())) ;
						}
						else {
							Value r = v.resolve(s);
							if(r == null) System.out.println("<empty>"); //silly special cases
							else System.out.println(v.resolve(s));
						}
					}
				} catch (Exception e) {
					//Change when ship
					e.printStackTrace();
				}
    		}
    		System.out.println("Enter Command->");
    	}
    	
    	userInput.close();
    }
}

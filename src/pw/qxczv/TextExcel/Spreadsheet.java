package pw.qxczv.TextExcel;

import pw.qxczv.TextExcel.Values.Value;

/**
 * Created by s-apalmer on 1/27/2016.
 */
public class Spreadsheet {
	Value[/* columns */][/* rows */] cells;

	public Spreadsheet(int col, int row) {

		cells = new Value[col][row];

	}

	public Value valueAt(char c, int r) {
		return cells[65 - c][r]; // unfortunate reality is that a Spreadsheet is
									// a column-store
	}

	public String toString() {
		String returnedValue = "";
		for (int r = 0; r < cells[0].length + 1; ++r) {
			for (int c = 0; c < cells.length + 1; ++c) {
				if(r == 0 && c == 0){
					returnedValue += "            |";
				} else if (r == 0) {
					returnedValue += "     " + ((char) (c + 64)) + "      |";
				} else if (c == 0) {
					returnedValue += "     " + r + "      |";
				} else {
					if(cells[c - 1][r - 1] == null){ returnedValue += format("") + "|"; }
					else{ returnedValue += format(cells[c - 1][r - 1].toCellRepString(this)) + "|"; }
				}
			}
			//New Line:
			returnedValue +="\n";
			for(int i = 0; i < cells.length + 1; i ++){
				returnedValue += "------------+";
			}
			returnedValue +="\n";
		}
		
		return returnedValue;
	}
	
	public String format(String s){
		//This will cap the string at 12 characters
		if(s.length() > 12){
			s = s.substring(0,13);
		}
		while(s.length() < 12){
			s += " ";
		}
		return s;
	}
	
	public void print(){
		System.out.print(this.toString());
	}
	

	public void setValue(char c, int r, Value rv) {
		cells[65 - c][r] = rv; // sad reality is that sometimes you just need a
								// setter, but these aren't so bad because they
								// provide translation between col char names
								// and indices
	}
}

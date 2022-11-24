import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class assembler_pass1 {
	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			String inputfilename = "Input.txt";
			fr = new FileReader(inputfilename);
			br = new BufferedReader(fr);

			String OUTPUTFILENAME = "OUTPUT/IC.txt";
			fw = new FileWriter(OUTPUTFILENAME);
			bw = new BufferedWriter(fw);

			//hashtable for imperative statements
			Hashtable<String, String> IS = new Hashtable<String, String>();
			IS.put("STOP", "00");
			IS.put("ADD", "01");
			IS.put("SUB", "02");
			IS.put("MULT", "03");
			IS.put("MOVER", "04");
			IS.put("MOVEM", "05");
			IS.put("COMP", "06");
			IS.put("BC", "07");
			IS.put("DIV", "08");
			IS.put("READ", "09");
			IS.put("PRINT", "10");

			//hashtable for declarative statements
			Hashtable<String, String> DL = new Hashtable<String, String>();
			DL.put("DC", "01");
			DL.put("DS", "02");

			//hashtable for assembler directives
			Hashtable<String, String> AD = new Hashtable<String, String>();

			AD.put("START", "01");
			AD.put("END", "02");
			AD.put("ORIGIN", "03");
			AD.put("EQU", "04");
			AD.put("LTORG", "05");

			Hashtable<String, String> symbolTable = new Hashtable<String, String>();
			Hashtable<String, String> literalTable = new Hashtable<String, String>();
			ArrayList<Integer> poolTable=new ArrayList<Integer>();

			String CurrentLine;
			int locationPointer = 0;
			int litptr = 1;
			int symptr = 1;
			int poolTablePointer = 1;
			
			//read the input.txt file which contains code line by line
			CurrentLine = br.readLine();

			String s1 = CurrentLine.split(" ")[1];
			if (s1.equals("START")) {

				bw.write("(AD  ,\t01)\t");
				String s2 = CurrentLine.split(" ")[2];
				bw.write("(C  ,\t" + s2 + ")" + "\n");
				locationPointer = Integer.parseInt(s2);

			}

			while ((CurrentLine = br.readLine()) != null) {

				int mind_the_LC = 0;
				String type = null;

				 
				int flag2 = 0;		

			 
				String s = CurrentLine.split(" |\\,")[0];	
				//allocating addr to arrived symbols
				for (Map.Entry m : symbolTable.entrySet()) {				
					if (s.equals(m.getKey())) {
						m.setValue(locationPointer);
						flag2 = 1;
					}
				}
				if (s.length() != 0 && flag2 == 0) {		
					 		
					symbolTable.put(s, String.valueOf(locationPointer));
					symptr++;
				}

				int isOpcode = 0;	 
				
				s = CurrentLine.split(" |\\,")[1];			
				
				for (Map.Entry m : IS.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("(IS  ,\t" + m.getValue() + ")" + "\t");		 
						type = "is";
						isOpcode = 1;
					}
				}
				
				
				for (Map.Entry m : AD.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("(AD  ,\t" + m.getValue() + ")" + "\t");		 
						type = "ad";
						isOpcode = 1;
					}
				}
				for (Map.Entry m : DL.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("(DL  ,\t" + m.getValue() + ")" + "\t");		 
						type = "dl";
						isOpcode = 1;
					}
				}
				
				
				if (s.equals("LTORG")) {
					poolTable. add(poolTablePointer);
					for (Map.Entry m : literalTable.entrySet()) {
						if (m.getValue() == "") {			 
							m.setValue(locationPointer);
							locationPointer++;
							poolTablePointer++;
							mind_the_LC = 1;
							isOpcode = 1;
						}
					}
				}
				
				
				if (s.equals("END")) {
					poolTable. add(poolTablePointer);
					for (Map.Entry m : literalTable.entrySet()) {
						if (m.getValue() == "") {
							m.setValue(locationPointer);
							locationPointer++;
							mind_the_LC = 1;
						}
					}
				}
				
				
				if(s.equals("EQU")){
					symbolTable.put("equ", String.valueOf(locationPointer));
				}
				
				
				if (CurrentLine.split(" |\\,").length > 2) {	
					s = CurrentLine.split(" |\\,")[2];				 
																	
			//this is our first operand.it must be either a Register/Declaration/Symbol
					if (s.equals("AREG")) {
						bw.write("(RG  ,\t1) ");
						isOpcode = 1;
					} else if (s.equals("BREG")) {
						bw.write("(RG  ,\t2) ");
						isOpcode = 1;
					} else if (s.equals("CREG")) {
						bw.write("(RG  ,\t3) ");
						isOpcode = 1;
					} else if (s.equals("DREG")) {
						bw.write("4\t");
						isOpcode = 1;
					} else if (type == "dl") {
						bw.write("(C  ,\t" + s + ")" + "\t");
					} else {
						symbolTable.put(s, "");				
					}
				}
				
				
				if (CurrentLine.split(" |\\,").length > 3) {		
					
					//if there are 4 words consider 4th word
					s = CurrentLine.split(" |\\,")[3];		 
					//this is our 2nd operand it is either a literal, or a symbol
					if (s.contains("=")) {
						literalTable.put(s, "");
						bw.write("(L  ," + litptr + ")" + "\t");
						isOpcode = 1;
						litptr++;
					} else {
						symbolTable.put(s, "");			
													
						bw.write("(S  ," + symptr + ")" + "\t");		
						symptr++;
						
					}
				}

				bw.write("\n");		 

				if (mind_the_LC == 0)
					locationPointer++;
			}

			String f1 = "OUTPUT/symbolTable.txt";
			FileWriter fw1 = new FileWriter(f1);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			bw1.write("SYMBOL   LOCATION") ;
			bw1.write("\n") ; 
			for (Map.Entry m : symbolTable.entrySet()) {
				bw1.write(m.getKey() + "\t" + m.getValue()+"\n");				
				// System.out.println(m.getKey() + " " + m.getValue());
			}

			String f2 = "OUTPUT/literalTable.txt";
			FileWriter fw2 = new FileWriter(f2);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			
			bw2.write("LITERAL    LOCATION") ;
			bw2.write("\n") ; 
			for (Map.Entry m : literalTable.entrySet()) {
				bw2.write(m.getKey() + "\t" + m.getValue()+"\n");
				// System.out.println(m.getKey() + " " + m.getValue());
			}
			
			String f3 = "OUTPUT/poolTable.txt";
			FileWriter fw3 = new FileWriter(f3);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			for (Integer item :poolTable) {  
				bw3.write(item+"\n");
			    // System.out.println(item);
			}

			bw.close();
			bw1.close();
			bw2.close();
			bw3.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.lang.*;

public class assembler_pass2{

	public static void main(String[] args) {

		try {

			String f = "OUTPUT/IC.txt";
			FileReader fw = new FileReader(f);
			BufferedReader ICFile = new BufferedReader(fw);

			String f1 = "OUTPUT/symbolTable.txt";
			FileReader fw1 = new FileReader(f1);
			BufferedReader symbolTableFile = new BufferedReader(fw1);
			symbolTableFile.mark(500);

			String f2 = "OUTPUT/literalTable.txt";
			FileReader fw2 = new FileReader(f2);
			BufferedReader literalTableFile = new BufferedReader(fw2);
			literalTableFile.mark(500);

			String literalTable[][] = new String[10][2];// { literal value and address}

			Hashtable<String, String> symbolTable = new Hashtable<String, String>();
			String str;
			int z = 0;
			while ((str = literalTableFile.readLine()) != null) {

				literalTable[z][0] = str.split("\t")[0];
				literalTable[z][1] = str.split("\t")[1];
				z++;
			}
			while ((str = symbolTableFile.readLine()) != null) {
				symbolTable.put(str.split("\t")[0], str.split("\t")[1]);
			}

			String f3 = "OUTPUT/poolTable.txt";
			FileReader fw3 = new FileReader(f3);
			BufferedReader poolTableFile = new BufferedReader(fw3);

			String f4 = "OUTPUT/machineCode.txt";
			FileWriter fw4 = new FileWriter(f4);
			BufferedWriter machineCodeFile = new BufferedWriter(fw4);

			ArrayList<Integer> poolTable = new ArrayList<Integer>();
			String t;
			while ((t = poolTableFile.readLine()) != null) {
				poolTable.add(Integer.parseInt(t));
			}

			int poolTableptr = 1;
			int temp1 = poolTable.get(0);
			int temp2 = poolTable.get(1);

			String CurrentLine;
			CurrentLine = ICFile.readLine();
			int locationPointer = 0;
			locationPointer = Integer.parseInt(CurrentLine.split("\t")[3]);

			while ((CurrentLine = ICFile.readLine()) != null) {

				machineCodeFile.write(locationPointer + "\t");

				String s0 = CurrentLine.split("\t")[0];

				String s1 = CurrentLine.split("\t")[1];

				if (s0.equals("IS")) {
					machineCodeFile.write(s1 + "\t");

					if (CurrentLine.split("\t").length == 5) {

						machineCodeFile.write(CurrentLine.split("\t")[2] + "\t");

						if (CurrentLine.split("\t")[3].equals("L")) {
							int add = Integer.parseInt(CurrentLine.split("\t")[4]);
							machineCodeFile.write(literalTable[add - 1][1]);

						}

						if (CurrentLine.split("\t")[3].equals("S")) {
							int add1 = Integer.parseInt(CurrentLine.split("\t")[4]);
							int i = 1;

							for (Map.Entry m : symbolTable.entrySet()) {
								if (i == add1) {
									machineCodeFile.write((String) m.getValue());
								}
								i++;
							}

						}
					}

					else {
						machineCodeFile.write("0\t000");
					}
				}

				if (s0.equals("AD")) {
					literalTableFile.reset();
					// if it is LTORG
					if (s1.equals("05")) {
						int j = 1;
						while (j < temp1) {
							literalTableFile.readLine();
						}
						while (temp1 < temp2) {
							machineCodeFile.write("00\t0\t00" + literalTableFile.readLine().split("'")[1]);
							if (temp1 < (temp2 - 1)) {
								locationPointer++;
								machineCodeFile.write("\n");
								machineCodeFile.write(locationPointer + "\t");
							}
							temp1++;
						}
						temp1 = temp2;
						poolTableptr++;
						if (poolTableptr < poolTable.size()) {
							temp2 = poolTable.get(poolTableptr);
						}
					}
					int j = 1;
					// for end statements
					if (s1.equals("02")) {
						String s;
						while ((s = literalTableFile.readLine()) != null) {
							if (j >= temp1)
								machineCodeFile.write("00\t0\t00" + s.split("'")[1]);
							j++;
						}
					}
				}

				// for declarative statements
				if (s0.equals("DL") && s1.equals("01")) {
					machineCodeFile.write("00\t0\t00" + CurrentLine.split("'")[1]);

				}

				locationPointer++;
				machineCodeFile.write("\n");
			}
			ICFile.close();
			symbolTableFile.close();
			literalTableFile.close();
			poolTableFile.close();
			machineCodeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

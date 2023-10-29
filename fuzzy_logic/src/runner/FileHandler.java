package runner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import inference.InferenceEngine;
import inference.Operators;
import shapes.Shape;
import shapes.Triangle;
import shapes.Trapezoid;
import variables.InVariable;
import variables.OutVariable;
import variables.Variable;

public class FileHandler {
	@SuppressWarnings("resource")
	static InferenceEngine readFile() throws Exception {
		System.out.println();
		InferenceEngine engine = new InferenceEngine();
		/*
		 * explanins the format of the input file
		 */
		System.out.println("the file should be in the following format:\n\n" + "variables:\n" + "----------"
				+ "var(1) data\n" + "var(2) data\n" + "var(n) data\n" + "x\n\n" + "fuzzy sets:" + "-----------\n"
				+ "var(1) fuzzy sets data\n" + "x\n" + "var(2) fuzzy sets data\n" + "x\n" + "var(n) fuzzy sets data\n"
				+ "x\n\n" + "rules:" + "------\n" + "rule(1)\n" + "rule(2)\n" + "rule(n)\n" + "x\n\n" + "crisp values:"
				+ "-------------\n" + "input var(1) crisp value\n" + "input var(2) crisp value\n"
				+ "input var(n) crisp value\n\n"
				+ "(note: the crisp values must be enterd in same order the input variables were entered)\n\n"
				+ "var(i) data format:\n" + "var(i)Name var(i)Type(IN\\OUT) var(i)Range[lower,upper]\n\n"
				+ "var(i) fuzzy sets data format:\n"
				+ "fuzzySet(1)Name fuzzySet(1)Type(Tri\\Trap) values(seprated by spaces)\n"
				+ "fuzzySet(2)Name fuzzySet(2)Type(Tri\\Trap) values(seprated by spaces)\n"
				+ "fuzzySet(n)Name fuzzySet(n)Type(Tri\\Trap) values(seprated by spaces)\n"
				+ "(note: the list of fuzzy sets must be entered in the same order the variables were entered)\n" + "\n"
				+ "rule(i) format:\n"
				+ "(Unary_Operator IN_variable\\IN_variable) set Binary_Operator IN_variable set => OUT_variable set\n"
				+ "(note: the supported operators are: " + Arrays.toString(Operators.getOperators()) + ")\n");
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new FileNameExtensionFilter(".txt files", "txt"));
		fileChooser.setDialogTitle("choose input file");
		int responce = fileChooser.showOpenDialog(null);
		File inputfile = null;
		File outputFile = null;
		if (responce == JFileChooser.APPROVE_OPTION) {
			inputfile = new File(fileChooser.getSelectedFile().getAbsolutePath());
		} else {
			throw new FileNotFoundException("intput file not found!\n");
		}
		fileChooser.setDialogTitle("choose output file");
		responce = fileChooser.showOpenDialog(null);
		if (responce == JFileChooser.APPROVE_OPTION) {
			outputFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
		} else {
			throw new FileNotFoundException("output file not found!\n");
		}
		Scanner scanner = Main.scanner;
		scanner = new Scanner(inputfile);
		Boolean isValidInputFile = false;
		while (scanner.hasNextLine()) {
			String testInput = scanner.nextLine();
			if (testInput.equalsIgnoreCase("")) {
				continue;
			} else if (testInput.equalsIgnoreCase("variables:")) {
				isValidInputFile = true;
				break;
			}
		}
		if (isValidInputFile) {
			scanner.nextLine();
			String input = "";
			while (true) {
				input = scanner.nextLine();
				if (input.equalsIgnoreCase("x")) {
					break;
				}
				LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.VARS_PHASE, input,
						new String[] { "[", ",", "]" }, new int[] { 4 });
				if (parts == null) {
					throw new IOException("the format of one of the variables is invalid");
				}
				Variable variable;
				try {
					if (parts.get(1).equalsIgnoreCase("in")) {
						variable = new InVariable(parts.get(0), Double.parseDouble(parts.get(2)),
								Double.parseDouble(parts.get(3)));

					} else {
						variable = new OutVariable(parts.get(0), Double.parseDouble(parts.get(2)),
								Double.parseDouble(parts.get(3)));
					}
				} catch (Exception e) {
					throw new IOException("variable " + parts.get(0) + "'s range values are invalid\n");
				}
				if (variable.isValidVariable()) {
					engine.getVariables().add(variable);
				} else {
					throw new IOException("variable " + parts.get(0) + "'s range values are invalid\n");
				}
			}
			isValidInputFile = false;
		} else {
			throw new IOException("input file format is invalid");
		}
		while (scanner.hasNextLine()) {
			String testInput = scanner.nextLine();
			if (testInput.equalsIgnoreCase("")) {
				continue;
			} else if (testInput.equalsIgnoreCase("fuzzy sets:")) {
				isValidInputFile = true;
				break;
			}
		}
		if (isValidInputFile) {
			scanner.nextLine();
			for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				while (true) {
					String input = "";
					input = scanner.nextLine();
					if (input.equalsIgnoreCase("x")) {
						break;
					}
					LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.FUZZY_SETS_PHASE, input, null,
							new int[] { 5, 6 });
					if (parts == null) {
						throw new IOException("the format of one of the fuzzy sets is invalid");
					}
					Shape shape;
					try {
						if (parts.get(1).equalsIgnoreCase("tri")) {
							shape = new Triangle(variable, parts.get(0), Double.parseDouble(parts.get(2)),
									Double.parseDouble(parts.get(3)), Double.parseDouble(parts.get(4)));
						} else {
							shape = new Trapezoid(variable, parts.get(0), Double.parseDouble(parts.get(2)),
									Double.parseDouble(parts.get(3)), Double.parseDouble(parts.get(4)),
									Double.parseDouble(parts.get(5)));
						}
					} catch (Exception e) {
						throw new IOException("fuzzy set " + parts.get(0) + "'s valuses are invalid");
					}
					if (shape.isValidSet()) {
						variable.getFuzzySets().add(shape);
					} else {
						throw new IOException("fuzzy set " + parts.get(0) + "'s valuses are invalid");
					}
				}
			}
			isValidInputFile = false;
		} else {
			throw new IOException("input file format is invalid");
		}
		while (scanner.hasNextLine()) {
			String testInput = scanner.nextLine();
			if (testInput.equalsIgnoreCase("")) {
				continue;
			} else if (testInput.equalsIgnoreCase("rules:")) {
				isValidInputFile = true;
				break;
			}
		}
		if (isValidInputFile) {
			scanner.nextLine();
			while (true) {
				String input = "";
				input = scanner.nextLine();
				if (input.equalsIgnoreCase("x")) {
					break;
				}
				LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.RULES_PHASE, input, null,
						new int[] { 8, 9 });
				if (parts == null) {
					throw new IOException("the format of one of the rules is invalid");
				}
				if (engine.isValidRule(input)) {
					engine.getRules().add(input);
				} else {
					throw new IOException("rule(" + input + ") is invalid");
				}
			}
			isValidInputFile = false;
		} else {
			throw new IOException("input file format is invalid");
		}
		while (scanner.hasNextLine()) {
			String testInput = scanner.nextLine();
			if (testInput.equalsIgnoreCase("")) {
				continue;
			} else if (testInput.equalsIgnoreCase("crisp values:")) {
				isValidInputFile = true;
				break;
			}
		}
		if (isValidInputFile) {
			scanner.nextLine();
			for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				if (variable instanceof InVariable) {
					try {
						variable.setCrispVal(scanner.nextDouble());
					} catch (Exception e) {
						throw new IOException("variable " + variable.getName() + "'s crisp value is invalid");
					}
				}

			}
		} else {
			throw new IOException("input file format is invalid");
		}
		System.out.println("Running the simulation…");
		for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if (variable instanceof InVariable) {
				InVariable inVariable = (InVariable) variable;
				inVariable.fuzzification();
			}
		}
		System.out.println("Fuzzification => done");
		engine.inference();
		System.out.println("Inference => done");
		for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if (variable instanceof OutVariable) {
				OutVariable outVariable = (OutVariable) variable;
				outVariable.deFuzzification();
			}
		}
		System.out.println("Defuzzification => done\n");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, true));
		for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if (variable instanceof OutVariable) {
				OutVariable outVariable = (OutVariable) variable;
				bufferedWriter.write("\nThe predicted " + outVariable.getName() + " is "
						+ outVariable.highestMembership() + " (" + outVariable.getCrispVal() + ")\n");
			}
		}
		bufferedWriter.close();
		return engine;
	}
}

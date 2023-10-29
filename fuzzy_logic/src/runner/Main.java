package runner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import gui.GUI;
import inference.InferenceEngine;
import inference.Operators;
import shapes.Shape;
import shapes.Triangle;
import shapes.Trapezoid;
import variables.InVariable;
import variables.OutVariable;
import variables.Variable;

public class Main {
	static Scanner scanner;

	public static void main(String[] args) {
		InferenceEngine engine = null;
		scanner = new Scanner(System.in);
		while (true) {
			System.out.println();
			System.out.println("Fuzzy Logic Toolbox\n===================");
			int choice = -1;
			System.out.println("1- Create a new fuzzy system\n"
					+ "2- Create a new fuzzy system with input vars crisp values from an input file"
					+ " and store output on an output file" + "\n3- Quit\n");
			while (true) {
				try {
					choice = scanner.nextInt();
					scanner.nextLine();
					break;
				} catch (Exception e) {
					System.out.println("invalid input enter it again:");
					scanner.nextLine();
					continue;
				}
			}
			while (choice < 1 || choice > 3) {
				while (true) {
					try {
						choice = scanner.nextInt();
						scanner.nextLine();
						break;
					} catch (Exception e) {
						System.out.println("invalid input enter it again:");
						scanner.nextLine();
						continue;
					}
				}
			}
			if (choice == 1) {
				engine = new InferenceEngine();
				System.out.println(
						"Enter the system’s name and a brief description:\n------------------------------------------------");
				String description = "";
				while (!description.contains(".")) {
					description += scanner.nextLine();
				}
			}
			if (choice == 2) {
				try {
					System.out.println();
					System.out.println(
							"Enter the system’s name and a brief description:\n------------------------------------------------");
					String description = "";
					while (!description.contains(".")) {
						description += scanner.nextLine();
					}
					engine = FileHandler.readFile();
					GUI gui = new GUI();
					gui.getGUI(engine);
					System.out.println();
					scanner = new Scanner(System.in);
				} catch (Exception e) {
					System.err.println(e.getMessage());
					continue;
				}
			}
			if (choice == 3) {
				scanner.close();
				System.exit(0);
			}
			choice = -1;
			System.out.println();
			while (true) {
				System.out.println("Main Menu:\n" + "==========\n" + "1- Add variables.\n"
						+ "2- Add fuzzy sets to an existing variable.\n" + "3- Add rules.\n"
						+ "4- Run the simulation on crisp values.\n" + "5- exit Main Menu\n");
				while (true) {
					try {
						choice = scanner.nextInt();
						scanner.nextLine();
						break;
					} catch (Exception e) {
						System.out.println("invalid input enter it again:");
						scanner.nextLine();
						continue;
					}
				}
				while (choice < 1 || choice > 5) {
					while (true) {
						try {
							choice = scanner.nextInt();
							scanner.nextLine();
							break;
						} catch (Exception e) {
							System.out.println("invalid input enter it again:");
							scanner.nextLine();
							continue;
						}
					}
				}
				if (choice == 1) {
					System.out.println();
					String input = "";
					System.out.println("Enter the variable’s name, type (IN/OUT) and range ([lower,"
							+ "upper]): (enter x to finish)\n"
							+ "-------------------------------------------------------------------");
					while (true) {
						input = scanner.nextLine();
						if (input.equalsIgnoreCase("x")) {
							break;
						}
						LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.VARS_PHASE, input,
								new String[] { "[", ",", "]" }, new int[] { 4 });
						if (parts == null) {
							System.out.println("invalid input enter it again:");
							continue;
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
							System.out.println("invalid input enter it again:");
							continue;
						}
						if (variable.isValidVariable()) {
							engine.getVariables().add(variable);
						} else {
							System.out.println("invalid input enter it again:");
							continue;
						}
					}
					System.out.println();
					continue;
				} else if (choice == 2) {
					System.out.println();
					if (engine.getVariables().isEmpty()) {
						System.out.println("can't add fuzzy sets! add variables first.\n");
						continue;
					}
					for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
						Variable variable = iterator.next();
						System.out.println("enter the fuzzy sets for variable " + variable.getName() + "\n"
								+ "Enter the fuzzy set name, type (TRI/TRAP) and values: (enter x to " + "finish)\n"
								+ "-----------------------------------------------------");
						while (true) {
							String input = "";
							input = scanner.nextLine();
							if (input.equalsIgnoreCase("x")) {
								break;
							}
							LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.FUZZY_SETS_PHASE,
									input, null, new int[] { 5, 6 });
							if (parts == null) {
								System.out.println("invalid input enter it again:");
								continue;
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
								System.out.println("invalid input enter it again:");
								continue;
							}
							if (shape.isValidSet()) {
								variable.getFuzzySets().add(shape);
							} else {
								System.out.println("invalid input enter it again:");
								continue;
							}
						}
					}
					System.out.println();
					continue;
				} else if (choice == 3) {
					System.out.println();
					Boolean exitCondtion = false;
					if (engine.getVariables().isEmpty()) {
						System.out.println("can't add rules! add variables first.\n");
						continue;
					}
					for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
						Variable variable = iterator.next();
						if (variable.getFuzzySets().isEmpty()) {
							System.out.println(
									"can't add rules! add fuzzy sets to variable " + variable.getName() + " first.");
							exitCondtion = true;
						}
					}
					if (exitCondtion == true) {
						System.out.println();
						continue;
					}
					System.out.println("Enter the rules in this format:\n"
							+ "(Unary_Operator IN_variable\\IN_variable) set Binary_Operator IN_variable set => OUT_variable set\n"
							+ "the supported operators are: " + Arrays.toString(Operators.getOperators()) + "\n"
							+ "(enter x to finish)\n" + "------------------------------------------------------------");
					while (true) {
						String input = "";
						input = scanner.nextLine();
						if (input.equalsIgnoreCase("x")) {
							break;
						}
						LinkedList<String> parts = SyntaxChecker.cheakSyntax(PreSimRunPhases.RULES_PHASE, input, null,
								new int[] { 8, 9 });
						if (parts == null) {
							System.out.println("invalid input enter it again:");
							continue;
						}
						if (engine.isValidRule(input)) {
							engine.getRules().add(input);
						} else {
							System.out.println("invalid input enter it again:");
							continue;
						}
					}
					System.out.println();
					continue;
				} else if (choice == 4) {
					System.out.println();
					if (engine.getVariables().isEmpty()) {
						System.out.println("can't run simulation! add variables first.\n");
						continue;
					}
					Boolean exitCondtion = false;
					for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
						Variable variable = iterator.next();
						if (variable.getFuzzySets().isEmpty()) {
							System.out.println("can't run simulation! add fuzzy sets to variable " + variable.getName()
									+ " first.");
							exitCondtion = true;
						}
					}
					if (engine.getRules().isEmpty()) {
						System.out.println("can't run simulation! add rules first.");
						exitCondtion = true;
					}
					if (exitCondtion == true) {
						System.out.println();
						continue;
					}
					System.out.println("Enter the crisp values:\n" + "-----------------------");
					for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
						Variable variable = iterator.next();
						if (variable instanceof InVariable) {
							while (true) {
								double crispVal;
								System.out.print(variable.getName() + ": ");
								try {
									crispVal = scanner.nextDouble();
									scanner.nextLine();
								} catch (Exception e) {
									System.out.println("invalid input enter it again");
									scanner.nextLine();
									continue;
								}
								if (crispVal < variable.getRange()[0] || crispVal > variable.getRange()[1]) {
									System.out.println("invalid input enter it again");
									continue;
								} else {
									variable.setCrispVal(crispVal);
									break;
								}
							}
						}
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
					for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
						Variable variable = iterator.next();
						if (variable instanceof OutVariable) {
							OutVariable outVariable = (OutVariable) variable;
							System.out.println("The predicted " + outVariable.getName() + " is "
									+ outVariable.highestMembership() + " (" + outVariable.getCrispVal() + ")");
						}
					}
					GUI gui = new GUI();
					gui.getGUI(engine);
					System.out.println();
					continue;
				} else if (choice == 5) {
					System.out.println();
					break;
				}
			}
		}
	}

}

package inference;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import shapes.Shape;
import variables.OutVariable;
import variables.Variable;

public class InferenceEngine {

	private LinkedList<String> rules = new LinkedList<String>();
	private LinkedList<Variable> variables = new LinkedList<Variable>();

	/**
	 * @return the rules
	 */
	public LinkedList<String> getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(LinkedList<String> rules) {
		this.rules = rules;
	}

	/**
	 * @return the variables
	 */
	public LinkedList<Variable> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(LinkedList<Variable> variables) {
		this.variables = variables;
	}

	/**
	 * 
	 * @param Variable
	 * @param Set
	 * @return the set with the given name within the given variable
	 */
	private Shape getSet(String Variable, String Set) {
		for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if (variable.getName().equalsIgnoreCase(Variable)) {
				for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
					Shape shape = iterator2.next();
					if (shape.getName().equalsIgnoreCase(Set)) {
						return shape;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param operator1
	 * @param InVariable1
	 * @param InSet1
	 * @param operator2
	 * @param InVariable2
	 * @param InSet2
	 * @param OutVariable
	 * @param OutSet      <br>
	 *                    applys the rules
	 */
	//proj_funding high or exp_level expert => risk low
	//not proj_funding high or exp_level expert => risk low
	private void applyRule(Operators operator1, String InVariable1, String InSet1, Operators operator2,
			String InVariable2, String InSet2, String OutVariable, String OutSet) {
		double operand1 = 0.0;
		double operand2 = 0.0;
		if (operator1 == null) {
			operand1 = getSet(InVariable1, InSet1).getMembership();
		} else if (operator1 == Operators.not) {
			operand1 = (1 - getSet(InVariable1, InSet1).getMembership());
		}
		if (operator2 == Operators.and) {
			operand2 = getSet(InVariable2, InSet2).getMembership();
			getSet(OutVariable, OutSet).addInferanceResult(Math.min(operand1, operand2));
		} else if (operator2 == Operators.or) {
			operand2 = getSet(InVariable2, InSet2).getMembership();
			getSet(OutVariable, OutSet).addInferanceResult(Math.max(operand1, operand2));
		} else if (operator2 == Operators.and_not) {
			operand2 = (1 - getSet(InVariable2, InSet2).getMembership());
			getSet(OutVariable, OutSet).addInferanceResult(Math.min(operand1, (1 - operand2)));
		} else if (operator2 == Operators.or_not) {
			operand2 = (1 - getSet(InVariable2, InSet2).getMembership());
			getSet(OutVariable, OutSet).addInferanceResult(Math.max(operand1, (1 - operand2)));
		}
	}

	//proj_funding high or exp_level expert => risk low
	//not proj_funding high or exp_level expert => risk low
	public void inference() {
		for (Iterator<String> iterator = rules.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			string = string.replace("=>", "");
			LinkedList<String> parts = new LinkedList<String>();
			Collections.addAll(parts, string.split(" "));
			while (parts.contains("")) {
				parts.remove("");
			}
			if (parts.size() == 8 && parts.get(0).equalsIgnoreCase(Operators.not.toString())) {
				applyRule(Operators.valueOf(parts.get(0)), parts.get(1), parts.get(2), Operators.valueOf(parts.get(3)),
						parts.get(4), parts.get(5), parts.get(6), parts.get(7));
			} else if (parts.size() == 7 && !parts.get(0).equalsIgnoreCase(Operators.not.toString())) {
				applyRule(null, parts.get(0), parts.get(1), Operators.valueOf(parts.get(2)), parts.get(3), parts.get(4),
						parts.get(5), parts.get(6));
			}
		}
		for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			if (variable instanceof OutVariable) {
				for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
					Shape shape = iterator2.next();
					shape.applyMaxResult();
				}

			}
		}
	}

	/**
	 * 
	 * @param rule
	 * @return true if the entered rule parameters are valid, returns false
	 *         otherwise
	 */
	//proj_funding high or exp_level expert => risk low
	//not proj_funding high or exp_level expert => risk low
	public Boolean isValidRule(String rule) {
		LinkedList<String> parts = new LinkedList<String>();
		Collections.addAll(parts, rule.split(" "));
		while (parts.contains("")) {
			parts.remove("");
		}
		int validOperatorsNum = 0;
		int validInVariablesNum = 0;
		int validInSetsNum = 0;
		int validOutVariablesNum = 0;
		int validOutSetsNum = 0;
		if (parts.size() == 9) {
			if (Operators.isValidOperator(parts.get(0)) && Operators.isUnary(parts.get(0))) {
				validOperatorsNum++;
			}
			if (Operators.isValidOperator(parts.get(3))) {
				validOperatorsNum++;
			}
			//cheaks if the number of IN\OUT vars and IN\OUT sets is valid
			for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				if (variable.getName().equalsIgnoreCase(parts.get(1))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(2))) {
							validInVariablesNum++;
							validInSetsNum++;
						}
					}
				}
				if (variable.getName().equalsIgnoreCase(parts.get(4))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(5))) {
							validInVariablesNum++;
							validInSetsNum++;
						}
					}
				}
				if (variable.getName().equalsIgnoreCase(parts.get(7))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(8))) {
							validOutVariablesNum++;
							validOutSetsNum++;
						}
					}
				}
			}
			if (validOperatorsNum == 2 && validInVariablesNum == 2 && validInSetsNum == 2 && validOutVariablesNum == 1
					&& validOutSetsNum == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			if (Operators.isValidOperator(parts.get(2))) {
				validOperatorsNum++;
			}
			//cheaks if the number of IN\OUT vars and IN\OUT sets is valid
			for (Iterator<Variable> iterator = variables.iterator(); iterator.hasNext();) {
				Variable variable = iterator.next();
				if (variable.getName().equalsIgnoreCase(parts.get(0))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(1))) {
							validInVariablesNum++;
							validInSetsNum++;
						}
					}
				}
				if (variable.getName().equalsIgnoreCase(parts.get(3))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(4))) {
							validInVariablesNum++;
							validInSetsNum++;
						}
					}
				}
				if (variable.getName().equalsIgnoreCase(parts.get(6))) {
					for (Iterator<Shape> iterator2 = variable.getFuzzySets().iterator(); iterator2.hasNext();) {
						Shape shape = iterator2.next();
						if (shape.getName().equalsIgnoreCase(parts.get(7))) {
							validOutVariablesNum++;
							validOutSetsNum++;
						}
					}
				}
			}
			if (validOperatorsNum == 1 && validInVariablesNum == 2 && validInSetsNum == 2 && validOutVariablesNum == 1
					&& validOutSetsNum == 1) {
				return true;
			} else {
				return false;
			}
		}
	}
}

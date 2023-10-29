package inference;

public enum Operators {
	not, and, or, and_not, or_not;

	/**
	 * 
	 * @return the list of supported operators
	 */
	public static String[] getOperators() {
		String[] supportedOperators = new String[Operators.values().length];
		int i = 0;
		for (Operators operator : Operators.values()) {
			supportedOperators[i] = operator.toString();
			i++;
		}
		return supportedOperators;
	}

	/**
	 * 
	 * @param operatorToValidate
	 * @return true if the operatorToValidate is a supported operator
	 */
	static Boolean isValidOperator(String operatorToValidate) {
		for (Operators operator : Operators.values()) {
			if (operatorToValidate.equalsIgnoreCase(operator.toString())) {
				return true;
			}
		}
		return false;
	}

	static Boolean isUnary(String operatorToValidate) {
		if (operatorToValidate.equalsIgnoreCase(Operators.not.toString())) {
			return true;
		} else {
			return false;
		}
	}
}

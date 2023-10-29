package variables;

import java.util.LinkedList;

import shapes.Shape;

public abstract class Variable {

	protected String name;
	protected double[] range = new double[2];
	protected LinkedList<Shape> fuzzySets = new LinkedList<Shape>();
	protected double crispVal;

	/**
	 * @param name
	 * @param low
	 * @param high
	 */
	public Variable(String name, double low, double high) {
		this.name = name;
		range[0] = low;
		range[1] = high;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the range
	 */
	public double[] getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(double[] range) {
		this.range = range;
	}

	/**
	 * @return the fuzzySet
	 */
	public LinkedList<Shape> getFuzzySets() {
		return fuzzySets;
	}

	/**
	 * @param fuzzySet the fuzzySet to set
	 */
	public void setFuzzySets(LinkedList<Shape> fuzzySets) {
		this.fuzzySets = fuzzySets;
	}

	/**
	 * @return the crispVal
	 */
	public double getCrispVal() {
		return crispVal;
	}

	/**
	 * @param crispVal the crispVal to set
	 */
	public void setCrispVal(double crispVal) {
		this.crispVal = crispVal;
	}

	/**
	 * 
	 * @return true if the variable's range is vaild, returns false otherwise
	 */
	public Boolean isValidVariable() {
		if (range[0] < 0 || range[1] < 0 || range[1] <= range[0]) {
			return false;
		} else {
			return true;
		}
	}
}

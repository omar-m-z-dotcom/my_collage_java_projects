package shapes;

import java.util.Iterator;
import java.util.LinkedList;

import variables.Variable;

public abstract class Shape {

	protected Variable variable;
	protected String name;
	protected double[] points;
	protected ShapeType type;
	protected double membership = 0;
	protected LinkedList<Double> inferanceResults = new LinkedList<Double>();

	/**
	 * @param variable
	 * @param name
	 */
	public Shape(Variable variable, String name) {
		this.variable = variable;
		this.name = name;
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
	 * @return the points
	 */
	public double[] getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(double[] points) {
		this.points = points;
	}

	/**
	 * @return the type
	 */
	public ShapeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ShapeType type) {
		this.type = type;
	}

	/**
	 * @return the membership
	 */
	public double getMembership() {
		return membership;
	}

	/**
	 * @param membership the membership to set
	 */
	public void setMembership(double membership) {
		this.membership = membership;
	}

	/**
	 * @return the variable
	 */
	protected Variable getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	protected void setVariable(Variable variable) {
		this.variable = variable;
	}

	/**
	 * 
	 * @return true if the set is within the variable range, returns false otherwise
	 */
	protected Boolean isWithinVariableRange() {
		for (int i = 0; i < points.length; i++) {
			if (points[i] < variable.getRange()[0] || points[i] > variable.getRange()[1]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * adds a related to set rule result to the list rule results
	 * 
	 * @param result
	 */
	public void addInferanceResult(double result) {
		inferanceResults.add(result);
	}

	/**
	 * assigns the largest inferance rule result related to the set to the
	 * membership of the set<br>
	 * note: used by OutVariables
	 */
	public void applyMaxResult() {
		double maxResult = 0;
		for (Iterator<Double> iterator = inferanceResults.iterator(); iterator.hasNext();) {
			double double1 = (double) iterator.next();
			if (maxResult < double1) {
				maxResult = double1;
			}
		}
		membership = maxResult;
	}

	/**
	 * 
	 * @param x
	 * @return the y-axis value of a point that is not a shape vertex
	 */
	protected abstract double getYPoint(double x);

	/**
	 * 
	 * @param x
	 * calculates the set membership given crisp value x<br>
	 * note: used by InVariables
	 */
	public void calcMembership(double x) {
		membership = getYPoint(x);
	}

	/**
	 * 
	 * @return the signed area of any non-self-intersecting closed polygon
	 */
	protected abstract double getSignedArea();

	/**
	 * 
	 * @return the x-axis centroid of any non-self-intersecting closed polygon
	 */
	public abstract double getCentroid();

	/**
	 * 
	 * @return true if the set is within it's variable range and the shape of the
	 *         set is valid, returns false otherwise
	 */
	public abstract Boolean isValidSet();

}

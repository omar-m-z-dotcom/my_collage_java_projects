package variables;

import java.util.Iterator;

import shapes.Shape;

public class OutVariable extends Variable {

	public OutVariable(String name, double low, double high) {
		super(name, low, high);
	}

	public void deFuzzification() {
		double wightedAverageMean = 0.0;
		double membershipSum = 0.0;
		for (Iterator<Shape> iterator = fuzzySets.iterator(); iterator.hasNext();) {
			Shape shape = iterator.next();
			wightedAverageMean += shape.getMembership() * shape.getCentroid();
		}
		for (Iterator<Shape> iterator = fuzzySets.iterator(); iterator.hasNext();) {
			Shape shape = iterator.next();
			membershipSum += shape.getMembership();
		}
		crispVal = (wightedAverageMean / membershipSum);
	}

	/**
	 * 
	 * @return the name of the highest membership set
	 */
	public String highestMembership() {
		double highestMembership = -1.0;
		String set = "";
		for (Iterator<Shape> iterator = fuzzySets.iterator(); iterator.hasNext();) {
			Shape shape = iterator.next();
			shape.calcMembership(crispVal);
			if (highestMembership <= shape.getMembership()) {
				highestMembership = shape.getMembership();
				set = shape.getName();
			}
		}
		return set;
	}
}

package variables;

import java.util.Iterator;

import shapes.Shape;

public class InVariable extends Variable {

	public InVariable(String name, double low, double high) {
		super(name, low, high);
	}

	public void fuzzification() {
		for (Iterator<Shape> iterator = fuzzySets.iterator(); iterator.hasNext();) {
			//shape =fuzzy set
			Shape shape = iterator.next();
			shape.calcMembership(crispVal);
		}
	}
}

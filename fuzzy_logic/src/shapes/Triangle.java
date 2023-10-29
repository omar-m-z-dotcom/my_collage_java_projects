package shapes;

import variables.Variable;

public class Triangle extends Shape {

	public Triangle(Variable variable, String name, double low1, double high, double low2) {
		super(variable, name);
		points = new double[3];
		points[0] = low1;
		points[1] = high;
		points[2] = low2;
		if (points[0] == points[1]) {
			type = ShapeType.LATRI;
		} else if (points[1] == points[2]) {
			type = ShapeType.RATRI;
		} else {
			type = ShapeType.NTRI;
		}
	}

	@Override
	protected double getYPoint(double x) {
		if (type == ShapeType.RATRI) {
			if (x <= points[0] || x > points[1]) {
				return 0.0;
			} else if (x == points[1]) {
				return 1.0;
			} else {
				double slope = (1 / (points[1] - points[0]));
				double c = -(slope * points[0]);
				return ((slope * x) + c);
			}
		} else if (type == ShapeType.LATRI) {
			if (x < points[1] || x >= points[2]) {
				return 0.0;
			} else if (x == points[1]) {
				return 1.0;
			} else {
				double slope = (-1 / (points[2] - points[1]));
				double c = -(slope * points[2]);
				return ((slope * x) + c);
			}
		} else {//normal triangle
			if (x > points[0] && x < points[1]) {
				double slope = (1 / (points[1] - points[0]));
				double c = -(slope * points[0]);
				return ((slope * x) + c);
			} else if (x > points[1] && x < points[2]) {
				double slope = (-1 / (points[2] - points[1]));
				double c = -(slope * points[2]);
				return ((slope * x) + c);
			} else if (x == points[1]) {
				return 1.0;
			} else {
				return 0.0;
			}
		}
	}

	@Override
	//cheaks if the traingle is valid
	public Boolean isValidSet() {
		if ((points[0] == points[1] && points[1] < points[2]) || (points[0] < points[1] && points[1] == points[2])
				|| (points[0] < points[1] && points[1] < points[2])) {
			if (isWithinVariableRange()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected double getSignedArea() {
		int n = points.length + 1;
		double[] xPoints = new double[n];
		double[] yPoints = new double[n];
		for (int i = 0; i < n; i++) {
			if (i == 1) {
				xPoints[i] = points[i];
				yPoints[i] = 1;
			} else if (i == (n - 1)) {
				xPoints[i] = points[0];
				yPoints[i] = 0;
			} else {
				xPoints[i] = points[i];
				yPoints[i] = 0;
			}
		}
		double area = 0.0;
		for (int i = 0; i < (n - 1); i++) {
			area += ((xPoints[i] * yPoints[i + 1]) - (xPoints[i + 1] * yPoints[i]));
		}
		area /= 2;
		return Math.abs(area);
	}

	@Override
	public double getCentroid() {
		int n = points.length + 1;
		double[] xPoints = new double[n];
		double[] yPoints = new double[n];
		for (int i = 0; i < n; i++) {
			if (i == 1) {
				xPoints[i] = points[i];
				yPoints[i] = 1;
			} else if (i == (n - 1)) {
				xPoints[i] = points[0];
				yPoints[i] = 0;
			} else {
				xPoints[i] = points[i];
				yPoints[i] = 0;
			}
		}
		double area = getSignedArea();
		double centroid = 0.0;
		for (int i = 0; i < (n - 1); i++) {
			centroid += ((xPoints[i] + xPoints[i + 1])
					* ((xPoints[i] * yPoints[i + 1]) - (xPoints[i + 1] * yPoints[i])));
		}
		centroid *= (1 / (6 * area));
		return Math.abs(centroid);
	}

}

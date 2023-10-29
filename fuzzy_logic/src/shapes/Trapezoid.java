package shapes;

import variables.Variable;

public class Trapezoid extends Shape {

	public Trapezoid(Variable variable, String name, double low1, double high1, double high2, double low2) {
		super(variable, name);
		points = new double[4];
		points[0] = low1;
		points[1] = high1;
		points[2] = high2;
		points[3] = low2;
		if (points[0] == points[1] && points[3] == points[2]) {
			type = ShapeType.EPS_QUAD;
		} else if (points[0] == points[1]) {
			type = ShapeType.LATRAP;
		} else if (points[3] == points[2]) {
			type = ShapeType.RATRAP;
		} else {
			type = ShapeType.NTRAP;
		}

	}

	@Override
	protected double getYPoint(double x) {
		if (type == ShapeType.RATRAP) {
			if (x > points[2] || x <= points[0]) {
				return 0.0;
			} else if (x >= points[1] && x <= points[2]) {
				return 1.0;
			} else {
				double slope = (1 / (points[1] - points[0]));
				double c = -(slope * points[0]);
				return ((slope * x) + c);
			}
		} else if (type == ShapeType.LATRAP) {
			if (x < points[1] || x >= points[3]) {
				return 0.0;
			} else if (x >= points[1] && x <= points[2]) {
				return 1.0;
			} else {
				double slope = (-1 / (points[3] - points[2]));
				double c = -(slope * points[3]);
				return ((slope * x) + c);
			}
		} else if (type == ShapeType.NTRAP) {
			if (x > points[0] && x < points[1]) {
				double slope = (1 / (points[1] - points[0]));
				double c = -(slope * points[0]);
				return ((slope * x) + c);
			} else if (x > points[2] && x < points[3]) {
				double slope = (-1 / (points[3] - points[2]));
				double c = -(slope * points[3]);
				return ((slope * x) + c);
			} else if (x >= points[1] && x <= points[2]) {
				return 1.0;
			} else {
				return 0.0;
			}
		} else {
			if (x >= points[1] && x <= points[2]) {
				return 1.0;
			} else {
				return 0.0;
			}
		}
	}

	@Override
	public Boolean isValidSet() {
		if ((points[0] == points[1] && points[1] < points[2] && points[2] < points[3])
				|| (points[0] < points[1] && points[1] < points[2] && points[2] == points[3])
				|| (points[0] < points[1] && points[1] < points[2] && points[2] < points[3])
				|| (points[0] == points[1] && points[1] < points[2] && points[2] == points[3])) {
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
			if (i == 1 || i == 2) {
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
			if (i == 1 || i == 2) {
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

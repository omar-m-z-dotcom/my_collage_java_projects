package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import shapes.Shape;
import shapes.Triangle;
import shapes.Trapezoid;
import variables.Variable;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	Variable variable;
	LinkedList<Color> colors = new LinkedList<Color>();

	public DrawPanel(Variable variable) {
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(500, 500));
		this.variable = variable;
		for (Iterator<Shape> iterator = variable.getFuzzySets().iterator(); iterator.hasNext();) {
			Shape shape = iterator.next();
			colors.add(JColorChooser.showDialog(null,
					"choose a color for set " + shape.getName() + " in variable " + variable.getName(), Color.black));
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int colorIndex = 0;
		g2d.setStroke(new BasicStroke(2));
		double xAxisRatio = (this.getWidth() / variable.getRange()[1]);
		for (Iterator<Shape> iterator = variable.getFuzzySets().iterator(); iterator.hasNext();) {
			Shape shape = (Shape) iterator.next();
			if (shape instanceof Triangle) {
				int[] xPoints = new int[3];
				int[] yPoints = new int[] { 0, 1, 0 };
				for (int i = 0; i < xPoints.length; i++) {
					xPoints[i] = (int) (shape.getPoints()[i] * xAxisRatio);
					if (yPoints[i] == 0) {
						yPoints[i] = this.getHeight();
					} else {
						yPoints[i] = 0;
					}
				}
				g2d.setPaint(colors.get(colorIndex));
				g2d.drawPolygon(xPoints, yPoints, 3);
			} else if (shape instanceof Trapezoid) {
				int[] xPoints = new int[4];
				int[] yPoints = new int[] { 0, 1, 1, 0 };
				for (int i = 0; i < xPoints.length; i++) {
					xPoints[i] = (int) (shape.getPoints()[i] * xAxisRatio);
					if (yPoints[i] == 0) {
						yPoints[i] = this.getHeight();
					} else {
						yPoints[i] = 0;
					}
				}
				g2d.setPaint(colors.get(colorIndex));
				g2d.drawPolygon(xPoints, yPoints, 4);
			}
			colorIndex++;
		}
		g2d.setPaint(Color.black);
		g2d.drawLine((int) (variable.getCrispVal() * xAxisRatio), this.getHeight(),
				(int) (variable.getCrispVal() * xAxisRatio), 0);
	}
}

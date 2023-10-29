package gui;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import inference.InferenceEngine;
import variables.Variable;

public class GUI {
	public void getGUI(InferenceEngine engine) {
		LinkedList<JFrame> jFrames = new LinkedList<JFrame>();
		int i = 0;
		for (Iterator<Variable> iterator = engine.getVariables().iterator(); iterator.hasNext();) {
			Variable variable = iterator.next();
			jFrames.add(new JFrame(variable.getName()));
			jFrames.get(i).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			jFrames.get(i).setResizable(true);
			jFrames.get(i).add(new DrawPanel(variable));
			jFrames.get(i).pack();
			jFrames.get(i).setVisible(true);
			i++;
		}
	}
}

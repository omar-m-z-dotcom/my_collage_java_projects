import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			int num_of_test_cases;
			File f = new File("curve_fitting_input.txt");
			Scanner s = new Scanner(f);
			num_of_test_cases = s.nextInt();
			String output = "";
			for (int i = 0; i < num_of_test_cases; i++) {
				int numberOfPoints = s.nextInt();
				int sizeOfKromosom = s.nextInt() + 1;
				float[] xPoints = new float[numberOfPoints];
				float[] yPoints = new float[numberOfPoints];
				for (int j = 0; j < numberOfPoints; j++) {
					xPoints[j] = s.nextFloat();
					yPoints[j] = s.nextFloat();
				}
				GenaticAlgorithm g = new GenaticAlgorithm();
				kromosom[] popKromosoms = g.initializePopulation(sizeOfKromosom);
				for (int j = 0; j < popKromosoms.length; j++) {
					popKromosoms[j] = g.calculateFittness(popKromosoms[j], xPoints, yPoints);
				}
				popKromosoms = g.handleInvalides(popKromosoms, xPoints, yPoints);
				for (int iterationsNum = 0; iterationsNum < 1000; iterationsNum++) {
					if (iterationsNum != 0) {
						popKromosoms = g.handleInvalides(popKromosoms, xPoints, yPoints);
					}
					if (iterationsNum == 0) {
						popKromosoms = g.kInsertionSort(popKromosoms);
					}
					kromosom[] matingPool = g.tournamentSelection(popKromosoms);
					kromosom[] offSprings = g.twoPointCrossOver(matingPool);
					offSprings = g.nonUniformMutation(offSprings, xPoints, yPoints);
					popKromosoms = g.elitistReplacment(popKromosoms, offSprings);
				}
				kromosom finalSolution = popKromosoms[0];
				for (int j = 0; j < popKromosoms.length; j++) {
					if (finalSolution.fittnes > popKromosoms[j].fittnes) {
						finalSolution = popKromosoms[j];
					}
				}
				output += "equation" + (i + 1) + ":\n";
				output += "xDataSet= " + Arrays.toString(xPoints) + "\n";
				output += "yDataSet= " + Arrays.toString(yPoints) + "\n";
				output += finalSolution + "\n";
				output += "\n";
			}
			f = new File("out.txt");
			FileWriter fileWriter = new FileWriter(f);
			fileWriter.write(output);
			fileWriter.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

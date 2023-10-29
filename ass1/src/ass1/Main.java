package ass1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		try {
			int num_of_test_cases;

			File f = new File("knapsack_input.txt");
			Scanner s = new Scanner(f);
			num_of_test_cases = s.nextInt();
			for (int i = 0; i < num_of_test_cases; i++) {
				int size_of_knapsack = s.nextInt();
				int size_of_Kromosom = s.nextInt();
				int arr_of_wights[] = new int[size_of_Kromosom];
				int arr_of_vaules[] = new int[size_of_Kromosom];
				for (int j = 0; j < size_of_Kromosom; j++) {
					int wight = s.nextInt();
					int value = s.nextInt();
					arr_of_wights[j] = wight;
					arr_of_vaules[j] = value;
				}
				GenaticAlgorithm g = new GenaticAlgorithm();
				Kromosom[] popKromosoms = g.Population(size_of_Kromosom);
				popKromosoms = g.fittness(popKromosoms, arr_of_vaules, arr_of_wights, size_of_knapsack);
				popKromosoms = g.handleInvalides(popKromosoms, arr_of_vaules, arr_of_wights, size_of_knapsack); 	
				for (int iterationsNum = 0; iterationsNum < 1000; iterationsNum++) {
					if (iterationsNum != 0) {
						popKromosoms = g.handleInvalides(popKromosoms, arr_of_vaules, arr_of_wights, size_of_knapsack);
					}
					if (iterationsNum == 0) {
						popKromosoms = g.kInsertionSort(popKromosoms);
					}
					Kromosom[] matingPool = g.rankSelection(popKromosoms);
					Kromosom[] offSprings = g.crossOver(matingPool);
					for (int j = 0; j < popKromosoms.length; j++) {
						popKromosoms[j].isCrossedOver = false;
					}
					offSprings = g.mutation(offSprings);
					popKromosoms = g.replacment(popKromosoms, offSprings, arr_of_vaules, arr_of_wights,
							size_of_knapsack);

				}
				Kromosom finalSolution = popKromosoms[0];

				for (int j = 0; j < popKromosoms.length; j++) {
					if (finalSolution.fittnes < popKromosoms[j].fittnes) {
						finalSolution = popKromosoms[j];
					}
				}
				System.out.println(finalSolution);
			}
			s.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

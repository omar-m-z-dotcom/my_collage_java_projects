package ass1;

import java.util.Random;

public class GenaticAlgorithm {
	final int populationSize = 5;

	public Kromosom[] Population(int size_of_k) {
		Random rand = new Random();
		Kromosom[] indiveduals = new Kromosom[populationSize];
		for (int i = 0; i < indiveduals.length; i++) {
			indiveduals[i] = new Kromosom();
			indiveduals[i].gens = new int[size_of_k];
		}
		for (int i = 0; i < indiveduals.length; i++) {
			for (int j = 0; j < indiveduals[i].gens.length; j++) {
				indiveduals[i].gens[j] = rand.nextInt(2);
			}
		}
		return indiveduals;
	}

	public Kromosom[] fittness(Kromosom[] population, int[] vals, int[] wights, int size_of_knapsack) {
		for (int i = 0; i < population.length && population[i] != null; i++) {
			if (population[i].fittnes > 0) {
				population[i].fittnes = 0;
			}
			for (int j = 0; j < population[i].gens.length; j++) {
				population[i].fittnes += population[i].gens[j] * vals[j];
			}
			int KromosomWight = 0;
			for (int j = 0; j < population[i].gens.length; j++) {
				KromosomWight += population[i].gens[j] * wights[j];
			}
			if (KromosomWight > size_of_knapsack) {
				population[i].fittnes = 0;
			}
		}
		return population;
	}

	public Kromosom[] handleInvalides(Kromosom[] population, int[] vals, int[] wights, int size_of_knapsack) {
		Random rand = new Random();
		for (int i = 0; i < population.length; i++) {
			while (population[i].isDublicate(population, i) || population[i].fittnes == 0) {
				population[i] = new Kromosom();
				population[i].gens = new int[vals.length];
				for (int j = 0; j < population[i].gens.length; j++) {
					population[i].gens[j] = rand.nextInt(2);
				}
				for (int j = 0; j < population[i].gens.length; j++) {
					population[i].fittnes += population[i].gens[j] * vals[j];
				}
				int KromosomWight = 0;
				for (int j = 0; j < population[i].gens.length; j++) {
					KromosomWight += population[i].gens[j] * wights[j];
				}
				if (KromosomWight > size_of_knapsack) {
					population[i].fittnes = 0;
				}
			}
		}
		return population;
	}

	public Kromosom[] kInsertionSort(Kromosom[] populationToSort) {
		int i, j;
		Kromosom key;
		for (i = 1; i < populationToSort.length; i++) {
			key = populationToSort[i];
			j = i - 1;
			while (j >= 0 && populationToSort[j].fittnes > key.fittnes) {
				populationToSort[j + 1] = populationToSort[j];
				j = j - 1;
			}
			populationToSort[j + 1] = key;
		}
		return populationToSort;
	}

	public Kromosom[] rankSelection(Kromosom[] population) {
		Kromosom[] matingPool = new Kromosom[4];
		for (int i = 0, j = population.length - 1; i < matingPool.length; i++, j--) {
			matingPool[i] = population[j];
		}
		return matingPool;
	}

	public Kromosom[] crossOver(Kromosom[] matingPool) {
		Random rand = new Random();
		Kromosom[] offsprings = new Kromosom[populationSize];
		for (int i = 0; i < matingPool.length; i++) {
			boolean willCrossOver = rand.nextBoolean();
			Kromosom mate1 = null;
			Kromosom mate2 = null;

			if (willCrossOver && matingPool[i].isCrossedOver == false) {
				mate1 = matingPool[i];
				for (int j = 0; j < matingPool.length; j++) {
					if ((mate2 == null && mate1 != matingPool[j] && matingPool[j].isCrossedOver == false)
							|| (mate1 != matingPool[j] && matingPool[j].isCrossedOver == false
									&& mate2.fittnes < matingPool[j].fittnes)) {
						mate2 = matingPool[j];
					}
				}
			}
			if (mate1 == null || mate2 == null) {
				continue;
			}
			int crossOverPoint = rand.nextInt(mate1.gens.length - 1);
			Kromosom offSpring1 = new Kromosom();
			Kromosom offSpring2 = new Kromosom();
			offSpring1.gens = new int[mate1.gens.length];
			offSpring2.gens = new int[mate1.gens.length];
			for (int j = 0; j < mate1.gens.length; j++) {
				if (j <= crossOverPoint) {
					offSpring1.gens[j] = mate1.gens[j];
				} else {
					offSpring1.gens[j] = mate2.gens[j];
				}
			}
			for (int j = 0; j < mate1.gens.length; j++) {
				if (j <= crossOverPoint) {
					offSpring2.gens[j] = mate2.gens[j];
				} else {
					offSpring2.gens[j] = mate1.gens[j];
				}
			}
			for (int j = 0; j < offsprings.length; j++) {
				if (offsprings[j] == null) {
					offsprings[j] = offSpring1;
					break;
				}
			}
			for (int j = 0; j < offsprings.length; j++) {
				if (offsprings[j] == null) {
					offsprings[j] = offSpring2;
					break;
				}
			}
			mate1.isCrossedOver = true;
			mate2.isCrossedOver = true;
		}
		return offsprings;
	}

	public Kromosom[] mutation(Kromosom[] offsprings) {
		Random rand = new Random();
		for (int i = 0; i < offsprings.length && offsprings[i] != null; i++) {
			if (rand.nextBoolean() == true) {
				int flipedBit = rand.nextInt(offsprings[i].gens.length);
				if (offsprings[i].gens[flipedBit] == 1) {
					offsprings[i].gens[flipedBit] = 0;
				} else {
					offsprings[i].gens[flipedBit] = 1;
				}
			}
		}
		return offsprings;
	}

	public Kromosom[] replacment(Kromosom[] population, Kromosom[] offsprings, int[] vals, int[] wights,
			int size_of_knapsack) {
		offsprings = fittness(offsprings, vals, wights, size_of_knapsack);
		for (int i = 0, j = population.length - 1; i < offsprings.length; i++) {
			if (offsprings[i] == null || offsprings[i].fittnes == 0) {
				offsprings[i] = population[j];
				j--;
			}
		}
		offsprings = kInsertionSort(offsprings);
		return offsprings;
	}

}

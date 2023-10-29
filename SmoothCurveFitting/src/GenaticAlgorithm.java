import java.util.Random;

public class GenaticAlgorithm {
	final int populationSize = 12;

	public kromosom[] initializePopulation(int sizeOfKromosom) {
		Random rand = new Random();
		kromosom[] indiveduals = new kromosom[populationSize];
		for (int i = 0; i < indiveduals.length; i++) {
			indiveduals[i] = new kromosom();
			indiveduals[i].gens = new float[sizeOfKromosom];
		}
		for (int i = 0; i < indiveduals.length; i++) {
			for (int j = 0; j < indiveduals[i].gens.length; j++) {
				indiveduals[i].gens[j] = rand.nextFloat(-10, 10);
			}
		}
		return indiveduals;
	}

	public kromosom calculateFittness(kromosom individual, float[] xPoints, float[] yPoints) {
		if (individual.fittnes > 0) {
			individual.fittnes = 0;
		}
		for (int i = 0; i < xPoints.length; i++) {
			float actualY = 0, expectedY = yPoints[i];
			for (int j = 0, degree = individual.gens.length - 1; j < individual.gens.length; j++, degree--) {
				actualY += individual.gens[j] * Math.pow(xPoints[i], degree);
			}
			individual.fittnes += Math.pow((actualY - expectedY), 2);
		}
		individual.fittnes /= xPoints.length;
		return individual;
	}

	public kromosom[] handleInvalides(kromosom[] population, float[] xPoints, float[] yPoints) {
		Random rand = new Random();
		int K_size = population[0].gens.length;
		for (int i = 0; i < population.length; i++) {
			while (population[i].isDublicate(population, i)) {
				population[i] = new kromosom();
				population[i].gens = new float[K_size];
				for (int j = 0; j < population[i].gens.length; j++) {
					population[i].gens[j] = rand.nextFloat(-10, 10);
				}
				population[i] = calculateFittness(population[i], xPoints, yPoints);
			}
		}
		return population;
	}

	public kromosom[] kInsertionSort(kromosom[] populationToSort) {
		int i, j;
		kromosom key;
		for (i = 1; i < populationToSort.length; i++) {
			key = populationToSort[i];
			j = i - 1;
			while (j >= 0 && populationToSort[j].fittnes < key.fittnes) {
				populationToSort[j + 1] = populationToSort[j];
				j = j - 1;
			}
			populationToSort[j + 1] = key;
		}
		return populationToSort;
	}

	public kromosom[] tournamentSelection(kromosom[] population) {
		Random rand = new Random();
		kromosom[] matingPool = new kromosom[(populationSize / 2)];
		int k_size = population[0].gens.length;
		kromosom[] populationCopy = new kromosom[populationSize];
		for (int i = 0; i < populationCopy.length; i++) {
			populationCopy[i] = new kromosom();
			populationCopy[i].gens = new float[k_size];
		}
		for (int i = 0; i < populationCopy.length; i++) {
			populationCopy[i].fittnes = population[i].fittnes;
			for (int j = 0; j < populationCopy[i].gens.length; j++) {
				populationCopy[i].gens[j] = population[i].gens[j];
			}
		}
		for (int i = 0; i < matingPool.length; i++) {
			kromosom k1 = null, k2 = null;
			int kIndex1 = -1, kIndex2 = -1;
			while (k1 == k2 || k1 == null || k2 == null) {
				kIndex1 = rand.nextInt(populationSize);
				k1 = populationCopy[kIndex1];
				kIndex2 = rand.nextInt(populationSize);
				k2 = populationCopy[kIndex2];
			}
			if (k1.fittnes < k2.fittnes) {
				matingPool[i] = k1;
				populationCopy[kIndex1] = null;
			} else {
				matingPool[i] = k2;
				populationCopy[kIndex2] = null;
			}
		}
		return matingPool;
	}

	public kromosom[] twoPointCrossOver(kromosom[] matingPool) {
		Random rand = new Random();
		kromosom[] offSprings = new kromosom[populationSize];
		for (int i = 0; i < matingPool.length; i++) {
			boolean willCrossOver = rand.nextBoolean();
			kromosom mate1 = null;
			kromosom mate2 = null;
			if (willCrossOver && matingPool[i].isCrossedOver == false) {
				mate1 = matingPool[i];
				for (int j = 0; j < matingPool.length; j++) {
					if ((mate2 == null && mate1 != matingPool[j] && matingPool[j].isCrossedOver == false)
							|| (mate1 != matingPool[j] && matingPool[j].isCrossedOver == false
									&& mate2.fittnes > matingPool[j].fittnes)) {
						mate2 = matingPool[j];
					}
				}
			}
			if (mate1 == null || mate2 == null) {
				continue;
			}
			int[] crossOverPoints = new int[2];
			for (int j = 0; j < crossOverPoints.length; j++) {
				crossOverPoints[j] = rand.nextInt(mate1.gens.length);
			}
			int difference = crossOverPoints[0] - crossOverPoints[1];
			while (crossOverPoints[0] == crossOverPoints[1] || difference == 1 || difference == -1) {
				crossOverPoints[0] = rand.nextInt(mate1.gens.length);
				crossOverPoints[1] = rand.nextInt(mate1.gens.length);
				difference = crossOverPoints[0] - crossOverPoints[1];
			}
			if (crossOverPoints[0] > crossOverPoints[1]) {
				int temp = crossOverPoints[0];
				crossOverPoints[0] = crossOverPoints[1];
				crossOverPoints[1] = temp;
			}
			kromosom offSpring1 = new kromosom();
			kromosom offSpring2 = new kromosom();
			offSpring1.gens = new float[mate1.gens.length];
			offSpring2.gens = new float[mate1.gens.length];
			for (int j = 0; j < mate1.gens.length; j++) {
				if (j <= crossOverPoints[0] || j >= crossOverPoints[1]) {
					offSpring1.gens[j] = mate1.gens[j];
				} else {
					offSpring1.gens[j] = mate2.gens[j];
				}
			}
			for (int j = 0; j < mate1.gens.length; j++) {
				if (j <= crossOverPoints[0] || j >= crossOverPoints[1]) {
					offSpring2.gens[j] = mate2.gens[j];
				} else {
					offSpring2.gens[j] = mate1.gens[j];
				}
			}
			for (int j = 0; j < offSprings.length; j++) {
				if (offSprings[j] == null) {
					offSprings[j] = offSpring1;
					break;
				}
			}
			for (int j = 0; j < offSprings.length; j++) {
				if (offSprings[j] == null) {
					offSprings[j] = offSpring2;
					break;
				}
			}
			mate1.isCrossedOver = true;
			mate2.isCrossedOver = true;
		}
		return offSprings;
	}

	public kromosom[] nonUniformMutation(kromosom[] offSprings, float[] xPoints, float[] yPoints) {
		Random rand = new Random();
		for (int i = 0; i < offSprings.length && offSprings[i] != null; i++) {
			if (rand.nextBoolean() == true) {
				int mutatedGenIndex = rand.nextInt(offSprings[i].gens.length);
				offSprings[i].gens[mutatedGenIndex] = rand.nextFloat(-10, 10);
			}
		}
		for (int i = 0; i < offSprings.length && offSprings[i] != null; i++) {
			offSprings[i] = calculateFittness(offSprings[i], xPoints, yPoints);
		}
		return offSprings;
	}

	// offspring1 fittness > offfspring2 fittness
	// so we take offspring2 fittness because we want to make sure we have the least
	// fitness indviduals possiple for the problem
	public kromosom[] elitistReplacment(kromosom[] population, kromosom[] offsprings) {
		for (int i = 0, j = population.length - 1; i < offsprings.length; i++) {
			if (offsprings[i] == null) {
				offsprings[i] = population[j];
				j--;
			}
		}
		offsprings = kInsertionSort(offsprings);
		return offsprings;//new population
	}
}

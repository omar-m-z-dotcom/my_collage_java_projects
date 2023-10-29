package ass1;

import java.util.Arrays;

public class Kromosom {
	public int[] gens;
	public int fittnes = 0;
	public boolean isCrossedOver = false;

	public boolean isDublicate(Kromosom[] k, int index) {
		boolean[] isDublicateArray = new boolean[k.length];
		for (int i = 0; i < k.length; i++) {
			boolean isDublicate = true;
			if (index == i) {
				isDublicateArray[i] = false;
				continue;
			}
			for (int j = 0; j < gens.length; j++) {
				if (gens[j] != k[i].gens[j]) {
					isDublicate = false;
					break;
				}
			}
			isDublicateArray[i] = isDublicate;
		}
		for (int i = 0; i < isDublicateArray.length; i++) {
			if (isDublicateArray[i] == true) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Kromosom= [" + (gens != null ? "gens=" + Arrays.toString(gens) + ", " : "") + "fittnes=" + fittnes
				+ "]";
	}

}

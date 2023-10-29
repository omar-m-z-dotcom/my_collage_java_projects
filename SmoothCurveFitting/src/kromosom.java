
public class kromosom {
	public float[] gens;
	public float fittnes = 0;
	public boolean isCrossedOver = false;

	public boolean isDublicate(kromosom[] k, int index) {
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
		String polynomial = "polynomial= [";
		int coffNumber = gens.length - 1;
		for (int i = 0; i < gens.length; i++, coffNumber--) {
			polynomial += "c" + coffNumber + "=" + gens[i];
			if (i < gens.length - 1) {
				polynomial += ", ";
			} else {
				polynomial += "], MSE= " + fittnes;
			}
		}
		return polynomial;
	}
}

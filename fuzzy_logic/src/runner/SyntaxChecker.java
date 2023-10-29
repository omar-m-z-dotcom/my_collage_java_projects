package runner;

import java.util.Collections;
import java.util.LinkedList;

public class SyntaxChecker {

	public static LinkedList<String> cheakSyntax(PreSimRunPhases phase, String input, String[] charsToRemove,
			int[] expectedPartsNum) {
		if (phase == PreSimRunPhases.RULES_PHASE) {
			LinkedList<String> parts = new LinkedList<String>();
			Collections.addAll(parts, input.split(" "));
			while (parts.contains("")) {
				parts.remove("");
			}
			if (parts.size() >= expectedPartsNum[0] && parts.size() <= expectedPartsNum[1]
					&& parts.get((parts.size() - 3)).equalsIgnoreCase("=>")) {
				return parts;
			} else {
				return null;
			}
		} else if (phase == PreSimRunPhases.VARS_PHASE) {
			String testString = new String(input);
			int leftSquareBracketNum = 0;
			int rightSquareBracketNum = 0;
			int commasNum = 0;
			for (int i = 0; i < testString.length(); i++) {
				if (testString.charAt(i) == '[') {
					leftSquareBracketNum++;
				}
				if (testString.charAt(i) == ']') {
					rightSquareBracketNum++;
				}
				if (testString.charAt(i) == ',') {
					commasNum++;
				}
			}
			if (leftSquareBracketNum != 1 || rightSquareBracketNum != 1 || commasNum != 1) {
				return null;
			}
			for (int i = 0; i < charsToRemove.length; i++) {
				testString = testString.replaceFirst("\\" + charsToRemove[i], " ");
			}
			LinkedList<String> parts = new LinkedList<String>();
			Collections.addAll(parts, testString.split(" "));
			while (parts.contains("")) {
				parts.remove("");
			}
			if (parts.size() == expectedPartsNum[0]
					&& (parts.get(1).equalsIgnoreCase("in") || parts.get(1).equalsIgnoreCase("out"))) {
				return parts;
			} else {
				return null;
			}
		} else {
			LinkedList<String> parts = new LinkedList<String>();
			Collections.addAll(parts, input.split(" "));
			while (parts.contains("")) {
				parts.remove("");
			} // expectedPartsNum[0] = 5 => TRI, expectedPartsNum[1] = 6 => TRAP
			if ((parts.size() == expectedPartsNum[0] && (parts.get(1).equalsIgnoreCase("tri"))
					|| (parts.size() == expectedPartsNum[1] && parts.get(1).equalsIgnoreCase("trap")))) {
				return parts;
			} else {
				return null;
			}
		}
	}
}

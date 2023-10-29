package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * used to validate that the input entered by the user is valid
 *
 */
public class InputValidator {
	public static boolean isValid(String targetString, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(targetString);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
}

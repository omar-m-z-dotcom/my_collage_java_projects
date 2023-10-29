package final_task_answer_20196115;

public class TextEditor {
	private TextFormatter textFormatter = null;
	public TextEditor(TextFormatter textFormatter) {
		this.textFormatter = textFormatter;
	}
	public void formatText(String text) {
		if (textFormatter == null) {
			System.out.println("text unformatted: " + text);
		}
		if (textFormatter instanceof LowerCaseTextFormatter) {
			System.out.println("text formatted to lower case: " + textFormatter.format(text));
		}
		if (textFormatter instanceof UpperCaseTextFormater) {
			System.out.println("text formatted to upper case: " + textFormatter.format(text));
		}
	}
}

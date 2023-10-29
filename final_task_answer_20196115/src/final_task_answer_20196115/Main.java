package final_task_answer_20196115;

public class Main {

	public static void main(String[] args) {
		String testText = "omar MOHAMED";
		TextEditor textEditor = new TextEditor(null);
		textEditor.formatText(testText);
		textEditor = new TextEditor(new LowerCaseTextFormatter());
		textEditor.formatText(testText);
		textEditor = new TextEditor(new UpperCaseTextFormater());
		textEditor.formatText(testText);
	}

}

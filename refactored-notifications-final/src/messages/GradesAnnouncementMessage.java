package messages;

public class GradesAnnouncementMessage implements Message {

	@Override
	public String prepareMessage(String[] placeHolders) {
		/*
		 * placeHolders[0]: username, placeHolders[1]: task\exam, placeHolders[2]:
		 * task\exam name, placeHolders[3]: course name, placeHolders[4]: location in
		 * course menu
		 */
		return "Dear " + placeHolders[0] + ", The grades of " + placeHolders[1] + " " + placeHolders[2]
				+ " has been announced and you can find it at course " + placeHolders[3] + " in " + placeHolders[4];
	}

	public boolean verifyGrades() {
		// code to verify Grades before announcement

		return true;
	}
}

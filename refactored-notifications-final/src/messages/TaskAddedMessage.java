package messages;

public class TaskAddedMessage implements Message {

	@Override
	public String prepareMessage(String[] placeHolders) {
		/*
		 * placeHolders[0]: course content type, placeHolders[1]: course content name,
		 * placeHolders[2]: course name, placeHolders[3]: location in course menu,
		 * placeHolders[4]: submission date
		 */
		return "Dear all " + placeHolders[0] + " " + placeHolders[1] + " has been announced on course "
				+ placeHolders[2] + " in " + placeHolders[3] + " and submission of the answer of the " + placeHolders[0]
				+ " will be on due date: " + placeHolders[4];
	}

	public void addTeamDescription() {
		//code to add the team description to the announcement
	}
}
package messages;

public class DailyNewsMessage implements Message {

	@Override
	public String prepareMessage(String[] placeHolders) {
		/* placeHolders[0]: news head line, placeHolders[1]: news body */
		return placeHolders[0] + " :\n" + placeHolders[1];
	}

}
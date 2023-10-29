package messages;

public class MessageFactory {
	/**
	 * dummy private constructor to hide the implicit public one
	 */
	private MessageFactory() {
		// TODO Auto-generated constructor stub
	}

	public static Message generateMessage(MessageTypes type) {
		if (type == MessageTypes.DAILY_NEWS_MESSAGE) {
			return new DailyNewsMessage();
		}
		if (type == MessageTypes.GRADES_ANNOUNCEMENT_MESSAGE) {
			return new GradesAnnouncementMessage();
		}
		if (type == MessageTypes.TASK_ADDED_MESSAGE) {
			return new TaskAddedMessage();
		}
		return null;
	}
}
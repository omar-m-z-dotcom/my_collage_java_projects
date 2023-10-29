package gateways;

import course.Course;
import messages.Message;

public interface GateWay {
	public void sendMessage(Message message, String[] placeholders, Course course);
}
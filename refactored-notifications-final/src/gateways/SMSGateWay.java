package gateways;

import course.Course;
import messages.DailyNewsMessage;
import messages.GradesAnnouncementMessage;
import messages.Message;
import messages.TaskAddedMessage;
import users.Professor;
import users.Student;
import users.TA;

public class SMSGateWay implements GateWay {

	@Override
	public void sendMessage(Message message, String[] placeholders, Course course) {
		if (message instanceof DailyNewsMessage || message instanceof TaskAddedMessage) {
			String finalMessage = message.prepareMessage(placeholders);
			for (Professor professor : course.getProfessorsForSMSNotification()) {
				professor.notifyProfessor(finalMessage, professor.getPhoneNumber());
			}
			for (Student student : course.getStudentsForSMSNotification()) {
				student.notifyStudent(finalMessage, student.getPhoneNumber());
			}
			for (TA ta : course.getTAsForSMSNotification()) {
				ta.notifyTA(finalMessage, ta.getPhoneNumber());
			}
		} else if (message instanceof GradesAnnouncementMessage) {
			String finalMessage;
			for (Professor professor : course.getProfessorsForSMSNotification()) {
				placeholders[0] = professor.getName();
				finalMessage = message.prepareMessage(placeholders);
				professor.notifyProfessor(finalMessage, professor.getPhoneNumber());
			}
			for (Student student : course.getStudentsForSMSNotification()) {
				placeholders[0] = student.getName();
				finalMessage = message.prepareMessage(placeholders);
				student.notifyStudent(finalMessage, student.getPhoneNumber());
			}
			for (TA ta : course.getTAsForSMSNotification()) {
				placeholders[0] = ta.getName();
				finalMessage = message.prepareMessage(placeholders);
				ta.notifyTA(finalMessage, ta.getPhoneNumber());
			}
		}
	}

}
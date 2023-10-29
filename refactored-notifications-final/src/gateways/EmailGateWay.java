package gateways;

import course.Course;
import messages.DailyNewsMessage;
import messages.GradesAnnouncementMessage;
import messages.Message;
import messages.TaskAddedMessage;
import users.Professor;
import users.Student;
import users.TA;

public class EmailGateWay implements GateWay {

	@Override
	public void sendMessage(Message message, String[] placeholders, Course course) {
		if (message instanceof DailyNewsMessage || message instanceof TaskAddedMessage) {
			String finalMessage = message.prepareMessage(placeholders);
			for (Professor professor : course.getProfessorsForEmailNotification()) {
				professor.notifyProfessor(finalMessage, professor.getEmail());
			}
			for (Student student : course.getStudentsForEmailNotification()) {
				student.notifyStudent(finalMessage, student.getEmail());
			}
			for (TA ta : course.getTAsForEmailNotification()) {
				ta.notifyTA(finalMessage, ta.getEmail());
			}
		} else if (message instanceof GradesAnnouncementMessage) {
			String finalMessage;
			for (Professor professor : course.getProfessorsForEmailNotification()) {
				placeholders[0] = professor.getName();
				finalMessage = message.prepareMessage(placeholders);
				professor.notifyProfessor(finalMessage, professor.getEmail());
			}
			for (Student student : course.getStudentsForEmailNotification()) {
				placeholders[0] = student.getName();
				finalMessage = message.prepareMessage(placeholders);
				student.notifyStudent(finalMessage, student.getEmail());
			}
			for (TA ta : course.getTAsForEmailNotification()) {
				placeholders[0] = ta.getName();
				finalMessage = message.prepareMessage(placeholders);
				ta.notifyTA(finalMessage, ta.getEmail());
			}
		}

	}

}
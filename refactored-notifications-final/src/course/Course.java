package course;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import gateways.GateWay;
import gateways.GateWayFactory;
import gateways.GateWayTypes;
import messages.Message;
import messages.MessageFactory;
import messages.MessageTypes;
import users.*;

public class Course {
	private String name;
	private String code;
	private ArrayList<String> announcements;
	private ArrayList<String> exams;
	private ArrayList<String> grades;

	private ArrayList<Professor> professorsForEmailNotification;
	private ArrayList<Professor> professorsForSMSNotification;

	private ArrayList<TA> tAsForEmailNotification;
	private ArrayList<TA> tAsForSMSNotification;

	private ArrayList<Student> studentsForEmailNotification;
	private ArrayList<Student> studentsForSMSNotification;

	public Course(String name, String code) {
		super();
		this.name = name;
		this.code = code;

		announcements = new ArrayList<>();
		exams = new ArrayList<>();
		grades = new ArrayList<>();

		professorsForEmailNotification = new ArrayList<>();
		professorsForSMSNotification = new ArrayList<>();

		tAsForEmailNotification = new ArrayList<>();
		tAsForSMSNotification = new ArrayList<>();

		studentsForEmailNotification = new ArrayList<>();
		studentsForSMSNotification = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Professor> getProfessorsForEmailNotification() {
		return professorsForEmailNotification;
	}

	public List<Professor> getProfessorsForSMSNotification() {
		return professorsForSMSNotification;
	}

	public List<TA> getTAsForEmailNotification() {
		return tAsForEmailNotification;
	}

	public List<TA> getTAsForSMSNotification() {
		return tAsForSMSNotification;
	}

	public List<Student> getStudentsForEmailNotification() {
		return studentsForEmailNotification;
	}

	public List<Student> getStudentsForSMSNotification() {
		return studentsForSMSNotification;
	}

	public void subscribeProfessorForEmailNotification(Professor professor) {
		professorsForEmailNotification.add(professor);
	}

	public void subscribeProfessorForSMSNotification(Professor professor) {
		professorsForSMSNotification.add(professor);
	}

	public void subscribeTAForEmailNotification(TA ta) {
		tAsForEmailNotification.add(ta);
	}

	public void subscribeTAForSMSNotification(TA ta) {
		tAsForSMSNotification.add(ta);
	}

	public void subscribeStudentForEmailNotification(Student student) {
		studentsForEmailNotification.add(student);
	}

	public void subscribeStudentForSMSNotification(Student student) {
		studentsForSMSNotification.add(student);
	}
	
	public void unsubscribeProfessorForEmailNotification(Professor professor) {
		professorsForEmailNotification.remove(professor);
	}
	
	public void unsubscribeProfessorForSMSNotification(Professor professor) {
		professorsForSMSNotification.remove(professor);
	}
	
	public void unsubscribeTAForEmailNotification(TA ta) {
		tAsForEmailNotification.remove(ta);
	}
	
	public void unsubscribeTAForSMSNotification(TA ta) {
		tAsForSMSNotification.remove(ta);
	}
	
	public void unsubscribeStudentForEmailNotification(Student student) {
		studentsForEmailNotification.remove(student);
	}
	
	public void unsubscribeStudentForSMSNotification(Student student) {
		studentsForSMSNotification.remove(student);
	}

	public void addAssignment(String assignmentName, String locationInCourseMenu, Date submissionDate) {
		announcements.add(assignmentName);
		String[] placeholders = new String[] { CourseContentTypes.TASK.toString(), assignmentName, this.getName(),
				locationInCourseMenu, submissionDate.toString() };
		Message message = MessageFactory.generateMessage(MessageTypes.TASK_ADDED_MESSAGE);
		GateWay emailGateWay = GateWayFactory.generateGateWay(GateWayTypes.EMAIL_GATEWAY);
		GateWay smsGateWay = GateWayFactory.generateGateWay(GateWayTypes.SMS_GATEWAY);
		emailGateWay.sendMessage(message, placeholders, this);
		smsGateWay.sendMessage(message, placeholders, this);
	}

	public void addExam(String examName, String locationInCourseMenu, Date submissionDate) {
		announcements.add(examName);
		exams.add(examName);
		String[] placeholders = new String[] { CourseContentTypes.EXAM.toString(), examName, this.getName(),
				locationInCourseMenu, submissionDate.toString() };
		Message message = MessageFactory.generateMessage(MessageTypes.TASK_ADDED_MESSAGE);
		GateWay emailGateWay = GateWayFactory.generateGateWay(GateWayTypes.EMAIL_GATEWAY);
		GateWay smsGateWay = GateWayFactory.generateGateWay(GateWayTypes.SMS_GATEWAY);
		emailGateWay.sendMessage(message, placeholders, this);
		smsGateWay.sendMessage(message, placeholders, this);
	}

	public void postGrades(CourseContentTypes type, String courseContentName, String locationInCourseMenu,
			List<String> grades) {
		this.grades.addAll(grades);
		String[] placeholders = new String[] { "", type.toString(), courseContentName, this.getName(),
				locationInCourseMenu };
		Message message = MessageFactory.generateMessage(MessageTypes.GRADES_ANNOUNCEMENT_MESSAGE);
		GateWay emailGateWay = GateWayFactory.generateGateWay(GateWayTypes.EMAIL_GATEWAY);
		GateWay smsGateWay = GateWayFactory.generateGateWay(GateWayTypes.SMS_GATEWAY);
		emailGateWay.sendMessage(message, placeholders, this);
		smsGateWay.sendMessage(message, placeholders, this);
	}

	public void postAnnouncement(String announcementName, String announcementBody) {
		announcements.add(announcementName);
		String[] placeholders = new String[] { announcementName, announcementBody };
		Message message = MessageFactory.generateMessage(MessageTypes.DAILY_NEWS_MESSAGE);
		GateWay emailGateWay = GateWayFactory.generateGateWay(GateWayTypes.EMAIL_GATEWAY);
		GateWay smsGateWay = GateWayFactory.generateGateWay(GateWayTypes.SMS_GATEWAY);
		emailGateWay.sendMessage(message, placeholders, this);
		smsGateWay.sendMessage(message, placeholders, this);
	}
}
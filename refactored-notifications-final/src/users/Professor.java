package users;

import java.sql.Date;
import java.util.ArrayList;

public class Professor {
	private String name;
	private String department;
	private Date hirringDate;
	private String phDTopic;
	private String email;
	private String phoneNumber;
	private ArrayList<String> emailMessages;
	private ArrayList<String> mobileMessages;

	public Professor(String name, String department, Date hirringDate, String phDTopic, String email,
			String phoneNumber) {
		this.name = name;
		this.department = department;
		this.hirringDate = hirringDate;
		this.phDTopic = phDTopic;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.emailMessages = new ArrayList<>();
		this.mobileMessages = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getHirringDate() {
		return hirringDate;
	}

	public void setHirringDate(Date hirringDate) {
		this.hirringDate = hirringDate;
	}

	public String getPhDTopic() {
		return phDTopic;
	}

	public void setPhDTopic(String phDTopic) {
		this.phDTopic = phDTopic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/** Notifies the professor via email or SMS */
	public void notifyProfessor(String message, String userInfo) {
		if (userInfo.equalsIgnoreCase(email)) {
			emailMessages.add(message);
		}
		if (userInfo.equalsIgnoreCase(phoneNumber)) {
			mobileMessages.add(message);
		}
	}
}
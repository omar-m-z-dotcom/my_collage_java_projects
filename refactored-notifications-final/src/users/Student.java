package users;

import java.util.ArrayList;

public class Student {
	private String name;
	private int id;
	private String email;
	private String phoneNumber;
	private ArrayList<String> emailMessages;
	private ArrayList<String> mobileMessages;

	public Student(String name, int id, String email, String phoneNumber) {
		this.name = name;
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	/** Notifies the student via email or SMS */
	public void notifyStudent(String message, String userInfo) {
		if (userInfo.equalsIgnoreCase(email)) {
			emailMessages.add(message);
		}
		if (userInfo.equalsIgnoreCase(phoneNumber)) {
			mobileMessages.add(message);
		}
	}
}
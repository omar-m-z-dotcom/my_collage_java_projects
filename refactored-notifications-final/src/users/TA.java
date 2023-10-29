package users;

import java.util.ArrayList;

public class TA {
	private String name;
	private String department;
	private String email;
	private String phoneNumber;
	private ArrayList<String> emailMessages;
	private ArrayList<String> mobileMessages;

	public TA(String name, String department, String email, String phoneNumber) {
		this.name = name;
		this.department = department;
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

	/** Notifies the TA via email or SMS */
	public void notifyTA(String message, String userInfo) {
		if (userInfo.equalsIgnoreCase(email)) {
			emailMessages.add(message);
		}
		if (userInfo.equalsIgnoreCase(phoneNumber)) {
			mobileMessages.add(message);
		}
	}
}
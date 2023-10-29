package model;

import com.opencsv.bean.CsvBindByName;

public class User implements Comparable<User> {
	@CsvBindByName
	private String name;
	@CsvBindByName
	private String username;
	@CsvBindByName
	private String password;
	@CsvBindByName
	private boolean isActive;

	/**
	 * default constructor used by openCSV
	 */
	public User() {

	}

	/**
	 * @param name
	 * @param username
	 * @param password
	 * @param isActive
	 */
	public User(String name, String username, String password, boolean isActive) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.isActive = isActive;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int compareTo(User o) {
		if (username.equals(o.getUsername())) {
			return 1;
		}
		return 0;
	}

}

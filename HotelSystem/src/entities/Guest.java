package entities;

/**
 * stores the guest details
 */
public class Guest {
	private String name;
	private long nationalId;
	private int vistsNum;

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
	 * @return the nationalId
	 */
	public long getNationalId() {
		return nationalId;
	}

	/**
	 * @param nationalId the nationalId to set
	 */
	public void setNationalId(long nationalId) {
		this.nationalId = nationalId;
	}

	/**
	 * @return the vistsNum
	 */
	public int getVistsNum() {
		return vistsNum;
	}

	/**
	 * @param vistsNum the vistsNum to set
	 */
	public void setVistsNum(int vistsNum) {
		this.vistsNum = vistsNum;
	}

	/**
	 * returns a string representation of the guest
	 * 
	 * @return guest in string
	 */
	@Override
	public String toString() {
		return "Guest= [ " + (name != null ? "name=" + name + ", " : "") + "nationalId=" + nationalId + ", vistsNum="
				+ vistsNum + " ]";
	}

}

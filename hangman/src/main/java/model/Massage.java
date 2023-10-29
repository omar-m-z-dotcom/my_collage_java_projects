package model;

import com.opencsv.bean.CsvBindByName;

public class Massage implements Comparable<Massage> {
	@CsvBindByName
	private String sender;
	@CsvBindByName
	private String reciver;
	@CsvBindByName
	MassageType type;
	@CsvBindByName
	private String content;
	@CsvBindByName
	private boolean isRead;

	/**
	 * default constructor used by openCSV
	 */
	public Massage() {
	}

	/**
	 * @param sender
	 * @param reciver
	 * @param type
	 * @param content
	 * @param isRead
	 */
	public Massage(String sender, String reciver, MassageType type, String content, boolean isRead) {
		this.sender = sender;
		this.reciver = reciver;
		this.type = type;
		this.content = content;
		this.isRead = isRead;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the reciver
	 */
	public String getReciver() {
		return reciver;
	}

	/**
	 * @param reciver the reciver to set
	 */
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}

	/**
	 * @return the type
	 */
	public MassageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MassageType type) {
		this.type = type;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the isRead
	 */
	public boolean getIsRead() {
		return isRead;
	}

	/**
	 * @param isRead the isRead to set
	 */
	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public int compareTo(Massage o) {
		if ((type.equals(MassageType.JOIN_MASSAGE) && o.getType().equals(MassageType.JOIN_MASSAGE))
				|| (type.equals(MassageType.TEAM_STATUS_MASSAGE) && o.getType().equals(MassageType.TEAM_STATUS_MASSAGE))
				|| (type.equals(MassageType.CORRECT_GUESS_MASSAGE)
						&& o.getType().equals(MassageType.CORRECT_GUESS_MASSAGE))) {
			if (sender.equals(o.getSender()) && reciver.equals(o.getReciver()) && content.equals(o.getContent())) {
				return 1;
			}
		} else if (type.equals(MassageType.NEXT_TURN_MASSAGE) && o.getType().equals(MassageType.NEXT_TURN_MASSAGE)) {
			if (content.equals(o.getContent()) && sender.equals(o.getSender())) {
				return 1;
			}
		}
		return 0;
	}

}

package model;

import java.util.ArrayList;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

public class Team implements Comparable<Team> {
	@CsvBindByName
	private String name;
	@CsvBindByName
	private int size;
	@CsvBindAndSplitByName(elementType = String.class, splitOn = "-", writeDelimiter = "-", collectionType = ArrayList.class)
	private ArrayList<String> members;
	@CsvBindByName
	private long gameId;
	@CsvBindByName
	private StatusInGame status;

	/**
	 * default constructor used by openCSV
	 */
	public Team() {
	}

	/**
	 * @param name
	 * @param size
	 * @param members
	 * @param gameId
	 * @param status
	 */
	public Team(String name, int size, ArrayList<String> members, long gameId, StatusInGame status) {
		this.name = name;
		this.size = size;
		this.members = members;
		this.gameId = gameId;
		this.status = status;
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
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the members
	 */
	public ArrayList<String> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(ArrayList<String> members) {
		this.members = members;
	}

	/**
	 * @return the gameId
	 */
	public long getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the status
	 */
	public StatusInGame getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusInGame status) {
		this.status = status;
	}

	@Override
	public int compareTo(Team o) {
		if (gameId == o.getGameId() && name.equalsIgnoreCase(o.getName())) {
			return 1;
		}
		return 0;
	}

}

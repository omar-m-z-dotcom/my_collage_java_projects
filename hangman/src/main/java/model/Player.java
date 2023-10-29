package model;

import com.opencsv.bean.CsvBindByName;

public class Player implements Comparable<Player> {
	@CsvBindByName
	private String name;
	@CsvBindByName
	private long gameId;
	@CsvBindByName
	private int score;
	@CsvBindByName
	private int hangmanWordLength;
	@CsvBindByName
	private StatusInGame status;

	/**
	 * default constructor used by openCSV
	 */
	public Player() {
	}

	/**
	 * @param name
	 * @param gameId
	 * @param score
	 * @param hangmanWordLength
	 * @param status
	 */
	public Player(String name, long gameId, int score, int hangmanWordLength, StatusInGame status) {
		this.name = name;
		this.gameId = gameId;
		this.score = score;
		this.hangmanWordLength = hangmanWordLength;
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the hangmanWordLength
	 */
	public int getHangmanWordLength() {
		return hangmanWordLength;
	}

	/**
	 * @param hangmanWordLength the hangmanWordLength to set
	 */
	public void setHangmanWordLength(int hangmanWordLength) {
		this.hangmanWordLength = hangmanWordLength;
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
	public int compareTo(Player o) {
		if (name.equals(o.getName()) && gameId == o.getGameId()) {
			return 1;
		}
		return 0;
	}

}

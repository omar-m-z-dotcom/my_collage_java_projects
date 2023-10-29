package model;

import com.opencsv.bean.CsvBindByName;

public class Game implements Comparable<Game> {
	@CsvBindByName
	private long id;
	@CsvBindByName
	private GameType type;
	@CsvBindByName
	private int playersNum;
	@CsvBindByName
	private int teamsNum;
	@CsvBindByName
	private boolean hasBegun;
	@CsvBindByName
	private boolean isFinished;

	/**
	 * default constructor used by openCSV
	 */
	public Game() {
	}

	/**
	 * @param id
	 * @param type
	 * @param playersNum
	 * @param teamsNum
	 * @param isFinished
	 * @param hasBegun
	 */
	public Game(long id, GameType type, int playersNum, int teamsNum, boolean isFinished, boolean hasBegun) {
		this.id = id;
		this.type = type;
		this.playersNum = playersNum;
		this.teamsNum = teamsNum;
		this.hasBegun = hasBegun;
		this.isFinished = isFinished;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public GameType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(GameType type) {
		this.type = type;
	}

	/**
	 * @return the playersNum
	 */
	public int getPlayersNum() {
		return playersNum;
	}

	/**
	 * @param playersNum the playersNum to set
	 */
	public void setPlayersNum(int playersNum) {
		this.playersNum = playersNum;
	}

	/**
	 * @return the teamsNum
	 */
	public int getTeamsNum() {
		return teamsNum;
	}

	/**
	 * @param teamsNum the teamsNum to set
	 */
	public void setTeamsNum(int teamsNum) {
		this.teamsNum = teamsNum;
	}

	/**
	 * @return the hasBegun
	 */
	public boolean getHasBegun() {
		return hasBegun;
	}

	/**
	 * @param hasBegun the hasBegun to set
	 */
	public void setHasBegun(boolean hasBegun) {
		this.hasBegun = hasBegun;
	}

	/**
	 * @return the isFinished
	 */
	public boolean getIsFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished the isFinished to set
	 */
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	@Override
	public int compareTo(Game o) {
		if (id == o.getId()) {
			return 1;
		}
		return 0;
	}

}

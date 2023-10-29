package controllers;

import java.util.HashMap;

import types.RoomType;

/**
 * gets the requested hotel room factory
 *
 */
public class RoomFactoryGetter {
	/**
	 * 
	 * @param roomTypeNums stores the number of rooms of each type
	 * @return requested room factory
	 */
	public RoomFactory getRoomFactory(HashMap<RoomType, Integer> roomTypeNums) {
		if (roomTypeNums.get(RoomType.SUITE) != 0) {
			return new SuiteRoomFactory();
		}
		if (roomTypeNums.get(RoomType.KING_ROOM) != 0) {
			return new KingRoomFactory();
		}
		if (roomTypeNums.get(RoomType.TWIN_BED_ROOM) != 0) {
			return new TwinBedRoomFactory();
		}
		if (roomTypeNums.get(RoomType.SINGLE_ROOM) != 0) {
			return new SingleRoomFactory();
		}
		return null;
	}
}

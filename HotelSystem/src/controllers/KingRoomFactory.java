package controllers;

import java.util.HashMap;

import entities.Room;
import types.RoomType;
import types.RoomView;

/**
 * builds king rooms
 */
public class KingRoomFactory implements RoomFactory {

	@Override
	/**
	 * method that is used to create king rooms
	 * 
	 * @return king room
	 */
	public Room createRoom(int roomNum, HashMap<RoomType, Integer> roomTypeNums,
			HashMap<RoomView, Integer> roomViewNums) {
		if (roomViewNums.get(RoomView.SEA_VIEW) != 0) {
			Room requestedRoom = new Room();
			requestedRoom.setRoomNum(roomNum);
			requestedRoom.setRoomView(RoomView.SEA_VIEW);
			requestedRoom.setRoomType(RoomType.KING_ROOM);
			roomTypeNums.replace(RoomType.KING_ROOM, (roomTypeNums.get(RoomType.KING_ROOM)) - 1);
			roomViewNums.replace(RoomView.SEA_VIEW, (roomViewNums.get(RoomView.SEA_VIEW)) - 1);
			return requestedRoom;
		} else {
			Room requestedRoom = new Room();
			requestedRoom.setRoomNum(roomNum);
			requestedRoom.setRoomView(RoomView.GARDEN_VIEW);
			requestedRoom.setRoomType(RoomType.KING_ROOM);
			roomTypeNums.replace(RoomType.KING_ROOM, (roomTypeNums.get(RoomType.KING_ROOM)) - 1);
			roomViewNums.replace(RoomView.GARDEN_VIEW, (roomViewNums.get(RoomView.GARDEN_VIEW)) - 1);
			return requestedRoom;
		}
	}

}

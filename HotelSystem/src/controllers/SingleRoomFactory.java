package controllers;

import java.util.HashMap;

import entities.Room;
import types.RoomType;
import types.RoomView;

/**
 * builds single rooms
 *
 */
public class SingleRoomFactory implements RoomFactory {

	@Override
	/**
	 * method that is used to create single rooms
	 * 
	 * @return single room
	 */
	public Room createRoom(int roomNum, HashMap<RoomType, Integer> roomTypeNums,
			HashMap<RoomView, Integer> roomViewNums) {
		if (roomViewNums.get(RoomView.SEA_VIEW) != 0) {
			Room requestedRoom = new Room();
			requestedRoom.setRoomNum(roomNum);
			requestedRoom.setRoomView(RoomView.SEA_VIEW);
			requestedRoom.setRoomType(RoomType.SINGLE_ROOM);
			roomTypeNums.replace(RoomType.SINGLE_ROOM, (roomTypeNums.get(RoomType.SINGLE_ROOM)) - 1);
			roomViewNums.replace(RoomView.SEA_VIEW, (roomViewNums.get(RoomView.SEA_VIEW)) - 1);
			return requestedRoom;
		} else {
			Room requestedRoom = new Room();
			requestedRoom.setRoomNum(roomNum);
			requestedRoom.setRoomView(RoomView.GARDEN_VIEW);
			requestedRoom.setRoomType(RoomType.SINGLE_ROOM);
			roomTypeNums.replace(RoomType.SINGLE_ROOM, (roomTypeNums.get(RoomType.SINGLE_ROOM)) - 1);
			roomViewNums.replace(RoomView.GARDEN_VIEW, (roomViewNums.get(RoomView.GARDEN_VIEW)) - 1);
			return requestedRoom;
		}
	}

}

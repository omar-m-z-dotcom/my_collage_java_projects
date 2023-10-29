package controllers;

import java.util.HashMap;

import entities.Room;
import types.RoomType;
import types.RoomView;

/**
 * constructs rooms based on the room number and the remaining number of rooms
 * to be created of each type and view
 *
 */
public interface RoomFactory {
	public Room createRoom(int roomNum, HashMap<RoomType, Integer> roomTypeNums,
			HashMap<RoomView, Integer> roomViewNums);
}

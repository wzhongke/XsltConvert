package com.wang.createpattern.primary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王忠珂 on 2016/10/6.
 */
public class Maze {
    public Maze (){}
    public void addRoom(Room r){
        roomMap.put(r.getRoomNumber(), r);
    }
    public Room getRoom(int i){
        return roomMap.get(i);
    }

    private Map<Integer, Room> roomMap = new HashMap<>();
}

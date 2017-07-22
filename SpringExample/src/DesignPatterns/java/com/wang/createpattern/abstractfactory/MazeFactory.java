package com.wang.createpattern.abstractfactory;

import com.wang.createpattern.primary.Door;
import com.wang.createpattern.primary.Maze;
import com.wang.createpattern.primary.Room;
import com.wang.createpattern.primary.Wall;

/**
 * Created by 王忠珂 on 2016/10/6.
 */
public interface MazeFactory {
    Maze makeMaze();
    Wall makeWell();
    Room makeRoom();
    Door makeDoor(Room r1, Room r2);
}

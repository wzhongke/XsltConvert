package com.wang.createpattern.primary;

/**
 * Created by 王忠珂 on 2016/10/6.
 */
public class Room implements MapSite {

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public MapSite getSide(Direction direction){
        return sites[direction.getValue()];
    }
    public void setSide(Direction direction, MapSite mapSite){
        sites[direction.getValue()] = mapSite;
    }
    @Override
    public void enter() {

    }
    public int getRoomNumber(){
        return roomNumber;
    }
    private MapSite [] sites = new MapSite[4];
    private int roomNumber;

}

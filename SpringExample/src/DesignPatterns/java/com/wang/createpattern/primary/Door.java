package com.wang.createpattern.primary;

/**
 * Created by 王忠珂 on 2016/10/6.
 */
public class Door implements MapSite {
    public Door(Room r1, Room r2) {
        this.r1 = r1;
        this.r2 = r2;
    }
    @Override
    public void enter() {

    }

    public Room otherSideFrom(Room r) {
        return (r.getRoomNumber() == r1.getRoomNumber()?r1:r2);
    }

    private Room r1;
    private Room r2;
    private boolean isOpen;

}

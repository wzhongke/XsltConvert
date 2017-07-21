package com.wang.createpattern.primary;

/**
 * Created by 王忠珂 on 2016/10/7.
 */
public enum Direction{
    North(0), South(1), East(2), West(3);
    private final int value;
    Direction(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}

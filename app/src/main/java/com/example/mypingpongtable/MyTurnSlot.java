package com.example.mypingpongtable;

import android.graphics.Color;

import java.util.concurrent.TimeUnit;

public class MyTurnSlot {

    private int slotImage;
    private String turnTime;
    private String turnAgainst;
    private Game turnGame;

    MyTurnSlot(){

    }

    public MyTurnSlot(int slotImage, String turnTime, String turnAgainst, Game turnGame){
        this.slotImage = slotImage;
        this.turnTime = turnTime;
        this.turnAgainst = turnAgainst;
        this.turnGame = turnGame;
    }

    int getSlotImage(){
        return this.slotImage;
    }

    String getTurnTime(){
        return this.turnTime;
    }

    String getTurnAgainst(){
        return this.turnAgainst;
    }

    void setSlotImage(int slotImage){
        this.slotImage = slotImage;
    }

    void setTurnTime(String turnTime){
        this.turnTime= turnTime;
    }

    void setTurnAgainst(String turnAgainst){
        this.turnAgainst = turnAgainst;
    }



}
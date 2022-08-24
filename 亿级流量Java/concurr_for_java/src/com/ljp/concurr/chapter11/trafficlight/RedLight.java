package com.ljp.concurr.chapter11.trafficlight;

public class RedLight {
    public final String color = "red";
    private boolean offFlag = true;

    public boolean getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }
}

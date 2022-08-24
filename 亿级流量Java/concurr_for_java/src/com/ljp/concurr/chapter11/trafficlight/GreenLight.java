package com.ljp.concurr.chapter11.trafficlight;

public class GreenLight {
    public final String color = "green";

    private boolean offFlag = true;

    public boolean getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }
}

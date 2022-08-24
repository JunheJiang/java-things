package com.ljp.concurr.chapter11.trafficlight;

public class YellowLight {
    public final String color = "yellow";
    private boolean offFlag = true;

    public boolean getOffFlag() {
        return offFlag;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }
}

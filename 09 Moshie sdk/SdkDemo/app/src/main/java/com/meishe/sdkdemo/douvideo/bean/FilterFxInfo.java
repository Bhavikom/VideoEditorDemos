package com.meishe.sdkdemo.douvideo.bean;

public class FilterFxInfo {

    private String name;
    private long inPoint;
    private long outPoint;
    private boolean isAddSuccess;
//    private boolean isInReverseMode;

    public FilterFxInfo(){
        isAddSuccess = false;
    }

    public FilterFxInfo(String name,long inPoint,long outPoint) {
        this.name = name;
        this.inPoint = inPoint;
        this.outPoint = outPoint;
        isAddSuccess = false;
    }

//    public void setInReverseMode(boolean mode){
//        isInReverseMode = mode;
//    }
//
//    public boolean getInReverseMode(){
//        return isInReverseMode;
//    }

    public void setAddResult(boolean result){
        isAddSuccess = result;
    }

    public boolean getAddResult(){
        return isAddSuccess;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getInPoint() {
        return inPoint;
    }

    public void setInPoint(long inPoint) {
        this.inPoint = inPoint;
    }

    public long getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(long outPoint) {
        this.outPoint = outPoint;
    }

}



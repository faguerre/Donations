package com.donation.model.in;

import com.google.gson.annotations.SerializedName;

public class StateModelIn {


    @SerializedName("id")
    private int id;
    @SerializedName("actualState")
    private String actualState;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("finishDate")
    private String finishDate;
    @SerializedName("wasExtended")
    private boolean wasExtended;
    @SerializedName("daysLeft")
    private int DaysLeft;

    public int getDaysLeft() {
        return DaysLeft;
    }

    public StateModelIn() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActualState() {
        return actualState;
    }

    public void setActualState(String actualState) {
        this.actualState = actualState;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
//    public Date  getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(String  creationDate) {
////        try {
//            this.creationDate = new Date();// new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(creationDate);
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//
//    }
//
//    public Date  getFinishDate() {
//        return finishDate;
//    }
//
//    public void setFinishDate(String  finishDate) {
//        this.finishDate = new Date();
////        try {
////            this.finishDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(finishDate);
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//
//    }

    public boolean isWasExtended() {
        return wasExtended;
    }

    public void setWasExtended(boolean wasExtended) {
        this.wasExtended = wasExtended;
    }
}

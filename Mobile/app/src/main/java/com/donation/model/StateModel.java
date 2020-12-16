package com.donation.model;

import com.google.gson.annotations.SerializedName;

public class StateModel {

    @SerializedName("id")
    private int Id;
    @SerializedName("actualState")
    private String ActualState;
    @SerializedName("creationDate")
    private String CreationDate;
    @SerializedName("finishDate")
    private String FinishDate;
    @SerializedName("wasExtended")
    private boolean WasExtended;

    public StateModel() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getActualState() {
        return ActualState;
    }

    public void setActualState(String actualState) {
        ActualState = actualState;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getFinishDate() {
        return FinishDate;
    }

    public void setFinishDate(String finishDate) {
        FinishDate = finishDate;
    }

    public boolean isWasExtended() {
        return WasExtended;
    }

    public void setWasExtended(boolean wasExtended) {
        WasExtended = wasExtended;
    }
}

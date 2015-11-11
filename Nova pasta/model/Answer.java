package com.isel.ps.Find_My_Beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("Success")
    @Expose
    private boolean success;

    @SerializedName("CoorX")
    @Expose
    private double coorX;

    @SerializedName("CoorY")
    @Expose
    private double coorY;

    @SerializedName("RequestId")
    @Expose(serialize = false)
    private int requestId;

    private String message;

    public Answer(){

    }

    public Answer(boolean success, double coorX, double coorY) {
        this.success = success;
        this.coorX = coorX;
        this.coorY = coorY;
        buildMessage();
    }

    public Answer(String message, double coorX, double coorY) {
        this.message = message;
        this.coorX = coorX;
        this.coorY = coorY;
    }

    private void buildMessage() {
        if(success){
            message = "Found in " + coorX + ", " + coorY;
        }else {
            message = "Not found.";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getCoorY() {
        return coorY;
    }

    public void setCoorY(double coorY) {
        this.coorY = coorY;
    }

    public double getCoorX() {
        return coorX;
    }

    public void setCoorX(double coorX) {
        this.coorX = coorX;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}

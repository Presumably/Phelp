package com.isel.ps.Find_My_Beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequestReceived {

    @SerializedName("UUID")
    @Expose(serialize = false)
    private String uuid;
    @SerializedName("RequestId")
    @Expose(serialize = false)
    private int requestId;
    @SerializedName("Major")
    @Expose(serialize = false)
    private int major;
    @SerializedName("Minor")
    @Expose(serialize = false)
    private int minor;

    public String getUuid() {
        return uuid;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

}

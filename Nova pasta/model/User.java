package com.isel.ps.Find_My_Beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class User {

    @SerializedName("UserName")
    @Expose(deserialize = false)
    private String userName;
    @SerializedName("Password")
    @Expose(deserialize = false)
    private String password;
    @SerializedName("PushId")
    @Expose(deserialize = false)
    private String appToken;

    public User(String userName, String password, String appToken) {
        this.userName = userName;
        this.password = password;
        this.appToken = appToken;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}

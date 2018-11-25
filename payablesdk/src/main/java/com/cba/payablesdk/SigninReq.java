package com.cba.payablesdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 2/13/2017.
 */

public class SigninReq {

    @SerializedName("username")
    @Expose
    private String userName;

    @SerializedName("pwd")
    @Expose
    private String pwd;

    @SerializedName("developerKey")
    @Expose
    private String developerKey ;

    @SerializedName("developerToken")
    @Expose
    private String developerToken ;

    @SerializedName("deviceId")
    @Expose
    private String deviceId ;

    @SerializedName("simId")
    @Expose
    private String simId ;

    public String getDeveloperKey() {
        return developerKey;
    }

    public void setDeveloperKey(String developerKey) {
        this.developerKey = developerKey;
    }

    public String getDeveloperToken() {
        return developerToken;
    }

    public void setDeveloperToken(String developerToken) {
        this.developerToken = developerToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

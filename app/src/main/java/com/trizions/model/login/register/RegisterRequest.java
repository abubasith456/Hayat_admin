package com.trizions.model.login.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("eid")
    @Expose
    private String eid;
    @SerializedName("mno")
    @Expose
    private String mno;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

}

package com.hayatAdmin.model.clients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientsRequest {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("perPage")
    @Expose
    private String perPage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

}

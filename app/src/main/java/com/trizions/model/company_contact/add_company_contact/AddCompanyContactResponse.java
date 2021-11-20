package com.trizions.model.company_contact.add_company_contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCompanyContactResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

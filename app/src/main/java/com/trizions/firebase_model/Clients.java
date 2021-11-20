package com.trizions.firebase_model;

public class Clients {

    String clientsId, clientImage, clientName, clientRole, clientBusiness, clientMobileNumber, clientEmail, clientAddress;

    public Clients() {

    }

    public Clients(String clientsId, String clientImage, String clientName, String clientRole, String clientBusiness, String clientMobileNumber, String clientEmail, String clientAddress) {
        this.clientsId = clientsId;
        this.clientImage = clientImage;
        this.clientName = clientName;
        this.clientRole = clientRole;
        this.clientBusiness = clientBusiness;
        this.clientMobileNumber = clientMobileNumber;
        this.clientEmail = clientEmail;
        this.clientAddress = clientAddress;
    }

    public String getClientsId() {
        return clientsId;
    }

    public void setClientsId(String clientsId) {
        this.clientsId = clientsId;
    }

    public String getClientImage() {
        return clientImage;
    }

    public void setClientImage(String clientImage) {
        this.clientImage = clientImage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientRole() {
        return clientRole;
    }

    public void setClientRole(String clientRole) {
        this.clientRole = clientRole;
    }

    public String getClientBusiness() {
        return clientBusiness;
    }

    public void setClientBusiness(String clientBusiness) {
        this.clientBusiness = clientBusiness;
    }

    public String getClientMobileNumber() {
        return clientMobileNumber;
    }

    public void setClientMobileNumber(String clientMobileNumber) {
        this.clientMobileNumber = clientMobileNumber;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }
}

package com.grocery.firebase_model;

public class CurrentProjects {

    String projectId, projectImage, projectName, projectStatus, projectDetails, projectAddress;

    public CurrentProjects() {

    }

    public CurrentProjects(String projectId, String projectImage, String projectName, String projectStatus, String projectDetails, String projectAddress) {
        this.projectId = projectId;
        this.projectImage = projectImage;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.projectDetails = projectDetails;
        this.projectAddress = projectAddress;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }
}

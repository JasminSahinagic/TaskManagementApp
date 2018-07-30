package com.example.taskmanagementapp.taskmanagementapp.Model;

public class Task {

    private String title;
    private String description;
    private String image;
    private String dateTaskAdded;
    private String taskID;
    private String removeID;

    public Task() {
    }

    public Task(String title, String date, String desctription, String image,String dateTaskAdded, String taskID,String removeID) {
        this.title = title;
        this.description = desctription;
        this.image = image;
        this.dateTaskAdded=dateTaskAdded;
        this.taskID = taskID;
        this.removeID=removeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDateTaskAdded() {
        return dateTaskAdded;
    }

    public void setDateTaskAdded(String dateTaskAdded) {
        this.dateTaskAdded = dateTaskAdded;
    }

    public String getRemoveID() {
        return removeID;
    }

    public void setRemoveID(String removeID) {
        this.removeID = removeID;
    }
}

package com.gekn.productivityapp.models;

import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class ProjectModel {

    // TableView col id
    private final SimpleStringProperty idField = new SimpleStringProperty("id");
    // TableView col name
    private final SimpleStringProperty nameField = new SimpleStringProperty("name");

    private int id;
    private String name;
    private String desc;
    private long totalTime;
    private long lastTime;
    private String dateCreated;
    private String dateUpdated;
    private String dateFinished;
    private int status;

    public ProjectModel() {}

    public ProjectModel(String id, String name) {}

    public ProjectModel(ProjectBuilder builder) {
        this.idField.set(String.valueOf(builder.id));
        this.nameField.set(builder.name);
        this.id = builder.id;
        this.name = builder.name;
        this.desc = builder.desc;
        this.totalTime = builder.totalTime;
        this.lastTime = builder.lastTime;
        this.dateCreated = builder.dateCreated;
        this.dateUpdated = builder.dateUpdated;
        this.dateFinished = builder.dateFinished;
        this.status = builder.status;
    }

    public String getIdField() {
        return idField.get();
    }

    public void setIdField(String id) {
        this.idField.set(id);
    }

    public String getNameField() {
        return nameField.get();
    }

    public void setNameField(String name) {
        this.nameField.set(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProjectModel{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", desc = '" + desc + '\'' +
                ", totalTime = " + totalTime +
                ", lastTime = " + lastTime +
                ", dateCreated = " + dateCreated +
                ", dateUpdated = " + dateUpdated +
                ", dateFinished = " + dateFinished +
                ", status = " + status +
                '}';
    }

    public static class ProjectBuilder {

        private int id;
        private String name;
        private String desc;
        private long totalTime;
        private long lastTime;
        private String dateCreated;
        private String dateUpdated;
        private String dateFinished;
        private int status;

        public ProjectBuilder() {

        }

        public ProjectBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProjectBuilder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public ProjectBuilder totalTime(long time) {
            this.totalTime = time;
            return this;
        }

        public ProjectBuilder lastTime(long time) {
            this.lastTime = time;
            return this;
        }

        public ProjectBuilder dateCreated(String date) {
            this.dateCreated = date;
            return this;
        }

        public ProjectBuilder dateUpdated(String date) {
            this.dateUpdated = date;
            return this;
        }

        public ProjectBuilder dateFinished(String date) {
            this.dateFinished = date;
            return this;
        }

        public ProjectBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ProjectModel build() {
            return new ProjectModel(this);
        }

    }

}

package com.bbd.RPG.models.stompmessages;

public class StatusIn {
    public String name;
    public String level;
    public String status;

    public StatusIn(String name, String level, String status) {
        this.name = name;
        this.level = level;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

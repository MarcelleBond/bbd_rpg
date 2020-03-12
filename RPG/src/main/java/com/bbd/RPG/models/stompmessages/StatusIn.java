package com.bbd.RPG.models.stompmessages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusIn {
    public String name;
    public String level;
    public String status;

    public StatusIn(String name, String level, String status) {
        this.name = name;
        this.level = level;
        this.status = status;
    }

}

package com.bbd.RPG.models.stompmessages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializeMapIn {
    public Integer x;
    public Integer y;

    public InitializeMapIn(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}

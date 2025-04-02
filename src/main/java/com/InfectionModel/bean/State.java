package com.InfectionModel.bean;

public enum State {
    Light("轻症")
    , Heavy("重症")
    , Danger("病危")
    , Normal("正常")
    , Infected("感染")
    , Immune("免疫");

    private final String name;

    State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}

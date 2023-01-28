package com.crio.qcontest.entities;

public enum Level {
    LOW(50),MEDIUM(30),HIGH(0);

    private final int weight;
    private Level(int weight){
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
     } 
}

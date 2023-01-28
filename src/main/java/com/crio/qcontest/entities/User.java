package com.crio.qcontest.entities;

public class User {
    private final String name;
    private Integer totalScore;
    private final Long id;

    public User(String name, Long id) {
        this.name = name;
        this.id = id;
        this.totalScore = 100;
    }

    public User(String name) {
        this(name, null);
    }
    

    public String getName() {
        return name;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Long getId() {
        return id;
    }

    public void modifyScore(Integer score){
        if(score <0 ){
            throw new RuntimeException("invalid score");
 
        }
        this.totalScore = score;
    }

    @Override
    public String toString() {
        return "User [id=" + id + "]";
    }  
}

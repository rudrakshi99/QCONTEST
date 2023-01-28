package com.crio.qcontest.entities;

public class Question {
    private final String title;
    private final Level level;
    private final Integer score;
    private final Long id;

    public Question(String title, Level level, Integer score, Long id) {
        this.title = title;
        this.level = level;
        this.score = score;
        this.id = id;
    }

    public Question(String title, Level level, Integer score) {
        this(title, level, score, null);
    }
    
    public String getTitle() {
        return title;
    }

    public Level getLevel() {
        return level;
    }

    public Integer getScore() {
        return score;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + "]";
    }
}

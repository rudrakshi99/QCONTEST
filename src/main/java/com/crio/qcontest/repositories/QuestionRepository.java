package com.crio.qcontest.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;

public class QuestionRepository implements IQuestionRepository{
    private final Map<Long,Question> questionMap;
    private Long autoIncrement = 1L;

    public QuestionRepository(){
        questionMap = new HashMap<Long,Question>();
    }

    @Override
    public Question save(Question question) {
        // Create a new Question object with all the parameters with an unique ID.
        Question q = new Question(question.getTitle(),question.getLevel(),question.getScore(),autoIncrement);
        // Store the newly created question object to HashMap.
        questionMap.put(autoIncrement,q);
        // Increment the variable which will be used when next question is saved.
        ++autoIncrement;
        return q;
    }

    @Override
    public List<Question> findAll() {
        // Return all the users stored in HashMap.
        return questionMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Question> findById(Long id) {
        // Find an user for a given id stored in HashMap.
        return Optional.ofNullable(questionMap.get(id));
    }

    @Override
    public List<Question> findAllQuestionLevelWise(Level level) {
        // Find all the contests for a given level stored in HashMap.
        return questionMap.values().stream().filter(q -> q.getLevel() == level).collect(Collectors.toList());
    }

    @Override
    public Integer count() {
        // Get total number of questions stored in HashMap.
        return questionMap.size();
    }
}

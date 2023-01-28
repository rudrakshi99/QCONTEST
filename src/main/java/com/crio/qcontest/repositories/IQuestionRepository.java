package com.crio.qcontest.repositories;

import java.util.List;
import java.util.Optional;

import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;

public interface IQuestionRepository {
    Question save(Question question);
    List<Question> findAll();
    Optional<Question> findById(Long id);
    List<Question> findAllQuestionLevelWise(Level level);
    Integer count();
}

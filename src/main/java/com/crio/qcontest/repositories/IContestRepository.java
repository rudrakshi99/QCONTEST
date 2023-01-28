package com.crio.qcontest.repositories;

import java.util.List;
import java.util.Optional;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.Level;

public interface IContestRepository {
    Contest save(Contest question);
    List<Contest> findAll();
    Optional<Contest> findById(Long id);
    List<Contest> findAllContestLevelWise(Level level);    
}

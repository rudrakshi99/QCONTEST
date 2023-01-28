package com.crio.qcontest.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.Level;

public class ContestRepository implements IContestRepository{
    private final Map<Long,Contest> contestMap;
    private Long autoIncrement = 1L;

    public ContestRepository(){
        contestMap = new HashMap<Long,Contest>();
    }

    @Override
    public Contest save(Contest contest) {
            Contest q = new Contest(contest.getTitle(),contest.getLevel(),contest.getCreator(),contest.getQuestions(),autoIncrement);
            contestMap.put(autoIncrement,q);
            ++autoIncrement;
            return q;
    }

    @Override
    public List<Contest> findAll() {
        return contestMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Contest> findById(Long id) {
        return Optional.ofNullable(contestMap.get(id));
    }


    @Override
    public List<Contest> findAllContestLevelWise(Level level) {
         return contestMap.values().stream().filter(c -> c.getLevel() == level).collect(Collectors.toList());
    }    
}

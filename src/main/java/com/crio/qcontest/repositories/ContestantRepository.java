package com.crio.qcontest.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.Contestant;

public class ContestantRepository implements IContestantRepository {
    private final Map<String,Contestant> contestantMap;

    public ContestantRepository(){
        contestantMap = new HashMap<String,Contestant>();
    }

    @Override
    public Contestant save(Contestant contestant) {
        String contestantId = getContestantId(contestant);
        contestantMap.put(contestantId,contestant);
        return contestant;
    }

    @Override
    public Optional<Contestant> find(Long contestId, String userName) {
        // Find the Contestant for a given Contest id and userName from the HashMap.
        return contestantMap.values().stream().filter(c -> c.getContest().getId() == contestId && c.getUser().getName().equals(userName)).findFirst();
    }

    @Override
    public List<Contestant> findAllByContestId(Long contestId) {
        // Find all the Contestants registered for a given contest Id.
        return contestantMap.values().stream().filter(c -> c.getContest().getId() == contestId).collect(Collectors.toList());
    }

    @Override
    public void delete(Contestant contestant) {
        String contestantId = getContestantId(contestant);
        contestantMap.remove(contestantId);        
    }

    private String getContestantId(Contestant contestant){
        // Generate a new ID in this format:- "User[id] Contest[id]"
        // Above representation of ID makes a Contestant unique. 
        return new StringBuilder().append("User[").append(contestant.getUser().getId())
        .append("] Contest[").append(contestant.getContest().getId()).append("]").toString();
    }   
}
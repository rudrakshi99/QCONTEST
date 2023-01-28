package com.crio.qcontest.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.qcontest.entities.User;

public class UserRepository implements IUserRepository{
    private final Map<Long,User> userMap;
    private Long autoIncrement = 1L;

    public UserRepository(){
        userMap = new HashMap<Long,User>();
    }


    @Override
    public User save(User user) {
        User u = new User(user.getName(), autoIncrement);
        // Store the newly created question object to HashMap.
        userMap.put(autoIncrement,u);
        // Increment the variable which will be used when next question is saved.
        ++autoIncrement;
        return u;
    }


    @Override
    public List<User> findAll() {
     return userMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
    // Find an user which matches with the required name.
        return userMap.values().stream().filter(u -> u.getName().equals(name)).findFirst();
    }  
}

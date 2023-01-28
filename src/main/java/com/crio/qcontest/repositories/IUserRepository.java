package com.crio.qcontest.repositories;

import java.util.List;
import java.util.Optional;

import com.crio.qcontest.entities.User;

public interface IUserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByName(String name); 
}
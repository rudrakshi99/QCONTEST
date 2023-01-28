package com.crio.qcontest.services;


import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crio.qcontest.constants.UserOrder;
import com.crio.qcontest.entities.User;
import com.crio.qcontest.repositories.IUserRepository;
import com.crio.qcontest.repositories.UserRepository;

@DisplayName("UserService Tests")
public class UserServiceTests{

    // TODO: WARNING!!!
    //  DO NOT MODIFY ANY FILES IN THE TESTS/ ASSESSMENTS UNLESS ASKED TO.
    //  Any modifications in this file may result in Assessment failure!


    @Test
    public void createUser_WhenUserNameGiven_CreateUser(){
        // Arrange
        UserService userService = new UserService(new UserRepository());
        Long expectedId = 1L;
        String expectedName = "User 1";
        // Act
        User actual = userService.createUser(expectedName);
        // Assert
        Assertions.assertEquals(expectedId, actual.getId());
        Assertions.assertEquals(expectedName, actual.getName());
    }


    @Test
    public void getUsers_WhenUserOrderIsScoreASC_ReturnScoreASCWiseUserList(){
        // Arrange
        IUserRepository userRepository = new UserRepository();
        User user1 = userRepository.save(new User("User 1"));
        user1.modifyScore(80);
        User user2 = userRepository.save(new User("User 2"));
        user2.modifyScore(50);
        UserService userService = new UserService(userRepository);
        List<User> expected = List.of(user2,user1);
        // Act
        List<User> actual = userService.getUsers(UserOrder.SCORE_ASC);
        // Assert
        Assertions.assertIterableEquals(expected, actual);
    }


    @Test
    public void getUsers_WhenUserOrderIsScoreDESC_ReturnScoreDESCWiseUserList(){
        // Arrange
        IUserRepository userRepository = new UserRepository();
        User user1 = userRepository.save(new User("User 1"));
        user1.modifyScore(80);
        User user2 = userRepository.save(new User("User 2"));
        user2.modifyScore(90);
        UserService userService = new UserService(userRepository);
        List<User> expected = List.of(user2,user1);
        // Act
        List<User> actual = userService.getUsers(UserOrder.SCORE_DESC);
        // Assert
        Assertions.assertIterableEquals(expected, actual);
    }

}
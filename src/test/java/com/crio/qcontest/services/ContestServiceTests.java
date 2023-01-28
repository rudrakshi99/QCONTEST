package com.crio.qcontest.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crio.qcontest.entities.Contest;
import com.crio.qcontest.entities.Contestant;
import com.crio.qcontest.entities.Level;
import com.crio.qcontest.entities.Question;
import com.crio.qcontest.entities.User;
import com.crio.qcontest.repositories.ContestRepository;
import com.crio.qcontest.repositories.ContestantRepository;
import com.crio.qcontest.repositories.IContestRepository;
import com.crio.qcontest.repositories.IContestantRepository;
import com.crio.qcontest.repositories.IQuestionRepository;
import com.crio.qcontest.repositories.IUserRepository;
import com.crio.qcontest.repositories.QuestionRepository;
import com.crio.qcontest.repositories.UserRepository;

@DisplayName("ContestService Tests")
public class ContestServiceTests{

    // TODO: WARNING!!!
    //  DO NOT MODIFY ANY FILES IN THE TESTS/ ASSESSMENTS UNLESS ASKED TO.
    //  Any modifications in this file may result in Assessment failure!

    private IQuestionRepository questionRepository;
    private IUserRepository userRepository;
    private IContestRepository contestRepository;
    private IContestantRepository contestantRepository;
    private ContestService contestService;
    private Contest c1,c2,c3;
    private Contestant ca1,ca2,ca3;


    @BeforeEach
    public void setup(){
        questionRepository = new QuestionRepository();
        Question q1 = questionRepository.save(new Question("Question 1",Level.LOW,10));
        Question q2 = questionRepository.save(new Question("Question 2",Level.MEDIUM,20));
        Question q2a = questionRepository.save(new Question("Question 2a",Level.MEDIUM,30));
        Question q2b = questionRepository.save(new Question("Question 2b",Level.MEDIUM,40));
        Question q3 = questionRepository.save(new Question("Question 3",Level.HIGH,30));
        userRepository = new UserRepository();
        User u1 = userRepository.save(new User("User 1"));
        User u2 = userRepository.save(new User("User 2"));
        User u3 = userRepository.save(new User("User 3"));
        contestRepository = new ContestRepository();
        c1 = contestRepository.save(new Contest("Contest 1",Level.LOW,u1,List.of(q1)));
        c2 = contestRepository.save(new Contest("Contest 2",Level.MEDIUM,u2,List.of(q2,q2a,q2b)));
        c3 = contestRepository.save(new Contest("Contest 3",Level.HIGH,u3,List.of(q3)));
        contestantRepository = new ContestantRepository();
        ca1 = contestantRepository.save(new Contestant(u1,c2));
        ca1.addQuestion(q2);
        ca2 = contestantRepository.save(new Contestant(u2,c2));
        ca2.addQuestion(q2);
        ca2.addQuestion(q2a);
        ca3 = contestantRepository.save(new Contestant(u3,c2));
        ca3.addQuestion(q2a);
        ca3.addQuestion(q2b);

        contestService = new ContestService(contestantRepository, contestRepository, questionRepository, userRepository);
    }

    @Test
    public void getContests_WhenLevelIsNull_ReturnAllContests(){
        // Arrange
        List<Contest> expected = List.of(c1,c2,c3);
        // Act
        List<Contest> actual = contestService.getContests(null);
        // Assert
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getContests_WhenLevelGiven_ReturnContestsForGivenLevel(){
        // Arrange
        List<Contest> expected = List.of(c1);
        // Act
        List<Contest> actual = contestService.getContests(Level.LOW);
        // Assert
        Assertions.assertIterableEquals(expected, actual);
    }


    @Test
    public void createContestant_WhenContestNotFound_ThrowRuntimeException(){
        // Arrange
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class,() -> contestService.createContestant(4L,"User 1"));
    }

    @Test
    public void createContestant_WhenUserNameNotFound_ThrowRuntimeException(){
        // Arrange
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class,() -> contestService.createContestant(3L,"User 4"));
    }

    @Test
    public void createContestant_WhenContestNotValid_ThrowRuntimeException(){
        // Arrange
        c3.endContest();
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class,() -> contestService.createContestant(3L,"User 1"));
    }

    @Test
    public void createContestant_WhenContestIdAndUserNameGiven_CreateContestant(){
        // Arrange
        String expectedUserName = "User 1";
        Long expectedContestId = 3L;
        // Act
        Contestant contestant =  contestService.createContestant(expectedContestId,expectedUserName);
        // Assert
        Assertions.assertEquals(expectedUserName, contestant.getUser().getName());
        Assertions.assertEquals(expectedContestId, contestant.getContest().getId());
    }


    @Test
    public void contestHistory_WhenContestNotFound_ThrowRuntimeException(){
        // Arrange
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class,() -> contestService.contestHistory(4L));
    }  

    @Test
    public void contestHistory_WhenContestNotEnded_ThrowRuntimeException(){
        // Arrange
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class,() -> contestService.contestHistory(2L));
    }

    @Test
    public void contestHistory_WhenContestEnded_ReturnContestants(){
        // Arrange
        List<Contestant> expected = List.of(ca3,ca2,ca1);
        c2.endContest();
        // Act
        List<Contestant> actual = contestService.contestHistory(2L);
        // Assert
        Assertions.assertIterableEquals(expected, actual);
    }
}
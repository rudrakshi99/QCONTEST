package com.crio.qcontest.entities;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Contest Tests")
public class ContestTests {
    
    // TODO: WARNING!!!
    //  DO NOT MODIFY ANY FILES IN THE TESTS/ ASSESSMENTS UNLESS ASKED TO.
    //  Any modifications in this file may result in Assessment failure!

    // https://methodpoet.com/unit-test-method-naming-convention/
    @Test
    public void Contest_WhenQuestionLevelMisMatch_ThrowRuntimeException(){
        // Arrange
        String title = "Crio.Do Launch'22";
        Level level = Level.LOW;
        User createdBy = new User("Crio Admin");
        List<Question> questions = List.of(
            new Question("Q1",Level.HIGH,10)
        );
        // Act and Assertl
        Assertions.assertThrows(RuntimeException.class, () -> new Contest(title, level, createdBy, questions));
    }

    @Test
    public void endContest_WhenContestIsEnded_EndContest(){
        // Arrange
        String title = "Crio.Do Launch'22";
        Level level = Level.LOW;
        User createdBy = new User("Crio Admin");
        List<Question> questions = List.of(
            new Question("Q1",level,10)
        );
        Contest contest = new Contest(title, level, createdBy, questions);

        // Act
        contest.endContest();

        // Assert
        Assertions.assertEquals(ContestStatus.ENDED, contest.getContestStatus());
    }
}

package com.crio.qcontest.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Tests")
public class UserTests {

    // TODO: WARNING!!!
    //  DO NOT MODIFY ANY FILES IN THE TESTS/ ASSESSMENTS UNLESS ASKED TO.
    //  Any modifications in this file may result in Assessment failure!

    // https://methodpoet.com/unit-test-method-naming-convention/
    @Test
    public void modifyScore_WhenScoreNegative_ThrowRunTimeException(){
        // Arrange
        User user = new User("Crio User 1");
        // Act and Assert
        Assertions.assertThrows(RuntimeException.class, ()-> user.modifyScore(-1));
    }

    @Test
    public void modifyScore_WhenScoreIsModified_ModifyScore(){
        // Arrange
        User user = new User("Crio User 1");
        // Act
        user.modifyScore(50);
        // Assert
        Assertions.assertEquals(50, user.getTotalScore());
    }
}

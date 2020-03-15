package com.cgm.project.commands.helper;

import com.cgm.project.commands.helpers.CommandTextValidationHelper;
import com.cgm.project.commands.pojos.Question;
import com.cgm.project.commands.exceptions.InvalidQuestionException;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CommandTextValidationHelperTest {

    @Test
    void saveCommand_missingQuestionSeparator_throwsValidationException() {

        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand("abc");
        });

        assertTrue(exception.getMessage().contains("Invalid question. Please use '?' to separate question from answer options"));
    }

    @Test
    void saveCommand_noAnswerOptionProvided_throwsValidationException() {

        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand("abc ? ");
        });

        assertTrue(exception.getMessage().contains("At least one answer is needed"));
    }

    @Test
    void saveCommand_questionLimitExceeded_throwsValidationException() {

        final String questionText = randomAlphabetic(256) + "? \" option1 \"";
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);
        });

        assertTrue(exception.getMessage().contains("Question string exceeds allowed length of characters"));
    }

    @Test
    void saveCommand_answerLimitExceeded_throwsValidationException() {

        final String questionText = "abc ? " + randomAlphabetic(256);
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);
        });

        assertTrue(exception.getMessage().contains("Answer string exceeds allowed length of characters"));
    }

    @Test
    void saveCommand_answerDelimiterNotPresent_throwsValidationException() {

        final String questionText = "abc ? option1";
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);
        });

        assertTrue(exception.getMessage().contains("Answer options not in proper format"));
    }

    @Test
    void saveCommand_insufficientAnswerDelimiters_throwsValidationException() {

        final String questionText = "abc ? \"option1\"  \"option2 ";
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);
        });

        assertTrue(exception.getMessage().contains("Answer options not in proper format"));
    }

    @Test
    void saveCommand_noBoundaryAnswerDelimiters_throwsValidationException() {

        final String questionText = "abc ? option1\"  \"option2\"  \"option3 ";
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);
        });

        assertTrue(exception.getMessage().contains("Answer options not in proper format"));
    }

    @Test
    void saveCommand_validString_returnJsonFormattedQuestion() throws InvalidQuestionException {

        final String questionText = "question ? \"option1\"  \"option2\"  \"option3\" ";
        final Question mockedQuestion = new Question("question", "[\"option1\",\"option2\",\"option3\"]");

        final Question question = CommandTextValidationHelper.validateAndBuildSaveCommand(questionText);

        assertThat(mockedQuestion, samePropertyValuesAs(question));
    }

    @Test
    void askCommand_missingQuestionSeparator_throwsValidationException() {
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAskCommand("abc");
        });

        assertTrue(exception.getMessage().contains("Question string must contain '?'"));
    }

    @Test
    void askCommand_withAnswerDelimiter_throwsValidationException() {
        final InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            CommandTextValidationHelper.validateAskCommand("abc ? \"option1\" \"option2\" ");
        });

        assertTrue(exception.getMessage().contains("Please use 'save' command to save questions"));
    }

    @Test
    void askCommand_withValidString_noException() {
        assertDoesNotThrow(() -> CommandTextValidationHelper.validateAskCommand("abc ?"));
    }

    }
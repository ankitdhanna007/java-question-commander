package com.cgm.project.commands;

import com.cgm.project.data.entities.QuestionEntity;
import com.cgm.project.data.repositories.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.cgm.project.commands.AskQuestionCommand.DEFAULT_ANSWER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
public class ShellCommandsIT {

    @Autowired
    private QuestionRepository questionRepository;

    private AskQuestionCommand askQuestionCommand;

    private SaveQuestionCommand saveQuestionCommand;

    @Before
    public void init() {
        askQuestionCommand = new AskQuestionCommand(questionRepository);
        saveQuestionCommand = new SaveQuestionCommand(questionRepository);
    }

    @Test
    public void ask_nonExistentQuestion_returnDefaultAnswer() {

        final String shellOutput = askQuestionCommand.ask("question ?");
        Assertions.assertSame(shellOutput, DEFAULT_ANSWER);
    }

    @Test
    public void ask_existentQuestion_returnValidAnswerString() {

        questionRepository.save(new QuestionEntity("question", "[\"option1\",\"option2\"]"));
        final String shellOutput = askQuestionCommand.ask("question ?");
        assertThat(shellOutput, samePropertyValuesAs("option1\noption2"));
    }

    @Test
    public void save_validQuestion_returnSuccessMessage() {

        final String questionText = "question ? \"option1\"  \"option2\"  \"option3\" ";

        final String shellOutput = saveQuestionCommand.save(questionText);

        Assertions.assertEquals(shellOutput, "Question saved");
        final Optional<QuestionEntity> optionalQuestionEntity = questionRepository.findByQuestion("question");
        Assertions.assertTrue(optionalQuestionEntity.isPresent());
        Assertions.assertEquals(optionalQuestionEntity.get().getQuestion(), "question");
        Assertions.assertEquals(optionalQuestionEntity.get().getAnswer(), "[\"option1\",\"option2\",\"option3\"]");
    }

    @Test
    public void save_existingQuestion_returnErrorMessage() {

        final String questionText = "question ? \"option1\"  \"option2\"  \"option3\" ";

        final String firstShellOutput = saveQuestionCommand.save(questionText);
        final String secondShellOutput = saveQuestionCommand.save(questionText);
        final String shellOutput = askQuestionCommand.ask("question ?");

        Assertions.assertEquals(firstShellOutput, "Question saved");
        Assertions.assertEquals(secondShellOutput, "Question already exists! Please enter a different question");
        assertThat(shellOutput, samePropertyValuesAs("option1\noption2\noption3"));
    }

}

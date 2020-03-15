package com.cgm.project.commands;

import com.cgm.project.commands.exceptions.InvalidQuestionException;
import com.cgm.project.commands.helpers.CommandTextValidationHelper;
import com.cgm.project.commands.pojos.Question;
import com.cgm.project.data.entities.QuestionEntity;
import com.cgm.project.data.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Optional;

@ShellComponent
class SaveQuestionCommand {

    private final QuestionRepository questionRepository;

    @Autowired
    SaveQuestionCommand(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @ShellMethod("Saves a new question.")
    String save(
            @ShellOption final String shellInputText
    ) {
        try {
            // validate and fetch question from database
            final Question questionDTO = CommandTextValidationHelper.validateAndBuildSaveCommand(shellInputText);

            // check if question already exists
            final Optional<QuestionEntity> optionalQuestion = questionRepository.findByQuestion(questionDTO.getQuestionText().replace("?","").trim());
            if(optionalQuestion.isPresent()) {
                return "Question already exists! Please enter a different question";
            }

            final QuestionEntity questionEntity = new QuestionEntity(questionDTO.getQuestionText(), questionDTO.getAnswerText());
            questionRepository.save(questionEntity);
            return "Question saved";

        } catch (final InvalidQuestionException exception) {
            return "Validation Error encountered: " + exception.getMessage();
        }
    }

}

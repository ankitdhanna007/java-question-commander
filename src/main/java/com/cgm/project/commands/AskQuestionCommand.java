package com.cgm.project.commands;

import com.cgm.project.commands.exceptions.InvalidQuestionException;
import com.cgm.project.commands.helpers.CommandTextValidationHelper;
import com.cgm.project.data.entities.QuestionEntity;
import com.cgm.project.data.repositories.QuestionRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Optional;

@ShellComponent
class AskQuestionCommand {

    static final String DEFAULT_ANSWER = "\"The answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\"";

    private final QuestionRepository questionRepository;

    @Autowired
    AskQuestionCommand(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @ShellMethod("Ask a saved question")
    String ask(
            @ShellOption String shellInputText
    ) {
        try{
            CommandTextValidationHelper.validateAskCommand(shellInputText);
        } catch (final InvalidQuestionException exception) {
            return "Validation Error encountered: " + exception.getMessage();
        }

        final Optional<QuestionEntity> optionalQuestion = questionRepository.findByQuestion(shellInputText.replace("?","").trim());
        if(!optionalQuestion.isPresent()) {
            return DEFAULT_ANSWER;
        }

        // if question found, json decode the string into options in new lines
        final String[] answerOptions = new Gson().fromJson(optionalQuestion.get().getAnswer(), String[].class);
        final StringBuilder commandResponse = new StringBuilder();
        for (String answerOption : answerOptions) {
            commandResponse.append(answerOption).append("\n");
        }
        return commandResponse.substring(0, commandResponse.length() -1);
    }

}
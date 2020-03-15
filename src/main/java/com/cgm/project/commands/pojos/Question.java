package com.cgm.project.commands.pojos;

public class Question {

    private String questionText;
    private String answerText;

    public Question(final String questionText, final String answerText) {
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Question setQuestionText(final String questionText) {
        this.questionText = questionText;
        return this;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Question setAnswerText(final String answerText) {
        this.answerText = answerText;
        return this;
    }
}

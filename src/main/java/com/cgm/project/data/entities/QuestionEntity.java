package com.cgm.project.data.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "questions", uniqueConstraints = {@UniqueConstraint(columnNames = "question", name = "uniqueQuestionConstraint")})
public class QuestionEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "question")
    @Size(max = 255, message = "Question length should be not greater than 255")
    private String question;

    @Column(name = "answer")
    @Size(max = 255, message = "Answer length should be not greater than 255")
    private String answer;

    public QuestionEntity() {
    }

    public QuestionEntity(final String question, final String answer) {
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public QuestionEntity setId(final int id) {
        this.id = id;
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public QuestionEntity setQuestion(final String question) {
        this.question = question;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public QuestionEntity setAnswer(final String answer) {
        this.answer = answer;
        return this;
    }
}
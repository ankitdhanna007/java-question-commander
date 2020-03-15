package com.cgm.project.commands.helpers;

import com.cgm.project.commands.pojos.Question;
import com.cgm.project.commands.exceptions.InvalidQuestionException;
import com.google.gson.Gson;

public class CommandTextValidationHelper {

    /**
     * validates the question string used in the save command
     * @param questionText
     * @return Question pojo which contains question text and json converted answer string
     * @throws InvalidQuestionException
     */
    public static Question validateAndBuildSaveCommand(final String questionText) throws InvalidQuestionException {

        if(!questionText.contains("?")) {
            throw new InvalidQuestionException("Invalid question. Please use '?' to separate question from answer options");
        }

        final String[] questionParts = questionText.trim().split("\\?");
        if (questionParts.length == 1) { // no answer is present
            throw new InvalidQuestionException("At least one answer is needed");
        }

        final String questionString = questionParts[0].trim();
        final String answerString = questionParts[1].trim();

        //PS: 255 max characters validation can be caught by SQL constraints as well but we want to save IO
        if (questionString.length() > 255) {
            throw new InvalidQuestionException("Question string exceeds allowed length of characters");
        }

        if (answerString.length() > 255) {
            throw new InvalidQuestionException("Answer string exceeds allowed length of characters");
        }

        // Delimiter must be present in the beginning and end, cannot exist in odd no of sequences
        final int answerDelimiterCount = countSubstring("\"", answerString);
        if (answerDelimiterCount == 0 || (answerDelimiterCount % 2 != 0) || !answerString.matches("^\\\".*\\\"$")) {
            throw new InvalidQuestionException("Answer options not in proper format");
        }

        // split by spaces(multiple) between option words
        final String[] answerParts = answerString.substring(1, answerString.length() - 1).split("(\\\")\\s+(\\\")");

        return new Question(questionString, new Gson().toJson(answerParts));
    }

    /**
     * validates the question string for ask command
     * @param questionText
     * @throws InvalidQuestionException
     */
    public static void validateAskCommand(final String questionText) throws InvalidQuestionException {

        if(!questionText.contains("?")) {
            throw new InvalidQuestionException("Question string must contain '?' ");
        }

        if(questionText.trim().split("\\?").length > 1) {
            throw new InvalidQuestionException("Please use 'save' command to save questions");
        }
    }

        private static int countSubstring(String subStr, String str) {
        return (str.length() - str.replace(subStr, "").length()) / subStr.length();
    }
}

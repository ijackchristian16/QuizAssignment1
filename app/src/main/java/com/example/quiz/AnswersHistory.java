package com.example.quiz;

import java.util.List;

public class AnswersHistory {
    private List<UserAnswer> answer;

    public AnswersHistory(List<UserAnswer> answer) {
        this.answer = answer;
    }

    public List<UserAnswer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<UserAnswer> answer) {
        this.answer = answer;
    }
}

package com.example.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private static final String PREFS_NAME = "MyQuizApp";
    private static final String KEY_MY_OBJECT = "user_answers";
    private static final String KEY_NUM_OF_QUESTIONS = "num_of_questions";

    private static int numOfQuestion = 10;
    private static List<QuestionModel> listOfQuestion = new ArrayList<>();

    public  static QuestionBank INSTANCE = new QuestionBank();
    public static void setQuestions(List<QuestionModel> list) {
        listOfQuestion = list;
    }

    public static List<QuestionModel> getQuestions() {
        List<QuestionModel> shuffledQuestions = new ArrayList<>(listOfQuestion);
        Collections.shuffle(shuffledQuestions);
        return shuffledQuestions.subList(0, numOfQuestion);
    }

    public static void saveUserAnswers(Context context, List<UserAnswer> userAnswer) {
        AnswersHistory previousAnswers = getSavedUserAnswers(context);
        List<UserAnswer> userAnswers = new ArrayList<>();
        if (previousAnswers != null) {
            userAnswers.addAll(previousAnswers.getAnswer());
        }
        userAnswers.addAll(userAnswer);
        AnswersHistory history = new AnswersHistory(userAnswers);
        Gson gson = new Gson();
        String json = gson.toJson(history);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_MY_OBJECT, json).apply();
    }

    private static AnswersHistory getSavedUserAnswers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MY_OBJECT, null);
        Gson gson = new Gson();
        return gson.fromJson(json, AnswersHistory.class);
    }

    public static int totalCorrectAnswers(Context context) {
        AnswersHistory savedAnswers = getSavedUserAnswers(context);
        if (savedAnswers != null) {
            int correctAnswers = 0;
            for (UserAnswer answer : savedAnswers.getAnswer()) {
                if (answer.isCorrect()) {
                    correctAnswers++;
                }
            }
            return correctAnswers;
        }
        return 0;
    }

    public static int getTotalQuestions(Context context) {
        AnswersHistory userAnswers = getSavedUserAnswers(context);
        if (userAnswers != null) {
            return userAnswers.getAnswer().size();
        }
        return 0;
    }

    public static void resetSavedQuestions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static int getNumOfQuestion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_NUM_OF_QUESTIONS, 5);
    }

    public static void setNumOfQuestions(Context context, int num) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_NUM_OF_QUESTIONS, num).apply();
    }
}

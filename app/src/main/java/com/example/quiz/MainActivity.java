package com.example.quiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        QuestionBank.INSTANCE.setQuestions(
            new ArrayList<QuestionModel>() {
                {
                    add(new QuestionModel(getString(R.string.question1), false));
                    add(new QuestionModel(getString(R.string.question2), false));
                    add(new QuestionModel(getString(R.string.question3), true));
                    add(new QuestionModel(getString(R.string.question4), true));
                    add(new QuestionModel(getString(R.string.question5), true));
                    add(new QuestionModel(getString(R.string.question6), false));
                    add(new QuestionModel(getString(R.string.question7), true));
                    add(new QuestionModel(getString(R.string.questiin8), true));
                    add(new QuestionModel(getString(R.string.question9), true));
                    add(new QuestionModel(getString(R.string.question10), false));
                }
            }
        );
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.getAverageItem) {
            showAverageDialog();
            return true;
        } else if (itemId == R.id.numQuestionsItem) {
            showEnterNumberDialog();
            return true;
        } else if (itemId == R.id.resetItem) {
            QuestionBank.INSTANCE.resetSavedQuestions(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAverageDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your Correct answers / total number of Questions = " +
            QuestionBank.INSTANCE.totalCorrectAnswers(this) + "/" + QuestionBank.INSTANCE.getTotalQuestions(this));

        alertDialogBuilder.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showEnterNumberDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.select_no_question, null);
        dialogBuilder.setView(dialogView);
        EditText etNumberOfQuestions = dialogView.findViewById(R.id.etNumberOfQuestions);
        dialogBuilder.setTitle(getString(R.string.enter_number_of_questions));
        dialogBuilder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            String input = etNumberOfQuestions.getText().toString().trim();
            if (!input.isEmpty()) {
                int numberOfQuestions = Integer.parseInt(input);
                QuestionBank.INSTANCE.setNumOfQuestions(this, numberOfQuestions);
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancel), null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}

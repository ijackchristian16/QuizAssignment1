package com.example.quiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.quiz.databinding.MainFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFragment extends Fragment {
    private MainFragmentBinding _binding;
    private QuestionBank questionBank = QuestionBank.INSTANCE;
    private List<UserAnswer> userAnswers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = MainFragmentBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showQuestion(true);
        clickListener();
    }

    private void showQuestion(boolean isFirst) {
        if (questionBank.getNumOfQuestion(requireContext()) == userAnswers.size()) {
            showAlertDialog();
            return;
        } else {
            QuestionModel question;
            if (isFirst) {
                question = questionBank.getQuestions().get(0);
            } else {
                List<QuestionModel> questions = questionBank.getQuestions();
                int randomIndex = new Random().nextInt(questions.size());
                question = questions.get(randomIndex);
            }
            _binding.setQuestion(question);
        }
        showProgress();
    }

    private void clickListener() {
        _binding.trueButton.setOnClickListener(view -> {
            boolean isAnswerCorrect = _binding.getQuestion().isAnswer();
            String toastMessage = isAnswerCorrect ? "Correct" : "Incorrect";
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show();
            userAnswers.add(new UserAnswer(_binding.getQuestion().getQuestion(), isAnswerCorrect, isAnswerCorrect));
            showQuestion(false);
        });

        _binding.falseButton.setOnClickListener(view -> {
            boolean isAnswerCorrect = !_binding.getQuestion().isAnswer();
            String toastMessage = isAnswerCorrect ? "Correct" : "Incorrect";
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show();
            userAnswers.add(new UserAnswer(_binding.getQuestion().getQuestion(), !isAnswerCorrect, isAnswerCorrect));
            showQuestion(false);
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.result));
        int correctAnswers = (int) userAnswers.stream().filter(UserAnswer::isCorrect).count();
        builder.setMessage("Your score is " + correctAnswers + " out of " + userAnswers.size());
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            questionBank.saveUserAnswers(requireContext(), userAnswers);
            resetQuiz();
            dialog.dismiss();
        });

        builder.setNegativeButton("Ignore", (dialog, which) -> {
            resetQuiz();
            dialog.dismiss();
        });

        builder.show();
        showProgress();
    }

    private void showProgress() {
        int numOfQuestions = questionBank.getNumOfQuestion(requireContext());
        float progressPercentage = ((float) userAnswers.size() / (float) numOfQuestions) * 100;
        _binding.progressBar.setProgress((int) progressPercentage);
    }

    private void resetQuiz() {
        userAnswers.clear();
        _binding.progressBar.setProgress(0);
        showQuestion(true);
    }
}

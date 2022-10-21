package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mBackButton;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private Hashtable<Integer, ArrayList<Object>> mAnsweredQuestions = new Hashtable<Integer, ArrayList<Object>>();
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private static Integer buttonWidth;
    private static Integer buttonHeight;


    private final Question[] mQuestionBank = new Question[] {
            new Question(R.string.alexelcapo, true),
            new Question(R.string.leakers, false),
            new Question(R.string.caliebre, true),
            new Question(R.string.pokemon, false),
            new Question(R.string.brandon, true),
            new Question(R.string.fantasma, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mAnsweredQuestions = (Hashtable<Integer, ArrayList<Object>>) savedInstanceState.getSerializable(KEY_ANSWERED);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mAnswerTextView = (TextView) findViewById(R.id.answer);
        updateQuestion();
        mTrueButton = (Button) findViewById(R.id.true_button);

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mBackButton = (Button) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mCurrentIndex - 1) < 0 ) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1);
                }
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateQuestion();
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateQuestion();
            }
        });

    }

    private void updateQuestion() {
        ArrayList<Object> answer = mAnsweredQuestions.get(mCurrentIndex);
        if (mTrueButton != null && mFalseButton != null) {
            if (buttonWidth == null || buttonHeight == null) {
                buttonWidth = mTrueButton.getWidth();
                buttonHeight = mTrueButton.getHeight();
            }

            if (answer != null) {
                boolean ans = (boolean) answer.get(0);
                int inp = (int) answer.get(1);
                if (ans) {
                    mTrueButton.setWidth(buttonWidth + 50);
                    mTrueButton.setHeight(buttonHeight + 25);
                    mFalseButton.setWidth(buttonWidth - 50);
                    mFalseButton.setHeight(buttonHeight - 25);
                } else {
                    mTrueButton.setWidth(buttonWidth - 50);
                    mTrueButton.setHeight(buttonHeight - 25);
                    mFalseButton.setWidth(buttonWidth + 50);
                    mFalseButton.setHeight(buttonHeight + 25);
                }
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                mAnswerTextView.setText(inp);
            } else {
                mTrueButton.setWidth(buttonWidth);
                mTrueButton.setHeight(buttonHeight);
                mFalseButton.setWidth(buttonWidth);
                mFalseButton.setHeight(buttonHeight);

                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                mAnswerTextView.setText("");
            }
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        ArrayList<Object> list = new ArrayList<>(Arrays.asList(answerIsTrue, messageResId));
        mAnsweredQuestions.put(mCurrentIndex, list);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putSerializable(KEY_ANSWERED,mAnsweredQuestions);
    }
}
package com.example.geoquiz;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import static com.example.geoquiz.CheatActivity.newIntent;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT=0;
    private static final String CHEATER= "CHEATER";
    private static final String ANS_QUE = "ANSWERED QUESTION";
    private static final String CHEAT_QUE = "CHEATED QUESTION";
    private int a=1;
    private Button mtruebutton;
    private Button mfalsebutton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private boolean mIsCheater;
    private ArrayList<Integer> cQuestion =  new ArrayList<>();
    private ArrayList<Integer> aQuestion = new ArrayList<>();
    private ArrayList<Integer> cheatClicks = new ArrayList<>();
    private void cheatButtonToast(){
//        if(cQuestion.isEmpty()){
//            Toast.makeText(this,"You can press CHEAT button 3 times",Toast.LENGTH_SHORT).show();
//        }
        if(cQuestion.get(cQuestion.size()-1)<4){
            Toast.makeText(this,3-(cQuestion.get(cQuestion.size()-1))+"attempt remaining",Toast.LENGTH_SHORT).show();
        }
        else
            mCheatButton.setClickable(false);

    }
    private void setClicks(){
        if(aQuestion.contains(mCurrentIndex)){
            mtruebutton.setClickable(false);
            mfalsebutton.setClickable(false);
        }
        else if(cQuestion.contains(mCurrentIndex)){
            mtruebutton.setClickable(false);
            mfalsebutton.setClickable(false);
        }
        else if(cheatClicks.contains(3)){
            mCheatButton.setClickable(false);
//            Toast.makeText(this,R.string.Cheat_Clicks,Toast.LENGTH_SHORT).show();
        }
        else{
            mtruebutton.setClickable(true);
            mfalsebutton.setClickable(true);
        }
    }


    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_india,false)
    };
    private int mCurrentIndex = 0;

    private void updateQuestion() {
//        Log.d(TAG, "Updating question text for question #" + mCurrentIndex,
//                new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void cheaterStatus(){
        if(cQuestion.contains(mCurrentIndex))
            mIsCheater = true;
        else
            mIsCheater = false;
    }

    private boolean checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.judgment_toast;
            } else {
                messageResId = R.string.noob_toast;
            }
        } else {
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            setClicks();
        } else {
            messageResId = R.string.incorrect_toast;
            setClicks();
        }}
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
        return userPressedTrue==answerIsTrue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);



        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
//        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
//                updateQuestion();
//            }
//        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = newIntent(MainActivity.this, answerIsTrue,mCurrentIndex);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                cheatClicks.add(a);
                a++;
//                cheatButtonToast();
            }
        });

        mtruebutton = (Button) findViewById(R.id.true_button);
        mtruebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    aQuestion.add(mCurrentIndex);
                    cheaterStatus();
                    checkAnswer(true);

            }
        });
        mfalsebutton = findViewById(R.id.false_button);
        mfalsebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                aQuestion.add(mCurrentIndex);
                cheaterStatus();
                checkAnswer(false);

            }
        });
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                cheaterStatus();
                updateQuestion();
                setClicks();
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex != 0)
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;

                else
                    mCurrentIndex = mQuestionBank.length-1;
                cheaterStatus();
                updateQuestion();
                setClicks();
            }
        });
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER , false);
            aQuestion = savedInstanceState.getIntegerArrayList(ANS_QUE);
            cQuestion = savedInstanceState.getIntegerArrayList(CHEAT_QUE);
        }
        updateQuestion();
        setClicks();
//        cheatButtonToast();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            cQuestion.add(CheatActivity.cheatingIndex(data));


        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEATER,mIsCheater);
        savedInstanceState.putIntegerArrayList(CHEAT_QUE,cQuestion);
        savedInstanceState.putIntegerArrayList(ANS_QUE,aQuestion);
    }
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState){
//        super.onRestoreInstanceState(savedInstanceState);
//    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}

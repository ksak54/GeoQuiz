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
    private static final String THIS_IS="This is";
    private static final String THIS_IS2="This is2";
    private ArrayList<Integer> cheatCounter = new ArrayList<>();
    private Button mtruebutton;
    private Button mfalsebutton;
    private TextView mCheatCounter;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private boolean mIsCheater;
    private ArrayList<Integer> cQuestion =  new ArrayList<>();
    private ArrayList<Integer> aQuestion = new ArrayList<>();
    private ArrayList<Integer> cheatClicks = new ArrayList<>();

    private void setClicks(){
        if(aQuestion.contains(mCurrentIndex)){
            mtruebutton.setClickable(false);
            mfalsebutton.setClickable(false);
        }
        else if(cQuestion.contains(mCurrentIndex)){
            mtruebutton.setClickable(false);
            mfalsebutton.setClickable(false);
        }
        else{
            mtruebutton.setClickable(true);
            mfalsebutton.setClickable(true);
        }
    }
    private cheatClass[] mCheatBank = new cheatClass[] {
            new cheatClass(R.string.ch0),
            new cheatClass(R.string.ch1),
            new cheatClass(R.string.ch2),
            new cheatClass(R.string.ch3),
    };
    private int mCheaterIndex = 0;

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
    private void setCheatText(){
        int que = mCheatBank[mCheaterIndex].getCtextId();
        if(cheatCounter.size()>=3)
            mCheatCounter.setText(que);
        else if(cheatCounter.size()==2)
            mCheatCounter.setText(que);
        else if(cheatCounter.size()==1)
            mCheatCounter.setText(que);
        else
            mCheatCounter.setText(que);
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
                setClicks();
            } else {
                messageResId = R.string.noob_toast;
                setClicks();
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
        mCheatCounter = (TextView) findViewById(R.id.Chances);

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = newIntent(MainActivity.this, answerIsTrue,mCurrentIndex);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                ++mCheaterIndex;
                if(cheatCounter.size()>2){
                    mCheatButton.setClickable(false);
                }

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
            cheatCounter = savedInstanceState.getIntegerArrayList(THIS_IS);
            mCheaterIndex = savedInstanceState.getInt(THIS_IS2);
        }
        updateQuestion();
        setClicks();
        setCheatText();
        if(cheatCounter.size()>2){
            mCheatButton.setClickable(false);
        }
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
            cheatCounter.add(CheatActivity.cheatcounter(data));
            cQuestion.add(CheatActivity.cheatingIndex(data));
            setCheatText();
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
        savedInstanceState.putIntegerArrayList(THIS_IS,cheatCounter);
        savedInstanceState.putInt(THIS_IS2,mCheaterIndex);
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

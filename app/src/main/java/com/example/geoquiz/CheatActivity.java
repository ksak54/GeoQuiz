package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;


public class CheatActivity extends AppCompatActivity {
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private static final String ANSWER = "ANSWER";
    private static final String CHEAT_INDEX = "CHEAT_INDEX";
    private int mCheatIndex;
    private static final String CHEAT_COUNTER = "No of Cheats"   ;
    private static final String CHEAT_COUNTE = "No of Cheatss"   ;
    private int a = 0;
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.geoquiz.answer_shown";
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.geoquiz.answer_is_true";
    private static final String CURRENT_INDEX = "com.example.geoquiz.current_index";
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
    public static int cheatcounter(Intent result){
        return result.getIntExtra(CHEAT_COUNTER,0);
    }
    public static int cheatingIndex(Intent data){
        return data.getIntExtra("CHEAT_INDEX",0);
    }
    public static Intent newIntent(Context packageContext, boolean answerIsTrue,int mCurrentIndex) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(CURRENT_INDEX,mCurrentIndex);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
         mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

         mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);


        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a+=1;
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mCheatIndex = getIntent().getIntExtra(CURRENT_INDEX,0);

                setAnswerShownResult(true);

            }
        });
        if(savedInstanceState!=null){
            mAnswerIsTrue = savedInstanceState.getBoolean(ANSWER,false);
            a = savedInstanceState.getInt(CHEAT_COUNTE,0);
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
            mShowAnswerButton.setClickable(false);

        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ANSWER, mAnswerIsTrue);
        savedInstanceState.putInt(CHEAT_COUNTE,a);
    }


    private void setAnswerShownResult(boolean isAnswerShown) {

        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(CHEAT_INDEX,mCheatIndex);
        data.putExtra(CHEAT_COUNTER,a);
        setResult(RESULT_OK, data);
    }

}

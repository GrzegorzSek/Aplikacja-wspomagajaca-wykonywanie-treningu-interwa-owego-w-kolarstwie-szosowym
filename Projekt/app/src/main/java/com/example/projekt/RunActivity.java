package com.example.projekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import java.util.Locale;

public class RunActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private int selectedId;

    private Context context;
    private ConstraintLayout screen;

    private static final String TAG = "RunActivity";

    private boolean wasPaused = false;
    private String num = "0";
    /*
    0 -- warm_up
    1 -- low_intst
    2 -- high_intst
    3 -- rest
    4 -- cool_down
    5 -- end of training
    */

    private TextView overallTime;
    private TextView timer; // time (text)
    private TextView sets; // number of set (text)
    private TextView cycles; // numer of cycles (text)
    private TextView partOfTraining; // warm_up/low/high/rest/cold_down/

    private Button ButtonStartPause; // button that change between pause and start
    private Button ButtonReset; // button that reset whole training

    private CountDownTimer mCountDownTimer; // CountDowntimer variable

    private boolean isTimerRunning = false; // TRUE when timer is counting down

    private int overallTimeMinutes = 0;
    private int overallTimeSeconds = 0;
    private int pausedOverallTimeSeconds = 0;

    private int numberOfSets;
    private int numberOfSetsCounter = 0;
    private int numberOfCycles;
    private int numberOfCyclesCounter = 0;
    private long warmUpDuration;
    private long lowIntstDuration;
    private long highIntstDuration;
    private long restDuration;
    private long coolDownDuration;
    private long timeLeftInMillis; // time left in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        // screen
        context = getApplicationContext();
        screen = (ConstraintLayout) findViewById(R.id.screen);

        // Database
        mDatabaseHelper = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();
        selectedId = receivedIntent.getIntExtra("id", -1);

        Cursor data = mDatabaseHelper.getFullData(selectedId);
        mDatabaseHelper.getFullData(selectedId);

        Log.d(TAG, "setting up variables");
        String warmUpString = "0";
        String lowIntstString = "0";
        String highIntstString = "0";
        int setsInt = 0;
        String restString = "0";
        String coolDownString = "0";
        int cyclesInt = 0;

        // getting data from DB
        while(data.moveToNext()){
            warmUpString = data.getString(1);
            lowIntstString = data.getString(2);
            highIntstString = data.getString(3);
            setsInt = Integer.parseInt(data.getString(4));
            restString = data.getString(5);
            coolDownString = data.getString(6);
            cyclesInt =  Integer.parseInt(data.getString(7));
        }
        // setting up init values
        numberOfSets = setsInt;
        numberOfCycles = cyclesInt;
        warmUpDuration = stringToLong(warmUpString);
        lowIntstDuration = stringToLong(lowIntstString);
        highIntstDuration = stringToLong(highIntstString);
        restDuration = stringToLong(restString);
        coolDownDuration = stringToLong(coolDownString);
        timeLeftInMillis = warmUpDuration; // time left in milliseconds

        // TextViews, buttons
        overallTime = findViewById(R.id.overall_time);
        timer = findViewById(R.id.timer);
        sets = findViewById(R.id.sets);
        cycles = findViewById(R.id.cycles);
        partOfTraining = findViewById(R.id.part_of_training);

        ButtonStartPause = findViewById(R.id.start_pause);
        ButtonReset = findViewById(R.id.reset);

        ButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonReset.setVisibility(View.INVISIBLE);
                if(isTimerRunning){
                    pauseTimer();
                }else{
                    runTraining();
                }
            }
        });

        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        setTextViews();
        updateCountDownText(); // set init timer (text) value
    }

    private void startTimer(){
        wasPaused = false;
        mCountDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { // updates every second
                timeLeftInMillis = millisUntilFinished; // pause
                pausedOverallTimeSeconds = overallTimeSeconds;
                overallTimeSeconds++;
                updateOverallTime();
                updateCountDownText();
            }

            @Override
            public void onFinish() { // runs when time = 00:00
                runTraining();
            }
        }.start();
        isTimerRunning = true;
        ButtonStartPause.setText("PAUSE");
    }

    private void updateCountDownText(){ // updates timer (text) every second
        int minutes = (int) timeLeftInMillis/1000/60; // floor
        int seconds = (int) timeLeftInMillis/1000%60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds); // data: 1, result: 01,  d: 11, r: 11

        timer.setText(timeLeftFormatted);
    }

    private void pauseTimer(){
        wasPaused = true;
        mCountDownTimer.cancel();
        ButtonReset.setVisibility(View.VISIBLE);
        isTimerRunning = false;
        ButtonStartPause.setText("START");
        overallTimeSeconds = pausedOverallTimeSeconds;
    }

    private void resetTimer(){
        screen.setBackgroundColor(ContextCompat.getColor(context, R.color.white)); // 0xff7373
        wasPaused = false;
        num = "0";
        numberOfCyclesCounter = 0;
        numberOfSetsCounter = 0;
        ButtonStartPause.setVisibility(View.VISIBLE);
        ButtonReset.setVisibility(View.INVISIBLE);
        timeLeftInMillis = warmUpDuration;
        partOfTraining.setText("Warm up");
        updateCountDownText();
        setTextViews();
        overallTimeMinutes = 0;
        overallTimeSeconds = 0;
    }

    private void runTraining(){
        if(!wasPaused) {
            switch (num) {
                case "0": // warm up
                    timeLeftInMillis = warmUpDuration;
                    partOfTraining.setText("Warm up");
                    startTimer();
                    num = "1";
                    break;
                case "1": // low intensity
                    screen.setBackgroundColor(ContextCompat.getColor(context, R.color.green)); //0x73ff78
                    numberOfSetsCounter++;
                    Log.d(TAG, "Current cycle: " + numberOfCyclesCounter + " Current set: " + numberOfSetsCounter);
                    setTextViews();
                    timeLeftInMillis = lowIntstDuration;
                    partOfTraining.setText("Low");
                    startTimer();
                    num = "2";
                    break;
                case "2": // high intensity
                    screen.setBackgroundColor(ContextCompat.getColor(context, R.color.red)); // 0xff7373
                    timeLeftInMillis = highIntstDuration;
                    partOfTraining.setText("High");
                    startTimer();
                    if (numberOfSetsCounter < numberOfSets) {
                        num = "1";
                    } else if (numberOfSetsCounter == numberOfSets && numberOfCyclesCounter != numberOfCycles - 1) { // -1, because we start counting at 0
                        num = "3";
                    } else {
                        num = "4";
                    }
                    break;
                case "3": // rest
                    screen.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow)); // 0xff7373
                    numberOfSetsCounter = 0;
                    setTextViews();
                    numberOfCyclesCounter++;
                    timeLeftInMillis = restDuration;
                    partOfTraining.setText("Rest");
                    startTimer();
                    num = "1";
                    break;
                case "4": // cool down
                    screen.setBackgroundColor(ContextCompat.getColor(context, R.color.blue)); // 0xff7373
                    timeLeftInMillis = coolDownDuration;
                    partOfTraining.setText("Cool down");
                    startTimer();
                    num = "5";
                    break;
                case "5": // end of training
                    ButtonStartPause.setVisibility(View.INVISIBLE);
                    ButtonReset.setVisibility(View.VISIBLE);
                    isTimerRunning = false;
                    ButtonStartPause.setText("START");
                    break;
            }
        }else{
            startTimer();
        }
    }

    public void setTextViews(){
        String cyclesTemp = (numberOfCyclesCounter + 1) + "/" + numberOfCycles;
        String setsTemp = numberOfSetsCounter + "/" + numberOfSets;
        cycles.setText(cyclesTemp);
        sets.setText(setsTemp);
        overallTime.setText("00:00");
    }

    public long stringToLong(String time){
        long timeLong;
        long timeLong_1;
        long timeLong_2;
        long minutes_1 = Character.getNumericValue(time.charAt(0));
        long minutes_2 = Character.getNumericValue(time.charAt(1));
        long seconds_1 = Character.getNumericValue(time.charAt(3));
        long seconds_2 = Character.getNumericValue(time.charAt(4));

        // converting minutes to milliseconds
        if(minutes_1 != 0){
            timeLong_1 = (minutes_1 * 10 + minutes_2) * 1000 * 60;
        }else{
            timeLong_1 = minutes_2 * 1000 * 60;
        }

        //converting seconds to milliseconds
        if(seconds_1 != 0){
            timeLong_2 = (seconds_1 * 10 + seconds_2) * 1000;
        }else{
            timeLong_2 = seconds_2 * 1000;
        }

        timeLong = timeLong_1 + timeLong_2;

        return timeLong;
    }

    public void updateOverallTime(){
        if(overallTimeSeconds == 60){
            overallTimeSeconds = 0;
            overallTimeMinutes++;
        }
        String overallTimeString = String.format(Locale.getDefault(), "%02d:%02d", overallTimeMinutes, overallTimeSeconds); // data: 1, result: 01,  d: 11, r: 11

        overallTime.setText(overallTimeString);
    }
}
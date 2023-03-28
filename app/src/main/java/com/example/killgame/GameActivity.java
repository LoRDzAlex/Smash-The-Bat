package com.example.killgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.BarringInfo;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.util.Random;

/**
 * The GameActivity class implements the gameplay logic for the KillGame app.
 * It extends the AppCompatActivity class and manages the creation and deletion
 * of bat and explosion objects, as well as updates the score and round number.
 */
public class GameActivity extends AppCompatActivity {
    TextView score, round, missed, gameover;
    Random randomizer;
    FrameLayout gamefield;

    boolean finished = false;
    int batnumber, batkilled, points, roundnumber = 1, batmissed = 0, highscore;
    float cm;
    Handler createHandler = new Handler();
    Handler deleteHandler = new Handler();
    Handler gameoverHandler = new Handler();
    boolean isRoundIncreased = false;
    long creationTime = 100;

    public static final String LOGTAG = "GameActivity";

    /**
     * Called when the activity is first created. Initializes the randomizer, density,
     * gamefield, and score and round TextViews, and hides the action bar.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        randomizer = new Random();
        cm = getResources().getDisplayMetrics().density;
        gamefield = findViewById(R.id.gamefield);
        score = findViewById(R.id.score);
        round = findViewById(R.id.round);
        missed = findViewById(R.id.missed);
        gameover = findViewById(R.id.gameover);
        gameover.setVisibility(View.INVISIBLE);
        getSupportActionBar().hide();
        Log.d(LOGTAG, "onCreate() called");
    }

    /**
     * Called when the activity is resumed. Starts the task for creating bats.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCreateBatTask();
    }

    /**
     * Called when the activity is paused. Stops the task for creating bats.
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopCreateBatTask();
    }

    /**
     * Starts the task for creating bats.
     */
    private void startCreateBatTask() {
        createHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!finished){
                    createBat(gamefield);
                    createHandler.postDelayed(this, creationTime * 10);
            }
                else{
                    createHandler.removeCallbacksAndMessages(null);
                }
            }
        }, creationTime*10);
    }

    /**
     * Stops the task for creating bats.
     */
    private void stopCreateBatTask() {
        finished = true;
        createHandler.removeCallbacksAndMessages(null);
        deleteHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Creates a bat object and adds it to the gamefield. Also updates the round number
     * if necessary, and starts the task for deleting the bat after a set amount of time.
     *
     * @param v The View object to add the bat to.
     */
    public void createBat(View v){
        BatLoader bat = new BatLoader(this);
        bat.setOnClickListener(this::killBat);
        batnumber++;
        int width = gamefield.getWidth();
        int height = gamefield.getHeight();
        int bat_width = Math.round(cm * 75);
        int bat_height = Math.round(cm * 42);
        int left = randomizer.nextInt(width - bat_width);
        int top = randomizer.nextInt(height - bat_height);
        bat.setBackgroundResource(R.drawable.batloader);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bat_width, bat_height);
        params.leftMargin = left;
        params.topMargin = top;
        params.gravity = Gravity.TOP+Gravity.LEFT;
        gamefield.addView(bat, params);


        /*
         * Updates the round number and creation time based on the current bat number.
         * If bat number is 0, isRoundIncreased is set to false. If bat number is a multiple
         * of 10 and isRoundIncreased is false, round number is increased by 1 and creation
         * time is decreased by 1. If neither condition is met, isRoundIncreased is set to false.
         */
        if (batkilled == 0){
            isRoundIncreased = false;
            round.setText("Round: "+ roundnumber);
        }
        else if (batkilled % 10 == 0 && !isRoundIncreased){
            roundnumber++;
            round.setText("Round: "+ roundnumber);
            isRoundIncreased = true;
            creationTime--; // hier wird die Erstellungszeit um 1 Sekunde reduziert
        } else {
            isRoundIncreased = false;
        }
        /*
         * Deletes the bat after 1,7 seconds if it has not been killed.
         */
        deleteHandler.postDelayed(() -> {
            removeBat(bat);
            batmissed = batnumber - batkilled;
            removeBatCounter(batmissed);
        }, 1700);

        /*
         * If batmissed is greater than or equal to 10, stops the createBatTask, sets the
         * highscore, and returns to the MainActivity with the highscore as an extra.
         */
        if (batmissed >= 10){
            setGameover();
        }
    }
    private void setGameover(){
        stopCreateBatTask(); // stoppe die Erstellung von Fledermäusen
        gameover.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.putExtra("SCORE", batkilled*10);
        setResult(RESULT_OK, intent);
        gameoverHandler.postDelayed(this::finish, 5000);
    }
    private void removeBat(View v){
        gamefield.removeView(v);
    }

    private void removeBatCounter(int batmissed){
            missed.setText("Missed: " + batmissed);
            Log.d(LOGTAG + " Bat missed: ", String.valueOf(batmissed));
    }

    /**
     * Called when a bat object is clicked. Updates the score, creates an explosion object,
     * and removes the bat object from the gamefield.
     *
     * @param v The View object that was clicked.
     */
    private void killBat(View v){
        batkilled++;
        setExplosion(v);
        gamefield.removeView(v);
        score.setText("Punkte: "+ batkilled*10);
    }

    /**
     * Creates an explosion object at the location of a killed bat and adds it to the gamefield.
     *
     * @param v The View object representing the killed bat.
     */
    public void setExplosion(View v){
        ExplosionLoader explosionLoader = new ExplosionLoader(this);
        explosionLoader.setBackgroundResource(R.drawable.explostionloader);
        int explosion_width = Math.round(cm * 60);
        int explosion_height = Math.round(cm * 60);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(explosion_width, explosion_height);
        params.topMargin = v.getTop();
        params.leftMargin = v.getLeft();
        params.gravity = Gravity.TOP+Gravity.LEFT;
        gamefield.addView(explosionLoader, params);
    }
}
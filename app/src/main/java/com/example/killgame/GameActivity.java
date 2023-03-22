package com.example.killgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.BarringInfo;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * The GameActivity class implements the gameplay logic for the KillGame app.
 * It extends the AppCompatActivity class and manages the creation and deletion
 * of bat and explosion objects, as well as updates the score and round number.
 */
public class GameActivity extends AppCompatActivity {
    TextView score, round;
    Random randomizer;
    FrameLayout gamefield;
    boolean killed = false;
    int batnumber, points, roundnumber = 1, batmissed, highscore;
    float cm;
    Handler createHandler = new Handler();
    Handler deleteHandler = new Handler();
    boolean isRoundIncreased = false;
    long creationTime = 100;

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
        getSupportActionBar().hide();
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
                createBat(gamefield);
                createHandler.postDelayed(this, creationTime*10);
            }
        }, creationTime*10);
    }

    /**
     * Stops the task for creating bats.
     */
    private void stopCreateBatTask() {
        createHandler.removeCallbacksAndMessages(null);
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
        if (batnumber == 0){
            isRoundIncreased = false;
        }
        else if (batnumber % 10 == 0 && !isRoundIncreased){
            roundnumber++;
            round.setText("Round: "+ roundnumber);
            isRoundIncreased = true;
            creationTime--; // hier wird die Erstellungszeit um 1 Sekunde reduziert
        } else {
            isRoundIncreased = false;
        }
        /*
         * Deletes the bat after 1.7 seconds if it has not been killed.
         */
        deleteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!killed){
                    gamefield.removeView(bat);
                    batmissed++;
                }
            }
        }, 1700); // lösche die Fledermaus nach 1,7 Sekunden

        /*
         * If batmissed is greater than or equal to 10, stops the createBatTask, sets the
         * highscore, and returns to the MainActivity with the highscore as an extra.
         */
        if (batmissed >= 10){
            stopCreateBatTask();
            highscore = batnumber*10;

            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            intent.putExtra("HighScore: ", highscore);
            startActivity(intent);
        }
    }

    /**
     * Called when a bat object is clicked. Updates the score, creates an explosion object,
     * and removes the bat object from the gamefield.
     *
     * @param v The View object that was clicked.
     */
    private void killBat(View v){
        batnumber++;
        setExplosion(v);
        gamefield.removeView(v);
        score.setText("Punkte: "+ batnumber*10);
        killed=true;
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
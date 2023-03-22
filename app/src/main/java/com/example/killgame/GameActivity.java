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

public class GameActivity extends AppCompatActivity {
    TextView score, round;
    Random randomizer;
    FrameLayout gamefield;
    boolean killed = false;
    int batnumber, points, roundnumber, batmissed, highscore;
    float cm;
    Handler createHandler = new Handler();
    Handler deleteHandler = new Handler();
    boolean isRoundIncreased = false;
    long creationTime = 100;

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

    @Override
    protected void onResume() {
        super.onResume();
        startCreateBatTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCreateBatTask();
    }

    private void startCreateBatTask() {
        createHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createBat(gamefield);
                createHandler.postDelayed(this, creationTime*10);
            }
        }, creationTime*10);
    }

    private void stopCreateBatTask() {
        createHandler.removeCallbacksAndMessages(null);
    }

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

        if (batnumber == 0){
            isRoundIncreased = false;
            roundnumber = 1;
        }
        else if (batnumber % 10 == 0 && !isRoundIncreased){
            roundnumber++;
            round.setText("Round: "+ roundnumber);
            isRoundIncreased = true;
            creationTime--; // hier wird die Erstellungszeit um 1 Sekunde reduziert
        } else {
            isRoundIncreased = false;
        }

        deleteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!killed){
                    gamefield.removeView(bat);
                    batmissed++;
                }
            }
        }, 1700); // lösche die Fledermaus nach 1,7 Sekunden

        if (batmissed >= 10){
            stopCreateBatTask();
            highscore = batnumber*10;

            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            intent.putExtra("HighScore: ", highscore);
            startActivity(intent);
        }
    }

    private void killBat(View v){
        batnumber++;
        setExplosion(v);
        gamefield.removeView(v);
        score.setText("Punkte: "+ batnumber*10);
        killed=true;
    }

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
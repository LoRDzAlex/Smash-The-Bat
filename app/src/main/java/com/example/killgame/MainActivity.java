package com.example.killgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{
    Handler handy;
    final static int DELAY = 1000;
    String displayHighScore;

    TextView start_game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        TextView startGameActivity = findViewById(R.id.start_game);
        TextView HighScore = findViewById(R.id.highscore);

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            displayHighScore = "HighScore: 0";
        } else {
            displayHighScore = "HighScore: "+extras.getInt("HighScore: ", 0);
        }
        HighScore.setText(displayHighScore);
        handy = new Handler();
        handy.postDelayed(this,DELAY);

    startGameActivity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);

            startActivity(intent);
        }
    });


}

    @Override
    public void run(){
        Calendar cal = Calendar.getInstance();
        TextView txt = findViewById(R.id.textUhr);
        txt.setText(cal.get(Calendar.HOUR_OF_DAY)+ ":"+cal.get(Calendar.MINUTE)+ ":"+cal.get(Calendar.SECOND));
        handy.postDelayed(this,DELAY);
    }

    public void closeActivity(View v){
        finish();
    }
}
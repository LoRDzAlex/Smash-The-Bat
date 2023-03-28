package com.example.killgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The `MainActivity` class represents the main activity of the "Kill Game" Android app.
 * It extends the `AppCompatActivity` class and implements the `Runnable` interface.
 *
 * This activity displays a welcome screen to the user, showing the current time and the high score.
 * It also allows the user to start a new game by clicking the "Start Game" button.
 */
public class MainActivity extends AppCompatActivity implements Runnable{

    // A handler for posting delayed messages (runnables)
    Handler handy;

    // The delay between each update of the clock
    final static int DELAY = 1000;

    // The string to display the current high score
    String displayHighScore;

    // The "Start Game" button
    TextView start_game;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity layout
        setContentView(R.layout.activity_main);

        // Hide the action bar
        getSupportActionBar().hide();

        // Find the views
        TextView startGameActivity = findViewById(R.id.start_game);

        /*
        // Get the high score from the intent extras
        Bundle extras = getIntent().getExtras();
        if (extras == null){
            displayHighScore = "HighScore: 0";
        } else {
            displayHighScore = "HighScore: "+extras.getInt("HighScore: ", 0);
        }

        // Set the high score text view
        HighScore.setText(displayHighScore);
        */

        // Create a handler for posting delayed messages (runnables)
        handy = new Handler();
        // Post a runnable for updating the clock
        handy.postDelayed(this,DELAY);

        // Set a click listener for the "Start Game" button
    startGameActivity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Create an intent to start the GameActivity
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivityForResult(intent, 1);
        }
    });

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){
                int result=data.getIntExtra("SCORE",0);
                TextView HighScore = findViewById(R.id.highscore);
                displayHighScore = "HighScore: "+result;
                HighScore.setText(displayHighScore);
            }
    }


    /**
     * Called when the runnable is executed.
     */
    @Override
    public void run(){
        // Get the current time
        Calendar cal = Calendar.getInstance();

        // Find the text view for displaying the clock
        TextView txt = findViewById(R.id.textUhr);

        // Update the text view with the current time
        txt.setText(cal.get(Calendar.HOUR_OF_DAY)+ ":"+cal.get(Calendar.MINUTE)+ ":"+cal.get(Calendar.SECOND));

        // Post a new runnable with a delay
        handy.postDelayed(this,DELAY);
    }

    /**
     * Called when the activity is being destroyed.
     *
     * @param v the view that was clicked
     */
    public void closeActivity(View v){
        // Finish the activity
        finish();
    }
}
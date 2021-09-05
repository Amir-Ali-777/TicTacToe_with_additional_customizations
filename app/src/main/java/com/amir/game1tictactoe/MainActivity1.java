package com.amir.game1tictactoe;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.amir.game1tictactoe.*;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    private static int DELAY = 2000;

    private Button[][] buttons = new Button[3][3];

    private ImageView iv;
    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView tv_p1,tv_p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        tv_p1 = findViewById(R.id.tv_p1);
        tv_p2 = findViewById(R.id.tv_p2);

        final MediaPlayer reset = MediaPlayer.create(this, R.raw.reset_play);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String buttonID = "button_"+i+j;
                int resID = getResources().getIdentifier(buttonID,"id",getPackageName());
                buttons[i][j] = findViewById(resID);
              //  buttons[i][j].setBackgroundColor(getResources().getColor(R.color.white));
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.start();
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(!((Button) v).getText().toString().equals("")){
            return;
        }

        final MediaPlayer xo_button = MediaPlayer.create(this, R.raw.xo_button_tap);
        xo_button.start();

        if(player1Turn){
            ((Button)v).setText("X");
            Typeface typeface = ResourcesCompat.getFont(
                    this,
                    R.font.mereinda_bold);
            ((Button)v).setTextColor(getResources().getColor(R.color.red));
            ((Button)v).setTypeface(typeface);
        } else {
            ((Button) v).setText("O");
            Typeface typeface = ResourcesCompat.getFont(
                    this,
                    R.font.mereinda_bold);
            ((Button)v).setTextColor(getResources().getColor(R.color.blue));
            ((Button)v).setTypeface(typeface);
        }
        roundCount++;

        if (checkForWin()){
            if (player1Turn){
                player1Wins();
            }else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin(){
        String[][] field = new String[3][3];

        for(int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i=0;i<3;i++){
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                return true;
            }
        }


        for (int i=0;i<3;i++){
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    private void player1Wins(){
        final MediaPlayer win = MediaPlayer.create(this, R.raw.win_sound);
        win.start();
        player1Points++;
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toastx,(ViewGroup) findViewById(R.id.custom_toast_layout_x));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
        updatePointsText();
        resetBoard();
    }
    private void player2Wins(){
        final MediaPlayer win = MediaPlayer.create(this, R.raw.win_sound);
        win.start();
        player2Points++;
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toasto,(ViewGroup) findViewById(R.id.custom_toast_layout_o));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
        updatePointsText();
        resetBoard();
    }
    private void draw(){
        final MediaPlayer draw_sound = MediaPlayer.create(this, R.raw.draw_sound);
        draw_sound.start();
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toastxo,(ViewGroup) findViewById(R.id.custom_toast_layout_xo));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
        resetBoard();
    }

    private void updatePointsText(){
        tv_p1.setText("PLAYER 1: "+player1Points);
        tv_p2.setText("PLAYER 2: "+player2Points);
    }

    private void resetBoard(){
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(false);
            }
        }
        roundCount = 0;
        player1Turn = true;
        // Providing the user touch response delay so that the user doesn't bash on the screen immediately
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        buttons[i][j].setText("");
                        buttons[i][j].setEnabled(true);
                    }
                }
            }
        }, DELAY);
    }

    private void resetGame(){
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("rC",roundCount);
        outState.putInt("p1p",player1Points);
        outState.putInt("p2p",player2Points);
        outState.putBoolean("p1t",player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("rC");
        player1Points = savedInstanceState.getInt("p1p");
        player2Points = savedInstanceState.getInt("p2p");
        player1Turn = savedInstanceState.getBoolean("p1t");
    }
}
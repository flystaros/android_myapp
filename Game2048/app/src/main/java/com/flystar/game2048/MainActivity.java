package com.flystar.game2048;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
{
    private TextView tvScore;
    private static MainActivity mainActivity = null;
    private int score = 0;
    private Button btRetry;
    private GameView gameview;
    private AnimLayer animLayer;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);
        btRetry = (Button) findViewById(R.id.btRetry);
        animLayer = (AnimLayer) findViewById(R.id.animLayer);

        gameview = (GameView) findViewById(R.id.gameView);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               retry();

            }
        });
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }

    public void clearScore()
    {
        score = 0;
        showScore();
    }

    public void showScore()
    {
        tvScore.setText(score+"");
    }

    public void addScore(int score)
    {
        this.score+=score;
        showScore();
    }

    public void retry()
    {

        gameview.startGame();

    }











}

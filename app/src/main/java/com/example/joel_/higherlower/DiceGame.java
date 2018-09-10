package com.example.joel_.higherlower;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceGame extends AppCompatActivity {

    private ImageButton mButtonLower;
    private ImageButton mButtonHigher;
    private ImageView mDice;
    private ArrayAdapter<Integer> mAdapter;
    private ListView mPreviousThrows;
    private TextView mScore;
    private int mAmountRight = 0;
    private int mHighScore = 0;
    private List<Integer> mPreviousRolls = new ArrayList<>();

    private int[] mDiceImages ={R.drawable.d1,R.drawable.d2,R.drawable.d3,R.drawable.d4,R.drawable.d5,R.drawable.d6};
    Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_game);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //(toolbar);

        mButtonLower = findViewById(R.id.button_lower);
        mButtonHigher =  findViewById(R.id.button_higher);
        mDice = findViewById(R.id.dice_image);
        mPreviousThrows = findViewById(R.id.list_view);
        mScore = findViewById(R.id.text_view);


        //Guessing the number to be lower
        mButtonLower.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                rollDice(true, view);
                                           }
        });

        //Guessing the number to be higher
        mButtonHigher.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                rollDice(false, view);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dice_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = mItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(mItem);
    }

    private int rollDice(boolean mLesser, View view)
    {
        int diceRoll;
        //This rolls the dice and gives a random number between 1 and 6
        if (mPreviousRolls.size() != 0) {
            do {
                diceRoll = mRandom.nextInt(6);
            } while (diceRoll == mPreviousRolls.get(mPreviousRolls.size() - 1));
        } else
        {
            diceRoll = mRandom.nextInt(6);
        }
        checkWon(diceRoll, mLesser, view);
        mPreviousRolls.add(diceRoll + 1);
        updateDice(diceRoll);
        updateScoreList();
        return diceRoll;
    }

    private void updateDice(int mDiceRoll)
    {
        mDice.setImageResource(mDiceImages[mDiceRoll]);
    }

    private void updateScoreList()
    {
        // If the list adapter is null, a new one will be instantiated and set on our list view.
        if (mAdapter == null) {
            // We can use ‘this’ for the context argument because an Activity is a subclass of the Context class.
            // The ‘android.R.layout.simple_list_item_1’ argument refers to the simple_list_item_1 layout of the Android layout directory.
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mPreviousRolls);
            mPreviousThrows.setAdapter(mAdapter);
        } else {
            // When the adapter is not null, it has to know what to do when our dataset changes, when a change happens we need to call this method on the adapter so that it will update internally.
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkWon(int mDiceRoll, boolean mLesser, View view)
    {
        int mLastValue;
        if (mPreviousRolls.size() != 0) {
            mLastValue = mPreviousRolls.get(mPreviousRolls.size() - 1);
        } else
        {
            mLastValue = 0;
        }
        if ((mDiceRoll < mLastValue && mLesser) || (mDiceRoll > mLastValue && !mLesser))
        {
            Snackbar.make(view, mDiceRoll, Snackbar.LENGTH_LONG).setAction("You Won!", null).show();
            mAmountRight ++;

            if (mAmountRight > mHighScore)
            {
                mHighScore = mAmountRight;
            }
            updateScore();
            return true;
        } else
        {
           // Snackbar.make(view, mDiceRoll, Snackbar.LENGTH_LONG).setAction("You Lost :(", null).show();
            mPreviousRolls.clear();
            mAmountRight = 0;
            updateScore();
            return false;
        }

    }

    private void updateScore()
    {
        mScore.setText("Highscore: " + String.valueOf(mHighScore) + "\nScore: " + String.valueOf(mAmountRight));
    }


}

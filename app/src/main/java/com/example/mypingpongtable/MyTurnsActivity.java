package com.example.mypingpongtable;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTurnsActivity extends AppCompatActivity implements Serializable {

    private TextView welcomePlayerTxt;
    private Button returnBtn;
    private String username;
    private ArrayList<Game> gameList;
    private ArrayList<Game> deletedGameList;
    RecyclerView myTurnsRecyclerView;
    MyTurnAdapter myTurnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ArrayList<MyTurnSlot> getMyList() {

        ArrayList<MyTurnSlot> turns = new ArrayList<>();
        for (Game game : gameList) {
            MyTurnSlot slot = new MyTurnSlot();
            slot.setTurnTime(game.getDateString() + " at " + game.getTimeString());

            if (game.isFull()) {
                String opponent =
                        username.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
                slot.setTurnAgainst("Playing against: " + opponent);

//                slot.setSlotImage(R.drawable.waiting);
            } else {
                slot.setTurnAgainst("Waiting for an opponent");
//                slot.setSlotImage(R.drawable.header_bg);
            }
            turns.add(slot);
        }
        return turns;
    }


    public void removeGame(int position)
    {
        myTurnAdapter.removeGame(position);
        Game game = gameList.get(position);
        deletedGameList.add(game);
        gameList.remove(position);
        Toast.makeText(getApplicationContext(),"Turn: "+game.getDate() +" at "+ game.getTime() +" was deleted",Toast.LENGTH_SHORT).show();

    }


}
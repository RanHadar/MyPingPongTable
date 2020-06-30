package com.example.mypingpongtable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
        setContentView(R.layout.activity_my_turns);
        myTurnsRecyclerView = findViewById(R.id.recyclerViewTurns);
        myTurnsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);
        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent incomingIntent = getIntent();
        username = incomingIntent.getStringExtra("username");
        gameList = (ArrayList<Game>) getIntent().getExtras().getSerializable("game_list");

        welcomePlayerTxt.setText("My Turns:");
        myTurnAdapter = new MyTurnAdapter(this, getMyList());
        myTurnsRecyclerView.setAdapter(myTurnAdapter);
        setAdapterClickListener();
        setAdapterSwipe();

        deletedGameList = new ArrayList<Game>();
    }

    private void setAdapterSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeGame(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(myTurnsRecyclerView);
    }

    private void setAdapterClickListener() {
        myTurnAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                removeGame(position);
            }
        });
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

    @Override
    public void onBackPressed() {

        Intent backIntent = new Intent();
        backIntent.putExtra("deletedGames", this.deletedGameList);
        setResult(RESULT_OK, backIntent);

        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
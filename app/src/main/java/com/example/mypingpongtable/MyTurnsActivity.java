package com.example.mypingpongtable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTurnsActivity extends AppCompatActivity implements Serializable {

    private TextView welcomePlayerTxt;
    private Button returnBtn;
    private String username;
    private ArrayList<Game> gameList;
    private ArrayList<Game> deletedGameList;
    RecyclerView myTurnsRecyclerView;
//    MyTurnAdapter myTurnAdapter;

    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private ImageView[] headerRacketIcons = new ImageView[GAMES_PER_HOUR];
    private Button[] leftJoinButtons = new Button[GAMES_PER_HOUR];
    private Button[] rightJoinButtons = new Button[GAMES_PER_HOUR];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    private ArrayList<String> getMyList() {
        ArrayList<String> turns = new ArrayList<>();

        return turns;

    }


    private void updateHeaderIcons() {
        ArrayList<Game> games = server.getHourAgenda(selectedDate, selectedHour);
        server.saveState();

        for (int i = 0; i < 4; i++) {
//            headerTexts[i].setTypeface(Typeface.DEFAULT_BOLD);
            switch (games.get(i).empty_slots()) {
                case 0:
                    if (games.get(i).getPlayer1().equals(username) || games.get(i).getPlayer2().equals(username)) {
                        headerRacketIcons[i].setImageResource(R.drawable.game_full);
                        headerRacketIcons[i].setVisibility(View.VISIBLE);
                        headerTexts[i].setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        headerRacketIcons[i].setImageResource(R.drawable.lock);
                        headerRacketIcons[i].setVisibility(View.VISIBLE);
                        headerTexts[i].setTextColor(getResources().getColor(R.color.GREY));
                    }

                    break;
                case 1:
                    headerRacketIcons[i].setVisibility(View.VISIBLE);
                    headerTexts[i].setTextColor(getResources().getColor(R.color.colorPrimary));

                    if (((games.get(i).getPlayer1() != null) && (games.get(i).getPlayer1().equals(username))
                    ) || ((games.get(i).getPlayer2() != null) && (games.get(i).getPlayer2().equals(username)))) {
                        headerRacketIcons[i].setImageResource(R.drawable.half_open);
                    } else {
                        headerRacketIcons[i].setImageResource(R.drawable.half_open);
                    }
                    break;
            }
        }
    }

}
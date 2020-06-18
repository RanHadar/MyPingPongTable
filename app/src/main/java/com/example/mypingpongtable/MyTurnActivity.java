package com.example.mypingpongtable;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
//    MyTurnAdapter myTurnAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    private ArrayList<String> getMyList() {
        ArrayList<String> turns = new ArrayList<>();

        return turns;

    }

}
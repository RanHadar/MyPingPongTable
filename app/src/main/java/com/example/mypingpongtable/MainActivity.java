package com.example.mypingpongtable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionsViewGroupLinearLayout;
import com.maxproj.calendarpicker.Builder;
import com.maxproj.calendarpicker.Models.YearMonthDay;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements Serializable {

    private static final int GAMES_PER_HOUR = Server.MINUTES_IN_HOUR / Server.SLOT_TIME;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;
    private static final int MAX_USERNAME_LEN = 9;

    private int selectedDate;
    private int selectedHour;
//    private Server server;
    private String username;
    private ArrayList<Game> deletedGames;
    private NumberPicker hourPicker;
    private TextView welcomePlayerTxt;
//    private NameDialog nameDialog;
    private Button dateButton;
//    private Button myTurnsBtn;

    float x1, x2;


    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private ImageView[] headerRacketIcons = new ImageView[GAMES_PER_HOUR];
    private Button[] leftJoinButtons = new Button[GAMES_PER_HOUR];
    private Button[] rightJoinButtons = new Button[GAMES_PER_HOUR];

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private ExpansionsViewGroupLinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


    private void connectViewsToXML() {
        hourPicker = findViewById(R.id.hour_picker);

        dateButton = findViewById(R.id.dateButton);

//        myTurnsBtn = findViewById(R.id.savedTurnBtn);

        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);

        slotExpansions[0] = findViewById(R.id.expansionLayout1);
        slotExpansions[1] = findViewById(R.id.expansionLayout2);
        slotExpansions[2] = findViewById(R.id.expansionLayout3);
        slotExpansions[3] = findViewById(R.id.expansionLayout4);

        slotHeaders[0] = findViewById(R.id.slot_header_1);
        slotHeaders[1] = findViewById(R.id.slot_header_2);
        slotHeaders[2] = findViewById(R.id.slot_header_3);
        slotHeaders[3] = findViewById(R.id.slot_header_4);

        headerTexts[0] = findViewById(R.id.header_text1);
        headerTexts[1] = findViewById(R.id.header_text2);
        headerTexts[2] = findViewById(R.id.header_text3);
        headerTexts[3] = findViewById(R.id.header_text4);
    }
}

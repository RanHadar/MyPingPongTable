package com.example.mypingpongtable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity implements Serializable {

    private static final int GAMES_PER_HOUR = Server.MINUTES_IN_HOUR / Server.SLOT_TIME;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;
    private static final int MAX_USERNAME_LEN = 9;

    private int selectedDate;
    private int selectedHour;
    private Server server;
    private String username;
    private ArrayList<Game> deletedGames;
    private NumberPicker hourPicker;
    private TextView welcomePlayerTxt;
    private NameDialog nameDialog;
    private Button dateButton;
    private Button myTurnsBtn;

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
        server = Server.getInstance();

        connectViewsToXML();
        setHourPickerValues();
        setHourPickerListener();
        setDefaultDateAndTime();
        fabricateGames(selectedDate);

        updateHeaders();

        loadSavedUsername();

        if (username == null) {
            launchNameDialog();
        } else {
//            updateExpansions();
            welcomePlayerTxt.setText(getString(R.string.welcome_text, username));
        }

//        makeSlideGesture();
        fabricateGames(selectedDate);
//        slideGestureMaker();
        deletedGames = new ArrayList<Game>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.deletedGames = (ArrayList<Game>) data.getSerializableExtra("deletedGames");
                if (this.deletedGames != null) {
                    for (Game game : deletedGames) {
                        int date = game.getDate();
                        int time = game.getTime();
                        server.removePlayer(date,time,username);
                    }
                }
            }
            updateHeaders();
//            updateExpansions();
        }
    }

    public void moveToMyTurnsActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), MyTurnsActivity.class);
        intent.putExtra("username", this.username);
        intent.putExtra("game_list", server.getPlayerAgenda(username));
        for (int i=0; i<4;i++){
            slotExpansions[i].collapse(true);
        }
        startActivityForResult(intent,1);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void valueChangeAnimate(int oldVal, int newVal) {} //todo - need to create

    @SuppressLint("ClickableViewAccessibility")
    private void slideGestureMaker(){} //todo - need to create


    private void updateHeaders() {
        updateHeaderIcons();
        updateHeaderTimes();
    }

    private void updateHeaders(int i) {
        updateHeaderIcons(i);
        updateHeaderTimes(i);
    }

    private void launchNameDialog() {
        FragmentManager fm = getSupportFragmentManager();
        nameDialog = NameDialog.newInstance("Welcome!");
        nameDialog.show(fm, "fragment_edit_name");
    }


    @SuppressLint("ClickableViewAccessibility")
    private void makeSlideGesture() {

        for (int i = 0; i < 4; i++) {

            slotHeaders[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            gestureCoord1 = event.getY();
                            break;

                        case MotionEvent.ACTION_UP:

                            gestureCoord2 = event.getY();
                            float deltaY = gestureCoord2 - gestureCoord1;
                            ObjectAnimator flip;

                            if (deltaY < -20) {
                                hourPicker.setValue(hourPicker.getValue() + 1);
                                selectedHour = selectedHour + 100;

                                if (selectedHour > 2301) {
                                    selectedHour = 0;
                                }
                                for (int i = 0; i < 4; i++) {
                                    slotExpansions[i].collapse(true);

                                    flip = (ObjectAnimator) AnimatorInflater.loadAnimator(
                                            getApplicationContext(), R.animator.flip_up);

                                    startAnimationUp(i, flip, 5);

                                    updateExpansions();
                                }

                            } else if (deltaY > 20) {
                                flip = (ObjectAnimator) AnimatorInflater.loadAnimator(
                                        getApplicationContext(), R.animator.flip_up);
                                startAnimationDown(flip);

                                updateExpansions();
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * set time picker default value to current time
     */
    private void setDefaultDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String datetime = dateFormat.format(date.getTime());

        selectedDate = Integer.parseInt(datetime);
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        selectedHour = hourPicker.getValue() * Server.INTERVAL;


        // fixes default hour being invisibleArrayList<MyClass> list = (ArrayList<MyClass>)getIntent().getExtras()getSerializable("myClassList");
        View firstItem = hourPicker.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    public void selectDate(View button) {

        Builder builder = new Builder(MainActivity.this, new Builder.CalendarPickerOnConfirm() {
            @Override
            public void onComplete(YearMonthDay date) {

                dateButton.setText(
                        getString(R.string.date_button_text, date.day, date.month, date.year));
                selectedDate = date.year + date.month * 10000 + date.day * 1000000;

                updateHeaderIcons();
//                updateExpansions();
            }
        })
                // todo - we should think about cutting this to another inner method
                // design
                .setPromptText("Select a day to play !")
                .setMonthBaseBgColor(0xF2FCFCFC)
                .setSelectedColor(0xFF284186)
                .setSelectedText("")
                .setConfirmBgColor(0xFF284186)
                .setConfirmColor(0xFFFCFCFC)
                .setSelectedBgColor(0xFFFFFFFF);

        builder.show();
    }



    private void updateHeaderTimes() {

        String[] headerTimes = getResources().getStringArray(R.array.header_times);

        for (int i = 0; i < GAMES_PER_HOUR; i++) {
            headerTexts[i].setText(String.format(headerTimes[i], hourPicker.getValue()));
            headerTexts[i].setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void updateHeaderTimes(int i) {

        String[] headerTimes = getResources().getStringArray(R.array.header_times);
        headerTexts[i].setText(String.format(headerTimes[i], hourPicker.getValue()));
        headerTexts[i].setTypeface(Typeface.DEFAULT_BOLD);

    }
    /**
     * Connect between Objects and XML representation of them
     */
    private void connectViewsToXML() {

        //todo - should seperate to 3 inner methods
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
        headerRacketIcons[0] = findViewById(R.id.racket_icon1);
        headerRacketIcons[1] = findViewById(R.id.racket_icon2);
        headerRacketIcons[2] = findViewById(R.id.racket_icon3);
        headerRacketIcons[3] = findViewById(R.id.racket_icon4);
        leftJoinButtons[0] = findViewById(R.id.join_button_left1);
        leftJoinButtons[1] = findViewById(R.id.join_button_left2);
        leftJoinButtons[2] = findViewById(R.id.join_button_left3);
        leftJoinButtons[3] = findViewById(R.id.join_button_left4);
        rightJoinButtons[0] = findViewById(R.id.join_button_right1);
        rightJoinButtons[1] = findViewById(R.id.join_button_right2);
        rightJoinButtons[2] = findViewById(R.id.join_button_right3);
        rightJoinButtons[3] = findViewById(R.id.join_button_right4);
        linearLayout = findViewById(R.id.slotButtonsLayout);
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

    private void updateHeaderIcons(int i) {
        ArrayList<Game> games = server.getHourAgenda(selectedDate, selectedHour);
        server.saveState();

        headerTexts[i].setTypeface(Typeface.DEFAULT_BOLD);
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
            case 2:
                headerRacketIcons[i].setImageResource(R.drawable.game_open);
                headerRacketIcons[i].setVisibility(View.INVISIBLE);
                headerTexts[i].setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }



    private void setHourPickerValues() {

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//               for (ExpansionLayout e : slotExpansions) {
//                    e.collapse(true);
//               }
                valueChangeAnimate(oldVal, newVal);
                selectedHour = newVal * Server.INTERVAL;
                updateHeaders();
                updateExpansions();
            }
        });


    }
    public void updateExpansions() {
        ArrayList<Game> games = server.getHourAgenda(selectedDate, selectedHour);

        for (int i = 0; i < GAMES_PER_HOUR; i++) {

            updateJoinButton(leftJoinButtons[i], games.get(i).getPlayer1());

            updateJoinButton(rightJoinButtons[i], games.get(i).getPlayer2());
        }
    }


    private void updateJoinButton(Button joinButton, String playerName) {
        Drawable bg = joinButton.getBackground();
        bg = DrawableCompat.wrap(bg);

        if (playerName == null) {
            joinButton.setText(R.string.join_button_init_text);
            joinButton.setTextColor(getResources().getColor(R.color.white));
            DrawableCompat.setTint(bg, getResources().getColor(R.color.com_maxproj_calendarpicker_Green));
            joinButton.setClickable(true);

        } else if (username.equals(playerName)) {
            DrawableCompat.setTint(bg, getResources().getColor(R.color.com_maxproj_calendarpicker_Navy));
            joinButton.setText(playerName);
            joinButton.setTextColor(getResources().getColor(R.color.white));
            joinButton.setClickable(true);

        } else {
            DrawableCompat.setTint(bg, Color.TRANSPARENT);
            joinButton.setText(playerName);
            joinButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            joinButton.setClickable(false);

        }
    }


    private int timeOffset(int buttonId){
        return 0;
    } // todo - create timeOffset method

    private void startAnimationDown(ObjectAnimator flipAnimator) {
        hourPicker.setValue(hourPicker.getValue() - 1);
        selectedHour = selectedHour - 100;
        if (selectedHour < -1) {
            selectedHour = 2300;
        }
        for (int i = 0; i < 4; i++) {
            slotExpansions[i].collapse(true);
//            flipAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flip_down);
            flipAnimator.setTarget(slotHeaders[i]);
            flipAnimator.setStartDelay(i * 100);
            final int finalI = i;
            flipAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    updateHeaders(finalI);

                }
            });
            flipAnimator.start();
        }
    }


    private void startAnimationUp(int headerIndex, ObjectAnimator flipAnimator, int delayMultiplier) {

        flipAnimator.setTarget(slotHeaders[headerIndex]);
        switch (headerIndex) {
            case 0:
                flipAnimator.setStartDelay(60 * delayMultiplier);
                break;
            case 1:
                flipAnimator.setStartDelay(40 * delayMultiplier);
                break;
            case 2:
                flipAnimator.setStartDelay(20 * delayMultiplier);
                break;
            case 3:
                flipAnimator.setStartDelay(0);
                break;
        }
        final int finalI = headerIndex;
        flipAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                updateHeaders(finalI);

            }
        });
        flipAnimator.start();
    }


    /**
     * time picker on value changed listener
     */
    private void setHourPickerListener() {

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//               for (ExpansionLayout e : slotExpansions) {
//                    e.collapse(true);
//               }
                valueChangeAnimate(oldVal, newVal);
                selectedHour = newVal * Server.INTERVAL;
                updateHeaders();
//                updateExpansions();
            }
        });
    }


    private void loadSavedUsername() {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        username = sharedPref.getString(getString(R.string.username), null);


    }


    void fabricateGames(int date) {
        server.addPlayer(date, 1200, "Ran");
        server.addPlayer(date, 1200, "Ruti");
        server.addPlayer(date, 1215, "Or");
        server.addPlayer(date, 1230, "Roey");
        server.addPlayer(date, 1300, "Rom");
        server.addPlayer(date, 1500, "Ran");
        server.addPlayer(date, 1515, "Ruti");
        server.addPlayer(date, 1515, "Or");
        server.addPlayer(date, 1645, "Roey");
        server.addPlayer(date, 1645, "Rom");
        server.addPlayer(date, 1615, "Ran");
        server.addPlayer(date, 1400, "Ruti");
    }
}

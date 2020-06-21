package com.example.mypingpongtable;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionsViewGroupLinearLayout;
import java.io.Serializable;
import java.util.ArrayList;

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
            updateExpansions();
            welcomePlayerTxt.setText(getString(R.string.welcome_text, username));
        }

        makeSlideGesture();

        deletedGames = new ArrayList<Game>();
    }
    }
    private void loadSavedUsername() {}
    private void setHourPickerValues() {}

    private void startAnimationDown(ObjectAnimator flipAnimator) {
        hourPicker.setValue(hourPicker.getValue() - 1);
        selectedHour = selectedHour - 100;
        if (selectedHour < -1) {
            selectedHour = 2300;
        }
        for (int i = 0; i < 4; i++) {
            slotExpansions[i].collapse(true);
            flipAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flip_down);
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
                updateExpansions();
            }
        })
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
                updateExpansions();
            }
        });
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

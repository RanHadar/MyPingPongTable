package com.example.mypingpongtable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
	
	private boolean removePlayerDebug(){
	
        if(player == null){
            return false;
        }
        else if(player.equals(getPlayer1())){
            player1 = null;
        }
        else if(player.equals(getPlayer2())){
            player2 = null;
        }
        if(getPlayer1() == null && getPlayer2() == null){
            server.deleteGame(this);
        }
        return true;

	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

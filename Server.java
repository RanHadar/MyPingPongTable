package com.example.cspingpong;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * A class representing a server of PingPongCS. Designed as a singleton.
 */
class Server implements java.io.Serializable{

    private static Server server = null;
    private ArrayList<Game> game_list;
    static final int INTERVAL = 100;
    static final int MINUTES_IN_HOUR = 60;
    static final int HOURS_IN_DAY = 24;
    static final int SLOT_TIME = 15; //Number of minutes for each slot. make sure it divides 60

    /**
     * Creates an empty server object
     */
    private Server(){
        game_list = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder server_string =
                new StringBuilder(
                        "Server object. The server currently has " + game_list.size() + " games:");
        for (Game g : game_list)
            server_string.append("\n\t").append(g.toString());
        return server_string.toString();
    }

}

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

    /**
     * Returns a server instance. The server instance will be created from available data, if
     * available. Otherwise, a new server will be created, assigned to the class' server variable,
     * and returned
     * @return A server object, either a new on or one loaded from existing data.
     *
     */
    static Server getInstance(){
        if(server == null) {
            try {
                FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") +
                        "/server_data.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                server = (Server) in.readObject();
                server.refreshGames();
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                server = new Server();
            } catch (ClassNotFoundException c) {
                System.out.println("Server class not found");
                c.printStackTrace();
                server = new Server();
            }
        }
        return server;
    }

    /**
     * Refreshes the games in games_list, associating them with the server.
     * This method is used when loading data from files.
     * It is crucial since the server field in the Game object is transient.
     */
    public void refreshGames(){
        for(Game g : game_list){
            g.setServer(this);
        }
    }

    /**
     * Returns a Game object according to a give time and date
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @param hour - a round hour in the format of HHMM or HMM or MM:
     *             ex. 1215 (=12:15), 2330(=23:30), 100(=1:00), 0(=00:00)
     * @return a Game object according to the given data. If there is no such game, returns null
     */
    Game getGame(int date, int hour){
        for (Game g : game_list){
            if(g.getDate() == date && g.getTime() == hour){
                return g;
            }
        }
        return new Game(this, date, hour);
    }

    /**
     * returns the full game_list of the server
     * @return variable game_list
     */
    ArrayList<Game> getGameList(){
        return game_list;
    }

    /**
     * Returns an ArrayList of Games of given date and hour. The size of the ArrayList is the number
     * of game slots that fit in one hour, so if each game is 15 minutes, the returned ArrayList
     * will be of size 4.
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @param hour - a round hour in the format of HHMM or HMM or MM:
     *             ex. 1215 (=12:15), 2330(=23:30), 100(=1:00), 0(=00:00)
     * @return an ArrayList of games of the given hour
     */
    ArrayList<Game> getHourAgenda(int date, int hour){
        ArrayList<Game> game_slots = new ArrayList<>();
        for (int slot = 0; slot < MINUTES_IN_HOUR / SLOT_TIME; slot++){
            boolean found = false;
            for (Game g : game_list){
                if(g.getDate() == date && hour + (SLOT_TIME * slot) == g.getTime()){
                    game_slots.add(g);
                    found = true;
                    break;
                }
            }
            if (!found){
                game_slots.add(new Game(this, date, hour + (SLOT_TIME * slot)));
            }
        }
        return game_slots;
    }


}

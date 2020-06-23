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
                server = (Server) in.readObject();                                                      // Reads the server state from a file we saved earlier to go on from the same state we were in (if we saved one earlier - otherwise it will create a new one)
                server.refreshGames();                                                                  // Sets us to be the server for all the games in the list: game_list
                in.close();object serialization
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                server = new Server();                                                                  // Creates a new server if we don't have a saved one
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
     * Returns an ArrayList of Games of given date and hour in case such games exists on game_list.
     * The size of the ArrayList (number of games will be created) is the number
     * of game slots that fit in one hour, so if each game is 15 minutes, the returned ArrayList
     * will be of size 4 (currently).
     *
     * SO RETURNS NEW ArrayList<Game> with the 4 games according to "date" (each one every 15 min) if exists in game_list,
     * OTHERWISE - Creates the new games in those slots (each one every 15 min)
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

    /**
     * Returns an ArrayList of Games of given date. The size of the ArrayList is the number of game
     * slots that fit in one day, so if each game is 15 minutes, the returned ArrayList will be of
     * size 4 * 24.
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @return an ArrayList of games of the given day
     */
    ArrayList<Game> getDayAgenda(int date){
        ArrayList<Game> game_slots = new ArrayList<>();
        for (int hour = 0; hour < HOURS_IN_DAY; hour++){
            game_slots.addAll(getHourAgenda(date, hour * INTERVAL));                            // If already exists a game on that hour + date use it, otherwise creates new one for that slot
        }
        return game_slots;
    }
                                                                                                      // למה הכל פה נקרא Agenda??
    /**
     * Returns an ArrayList of Games of a given player.
     * @param player the player name to get games of
     * @return an ArrayList of games of all games where player plays
     */
    ArrayList<Game> getPlayerAgenda(String player){
        ArrayList<Game> game_slots = new ArrayList<>();
        if (player == null){
            return game_slots;
        }
        for (Game g : game_list){
            if(player.equals(g.getPlayer1()) || player.equals(g.getPlayer2())){
                game_slots.add(g);
            }
        }
        return game_slots;
    }

    /**
     * Adds a game to the game_list of the server.
     * @param game a Game object to add to the server
     */
    void addGame(Game game){
        game_list.add(game);
    }

    /**
     * NOTICE: you can also join directly to a game, you don't have to use this method.
     * Adds a player to a game. If there is no game at the current date
     * and time, creates a new game and adds the player to it
     * @param date the date of the game in 8 digits, as in 22122019
     * @param time the time of the game in 4 digits, as in 1215
     * @param user the username
     * @return whether the player was successfully added to the game
     */
    boolean addPlayer(int date, int time, String user){
        for (Game g : game_list){
            if(g.getDate() == date && g.getTime() == time){
                return g.addPlayer(user);
            }
        } return game_list.add(new Game(this, date, time, user));
    }

    /**
     * Removes a player from a game \
     * @param date int representing the date of the game to modify
     * @param time int representing the time of the game to modify
     * @param user String representing the username to remove from the game
     * @return true of the user was successfully removed from the game, false otherwise
     */
    boolean removePlayer(int date, int time, String user){
        Game g = getGame(date, time);
        if(g == null)
            return false;
        return g.removePlayer(user);
    }

    /**
     * Removes a game from the games_list
     * @param g a Game object to be removed from the server
     * @return True iff the game was successfully removed from the games_list
     */
    boolean deleteGame(Game g){
        return game_list.remove(g);
    }

    /**
     * Gets a list of games in a given day
     * @param date - an integer in the format DDMMYEAR or DMMYEAR,
     *             ex. 20112019, 1012020
     * @return a list of only NONEMPTY games in the given day, not sorted by time.
     */
    ArrayList<Game> getGamesByDate(int date){
        ArrayList<Game> games_by_date = new ArrayList<>();
        for (Game g : game_list){
            if(g.getDate() == date){
                games_by_date.add(g);
            }
        }
        return games_by_date;
    }

    /**
     * Saves the server state in a file, server_data.ser
     */
    void saveState(){
        System.out.println(System.getProperty("user.dir")+"/server_data.ser");

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(System.getProperty("user.dir")+"/server_data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
            System.out.println("!!!!!!!!!!!!!!");
            System.out.println(System.getProperty("user.dir")+"/server_data.ser");

        }
    }

    /**
     * Resets the server. After calling this method, you should call user getInstance again.
     */
    void reset(){
        game_list = new ArrayList<>();
        server = null;
    }
}

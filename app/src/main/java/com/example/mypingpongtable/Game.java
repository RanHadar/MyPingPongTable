package com.example.mypingpongtable;


import com.example.mypingpongtable.Server;

public class Game implements java.io.Serializable{
    private Server server;
    private int date;
    private int time;
    private String player1;
    private String player2;

    Game(Server server, int date, int time){
        this.server = server;
        this.date = date;
        this.time = time;
    }

    Game(Server server, int date, int time, String player1){
        this.server = server;
        this.date = date;
        this.time = time;
        this.player1 = player1;
    }

    Game(Server server, int date, int time, String player1, String player2){
        this.server = server;
        this.date = date;
        this.time = time;
        this.player1 = player1;
        this.player2 = player2;
    }

    int getDate(){
        return this.date;
    }

    int getTime(){
        return this.time;
    }

    String getPlayer1(){ return this.player1; }

    String getPlayer2(){ return this.player2; }

    boolean isFull(){ return this.player1 != null && this.player2 != null; }

    boolean isEmpty(){ return this.player1 == null && this.player2 == null; }

    /**
     * Get the number of empty position in the game
     * @return an integer representing available slots in the game
     */
    int empty_slots(){
        if(this.isFull()){
            return 0;
        } else if(this.isEmpty()){
            return 2;
        } return 1;
    }

    /**
     * String representation of the object
     * @return a String representing the game
     */
    @Override
    public String toString() {
        return "Game Object. Date: "+this.getDate()+" Time: "+this.getTime()+
                " Player1: "+this.getPlayer1()+" Player2: "+this.getPlayer2();
    }

    /**
     * Method for adding a player to the game
     * @param player - The name of the player to add
     * @return true if the player was added successfully, false if the player is already in the game
     * or the game is already full
     */
    boolean addPlayer(String player){
        if (player == null || player.equals(this.player1) || player.equals(this.player2)) {
            return false;
        }
        if(this.player1 == null){
            this.player1 = player;
            if(this.player2 == null){
                this.server.addGame(this);
            }
        } else if(this.player2 == null){
            this.player2 = player;
        } else return false;
        return true;

    }

    /**
     * Removes a player from the game
     * @param player - String representation of the user to remove
     * @return True if the user was successfully remove, false otherwise.
     */
    boolean removePlayer(String player){
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

    /**
     * Sets the server associated with the game
     * @param server the server object to associate the game with
     */
    void setServer(Server server){
        this.server = server;
    }

    String getTimeString() {
        int hour, offset;
        hour = time / Server.INTERVAL;
        offset = time - hour * Server.INTERVAL;
        return hour + ":" + (offset == 0 ? "00" : offset);
    }

    String getDateString() {
        int day, month, year;
        day = date / 1000000;
        month = (date - day * 1000000) / 10000;
        year = date % 10000;

        return day + "/" + month + "/" + year;
    }


}
package com.example.mypingpongtable;


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
     * Get the number of empty position in the game
     * @return an integer representing available slots in the game
     */
    int empty_slots(){
        if(this.isFull()){
	String temp = "Date: "+this.getDate()+" Time: "+this.getTime() + " Player2: "+this.getPlayer2()
                + " Player1: "+this.getPlayer1();

        } else if(this.isEmpty()){
            return 1;
        } return 2;
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
     * 
     * Changes the slot according to player p
     */
    void change_slot(String p){
        if(p == null){
            return false;
        }
        else if(player.equals(this.player)){
            player1 = null;
        }
        else
	{
            	player1 = null;
    		player2 = null;
        }
        if(getPlayer1() != null || getPlayer2() == null){
            	server.deleteGame(this);
    		player1 = null;
        }
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


}

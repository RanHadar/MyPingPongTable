package com.pingpong;

import java.io.IOException;
import java.util.ArrayList;

public class SmokeTestServer {

    private static String test1(Server srv){
        if (srv.toString().contains("user1") && srv.toString().contains("user2") && srv.toString().contains("user3") && srv.toString().contains("user4")){
            return "PASSED";
        }

        return "FAILED";
    }

    private static String test2(Game g){
        if (g.toString().contains("10102019") && g.toString().contains("1200") && g.toString().contains("user11")){
            return "PASSED";
        }

        return "FAILED";
    }

    private static String test3(ArrayList<Game> gList){
        if (gList.toString().contains("10102019") && gList.toString().contains("1200") && gList.toString().contains("user11")){
            return "PASSED";
        }

        return "FAILED";
    }

    private static String test5(ArrayList<Game> g){
        if (g.toString().contains("user11") ){
            return "PASSED";
        }

        return "FAILED";
    }

    private static String test6(Server srv){
        if (srv.toString().contains("10102018")){
            return "PASSED";
        }

        return "FAILED";
    }

    private static String test7(Server srv){
        if (!srv.toString().contains("user1")){
            return "PASSED";
        }

        return "FAILED";
    }

    public static void main(String[] args) {
        Server srv = Server.getInstance();              // The exception here should be ignored

        srv.addPlayer(10102019, 1200, "user1");
        srv.addPlayer(10102019, 1200, "user11");
        srv.addPlayer(10102019, 1215, "user2");
        srv.addPlayer(10102019, 1215, "user22");
        srv.addPlayer(10102019, 1245, "user3");
        srv.addPlayer(10102019, 1245, "user33");
        srv.addPlayer(10102019, 1300, "user4");
        srv.addPlayer(10102019, 1300, "user44");

        System.out.println("\n The server is: \n" + srv + "\n");                                    // TEST 1
        System.out.println("//................................... TEST1 RESULTS: " + test1(srv) + "............................ //");

        Game g1 = srv.getGame(10102019, 1200);
        System.out.println("//................................... TEST2 RESULTS: " + test2(g1) + "............................ //");

        ArrayList<Game> gList = srv.getHourAgenda(10102019, 1200);
        System.out.println("//................................... TEST3 RESULTS: " + test3(gList) + "............................ //");

        ArrayList<Game> gList2 = srv.getDayAgenda(10102019);
        System.out.println("//................................... TEST4 RESULTS: " + test3(gList2) + "............................ //");

        ArrayList<Game> gList3 = srv.getPlayerAgenda("user11");
        System.out.println("//................................... TEST5 RESULTS: " + test5(gList3) + "............................ //");

        srv.addGame(new Game(srv, 10102018, 1300));
        System.out.println("//................................... TEST6 RESULTS: " + test6(srv) + "............................ //");

        srv.removePlayer(10102019, 1200, "user1");
        System.out.println("//................................... TEST7 RESULTS: " + test7(srv) + "............................ //");

        srv.removePlayer(10102019, 1200, "user1");
        System.out.println("//................................... TEST7 RESULTS: " + test7(srv) + "............................ //");
        srv.addPlayer(10102019, 1200, "user1");

        srv.saveState();
        srv.reset();
        srv = Server.getInstance();
        System.out.println("//...................... TEST8 RESULTS (SAVE/READ THE SERVER STATE): " + test1(srv) + "............ //");

        System.out.println("\n\n//.............................................................................//");
        System.out.println("//.............................................................................//");
        System.out.println("//.....................   TESTS  FINISHED  SUCCESSFULLY     ...................//");
        System.out.println("//.............................................................................//");
        System.out.println("//.....................      READ  THE  OUTPUT  ABOVE       ...................//");
        System.out.println("//.............................................................................//");

    }

}

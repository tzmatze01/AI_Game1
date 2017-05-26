import java.io.File;
import java.io.IOException;

import game.Game;
import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;
import model.Stone;

import javax.imageio.ImageIO;


public class Main {

    private final static int DEPTH = 3;

    public static void main(String args[]) throws InterruptedException {

        // java -Djava.library.path=lib/native -jar bogapr.jar

        /*
        Game game = new Game(Stone.RED, 3);
        game.moveStone(new Move(0,1, 0, 4));

        long start = System.currentTimeMillis();

        Move move = game.calculateBestMove();


        System.out.println("in "+(System.currentTimeMillis()-start)/1000+" s has value of:"+move.fromX+":"+move.fromY+" -> "+move.toX+":"+move.toY);
        */


        try {
            NetworkClient networkClient = new NetworkClient(null, "PLAYER", ImageIO.read(new File("meinLogo.png")));

            int networkLateny = networkClient.getExpectedNetworkLatencyInMilliseconds();
            int timeLimit = networkClient.getTimeLimitInSeconds();
            int playerNumber = networkClient.getMyPlayerNumber();


            // TODO: fix order of playernumber -> stone color
            Stone playerColor = (playerNumber == 0) ? Stone.RED :
                                (playerNumber == 1) ? Stone.GREEN : Stone.BLUE;

            Game game = new Game(playerColor, DEPTH);
            long sleepTime = (timeLimit*1000)-(networkLateny*2);

            System.out.println("sleeptime: "+sleepTime);

            long startTime = 0;
            long endTime = 0;



            Thread timeThread = new Thread () {

                @Override
                public void run()  {


                    try {
                        //game.calculateBestMove();

                        // TODO uncomment for real game
                        //sleep(sleepTime);
                        sleep(sleepTime-500);
                    }
                    catch(InterruptedException e) {
                        System.out.println(System.currentTimeMillis());
                        e.printStackTrace();
                    }
                    finally {
                        /*
                        System.out.println("Retrieve solution from Game");
                        Move move = game.getNextMove();
                        System.out.println("sending from: "+move.fromX+" "+move.fromY+" to: "+move.toX+" "+move.toY);

                        networkClient.sendMove(move);

                        // persist move to gameboard
                        game.moveStone(move);
                        */
                    }

                    
                }
            };

            timeThread.interrupt();

            System.out.println("Stone color is: "+game.getStoneColor());

            for (; ; ) {
                Move receiveMove;
                Move makeMove;

                while ((receiveMove = networkClient.receiveMove()) != null) {

                    System.out.println("received move: fromX="+ receiveMove.fromX+" fromY="+ receiveMove.fromY+" toX="+receiveMove.toX+" toY="+receiveMove.toY);
                    //Zug in meine Brettrepräsentation einarbeiten
                    game.moveStone(receiveMove);
                }


                startTime = System.currentTimeMillis();

                timeThread.start();

                while((makeMove = game.calculateBestMove() ) ) {

                    System.out.println("while");
                }



            }

        }
        catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Connection ended!", e);
        }
    }

        /*
        TODO:

        Bewertungfkt.
        Spielbaum Züge: Gegnerzug darf zweimal ziehen, dan zwei Gegner - in Spielbaum nur ein Gegner
        Zeitabfrage - Threads
         */
}

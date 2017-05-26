
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import game.Game;
import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;
import model.Player;
import model.Stone;

import javax.imageio.ImageIO;


public class Main {

    private final static int DEPTH = 3;

    public static void main(String args[]) throws InterruptedException {

        try {
            String hostName = "localhost";
            String playerName = "Player";
            BufferedImage playerImage = ImageIO.read(new File("meinLogo.png"));

            for (int i = 1; i <= 3; i++) {
                playerName = playerName + " " + i;
                Player player = new Player(hostName, playerName, playerImage);
                Thread playerThread = new Thread(player);
                playerThread.start();
            }

        }
        catch(IOException exc) {

        }


        // java -Djava.library.path=lib/native -jar bogapr.jar
        /*
        try {
            NetworkClient networkClient = new NetworkClient(null, "PLAYER 3", ImageIO.read(new File("meinLogo.png")));

            int networkLateny = networkClient.getExpectedNetworkLatencyInMilliseconds();
            int timeLimit = networkClient.getTimeLimitInSeconds();
            int playerNumber = networkClient.getMyPlayerNumber();

            Stone playerColor = (playerNumber == 0) ? Stone.RED :
                                (playerNumber == 1) ? Stone.GREEN : Stone.BLUE;

            Game game = new Game(playerColor, DEPTH);
            long sleepTime = (timeLimit*1000)-(networkLateny*2);

            System.out.println("sleeptime: "+sleepTime);


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

                        System.out.println("Retrieve solution from Game");
                        Move move = game.getNextMove();
                        System.out.println("sending from: "+move.fromX+" "+move.fromY+" to: "+move.toX+" "+move.toY);

                        networkClient.sendMove(move);

                        // persist move to gameboard
                        game.moveStone(move);
                    }

                    
                }
            };

            timeThread.interrupt();

            System.out.println("Stone color is: " + game.getStoneColor());

            for (; ; ) {
                Move receiveMove;
                Move makeMove;

                while ((receiveMove = networkClient.receiveMove()) != null) {

                    System.out.println("received move: fromX="+ receiveMove.fromX+" fromY="+ receiveMove.fromY+" toX="+receiveMove.toX+" toY="+receiveMove.toY);
                    //Zug in meine Brettrepräsentation einarbeiten
                    game.moveStone(receiveMove);
                }

                timeThread.start();


            }

        }
        catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Connection ended!", e);
        }
        **/
    }
}

package model;

import game.Game;
import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

/**
 * Created by Matthias Daiber und Felix Schwanke on 26.05.2017.
 */
public class Player extends Observable implements Runnable {

    private final static int DEPTH = 3;

    private boolean end = false;

    private String hostName;
    private String playerName;
    private BufferedImage playerImage;

    public Player(String hostName, String playerName, BufferedImage playerImage) {
        this.hostName = hostName;
        this.playerName = playerName;
        this.playerImage = playerImage;
    }

    public void run() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        NetworkClient networkClient = null;

        try {
            networkClient = new NetworkClient(null, "PLAYER 1", ImageIO.read(new File("meinLogo.png")));
        }
        catch (IOException exc) {

        }

        int networkLateny = networkClient.getExpectedNetworkLatencyInMilliseconds();
        int timeLimit = networkClient.getTimeLimitInSeconds();
        int playerNumber = networkClient.getMyPlayerNumber();

        Stone playerColor = (playerNumber == 0) ? Stone.RED :
                (playerNumber == 1) ? Stone.GREEN : Stone.BLUE;

        Game game = new Game(playerColor, DEPTH);
        long sleepTime = (timeLimit*1000)-(networkLateny*2);

        System.out.println("sleeptime: "+sleepTime);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Move receiveMove;
        Move makeMove;

        do {
            try {
                while ((receiveMove = networkClient.receiveMove()) != null) {

                    System.out.println("received move: fromX="+ receiveMove.fromX+" fromY="+ receiveMove.fromY+" toX="+receiveMove.toX+" toY="+receiveMove.toY);
                    //Zug in meine Brettrepräsentation einarbeiten
                    game.moveStone(receiveMove);
                }

                makeMove = game.calculateBestMove();
                networkClient.sendMove(makeMove);

                // persist move to gameboard
                game.moveStone(makeMove);

            }
            catch (RuntimeException ex) {
                ex.printStackTrace();
                end = true;
            }

        }
        while(!end);
    }
}

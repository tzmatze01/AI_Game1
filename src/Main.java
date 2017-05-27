
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.Player;

import javax.imageio.ImageIO;


public class Main {

    public static void main(String args[]) throws InterruptedException {

        try {
            String hostName = "localhost";

            BufferedImage playerImage = ImageIO.read(new File("icon.png"));

            for (int i = 1; i <= 3; i++) {
                String playerName = "Player " + i;
                Player player = new Player(hostName, playerName, playerImage);
                Thread playerThread = new Thread(player);
                playerThread.start();
            }

        }
        catch(IOException exc) {

        }

    }
}

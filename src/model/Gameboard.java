package model;

import lenz.htw.bogapr.Move;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthias Daiber & Felix Schwanke on 09.05.17.
 */
public class Gameboard {

    // TODO internal representation of occupied fields, to decrease computation time

    // Für Bericht: beschreiben wie es zu dieser Struktur kam,
    private Map<Integer, StoneStack> gameboard;

    public Gameboard() {

        this.gameboard = initGameBoard();
    }

    public synchronized void moveStone(Move move) {

        // TODO keine Berücksichtigung ob fromX valide ist
        Stone stone = this.gameboard.get((move.fromX*100)+move.fromY).removeTop();

        // remove stack if this was the last stone
        if(this.gameboard.get((move.fromX*100)+move.fromY).isEmpty())
            this.gameboard.remove((move.fromX*100)+move.fromY);

        // store Stone in StoneStack array, if this not exists create a new one
        if(this.gameboard.get((move.toX*100) + move.toY) == null) {
            StoneStack tempStack = new StoneStack();
            tempStack.addStone(stone);

            this.gameboard.put(((move.toX * 100) + move.toY), tempStack);
        }
        else
            this.gameboard.get((move.toX*100) + move.toY).addStone(stone);

    }

    public Map<Integer, StoneStack> getMovableStonesForColor(Stone color) {

        Map<Integer, StoneStack> movableStones = new HashMap<>();

        for(int key : this.gameboard.keySet()) {
            if(this.gameboard.get(key).getTopColor() == color)
                movableStones.put(key, this.gameboard.get(key));
        }

        return movableStones;
    }

    protected int stonesOnField(int field) {

        if(this.gameboard.get(field) == null)
            return 0;

        return this.gameboard.get(field).getSize();
    }

    protected Stone colorOfField(int field) {

        if(this.gameboard.get(field) == null)
            return Stone.NONE;

        return this.gameboard.get(field).getTopColor();
    }

    protected void printGameboard() {

        int sum = 0;

        for(int key : this.gameboard.keySet()) {

            StoneStack stack = gameboard.get(key);

            System.out.println(key+"     "+stack.toString());

            sum += gameboard.get(key).getSize();
        }
        System.out.println(sum);

        if(sum != 27)
            throw new ArrayIndexOutOfBoundsException();
    }

    protected Map<Integer, StoneStack> getGameboard() {

        return this.gameboard;
    }

    protected void setGameboard(Map<Integer, StoneStack> gameboard) {

        this.gameboard = gameboard;
    }

    public Map<Integer, StoneStack> initGameBoard() {

        Map<Integer, StoneStack> newGameboard = new HashMap<>();

        // populate stone stacks given at game start
        StoneStack red1 = new StoneStack(Stone.RED, Stone.RED, Stone.RED);
        StoneStack red2 = new StoneStack(Stone.RED, Stone.RED, Stone.RED);
        StoneStack red3 = new StoneStack(Stone.RED, Stone.RED, Stone.RED);

        StoneStack green1 = new StoneStack(Stone.GREEN, Stone.GREEN, Stone.GREEN);
        StoneStack green2 = new StoneStack(Stone.GREEN, Stone.GREEN, Stone.GREEN);
        StoneStack green3 = new StoneStack(Stone.GREEN, Stone.GREEN, Stone.GREEN);

        StoneStack blue1 = new StoneStack(Stone.BLUE, Stone.BLUE, Stone.BLUE);
        StoneStack blue2 = new StoneStack(Stone.BLUE, Stone.BLUE, Stone.BLUE);
        StoneStack blue3 = new StoneStack(Stone.BLUE, Stone.BLUE, Stone.BLUE);

        newGameboard.put(1, red1);
        newGameboard.put(101, red2);
        newGameboard.put(201, red3);

        newGameboard.put(106, green1);
        newGameboard.put(206, green2);
        newGameboard.put(5, green3);

        newGameboard.put(1006, blue1);
        newGameboard.put(1106, blue2);
        newGameboard.put(1005, blue3);

        return newGameboard;
    }
}

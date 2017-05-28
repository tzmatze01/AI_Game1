package game;

import lenz.htw.bogapr.Move;
import model.Gameboard;
import model.Stone;
import model.StoneStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthias Daiber & Felix Schwanke on 12.05.17.
 */
public class Game extends Gameboard {

    private Stone color;
    private Stone[] enemyColors;
    private boolean stopCalculation;
    private int currentBest;

    private Move nextMove;

    private final int DEPTH;

    public Game(Stone color, int depth) {

        this.color = color;
        this.stopCalculation = false;   
        this.currentBest = -99999;
        this.DEPTH = depth;

        this.enemyColors = (this.color == Stone.RED) ? new Stone[]{Stone.BLUE, Stone.GREEN} :
                            (this.color == Stone.BLUE) ? new Stone[]{Stone.RED, Stone.GREEN} : new Stone[]{Stone.RED, Stone.BLUE};
    }

    public Move calculateBestMove() {

        // TODO sort set to get stones with highest jump length
        Map<Integer, StoneStack> movableStones = getMovableStonesForColor(getStoneColor());

        // calculate best move for one of all movable stones
        for(int stone : movableStones.keySet()) {

            Set<Integer> stoneSet = new HashSet<>();
            stoneSet.add(stone);

            Set<Integer> jumpFields = null;
            try {
                System.out.println("Movable stones size: " + movableStones.size());
                System.out.println("For stack:"+stone+" jumplength is: "+movableStones.get(stone).getSize()+" content: "+movableStones.get(stone).toString());
                jumpFields = generateFields(movableStones.get(stone).getSize(), stoneSet); //????
            }
            catch (StackOverflowError exc) {
                exc.printStackTrace();
            }

            if (jumpFields == null) {
                System.out.println("ERROR: jumpFields ist null");
            }

            // iterate over all possible jumps for stone
            if (jumpFields != null) {
                for (int field : jumpFields) {

                    // TODO alpha mitgeben !
                    int moveScore = alphaBetaMAX(new Move(stone / 100, stone % 100, field / 100, field % 100), DEPTH, -9999, 9999);

                    System.out.println("moveScore from " + stone + " to " + field + " for:" + getStoneColor() + " is: " + moveScore);

                    // if the value of the returned move is higher than the current, save the move
                    if (this.currentBest < moveScore) {
                        this.currentBest = moveScore;
                        this.nextMove = new Move(stone / 100, stone % 100, field / 100, field % 100);
                    }

                    if (stopCalculation)
                        break;
                }
            }

            if(stopCalculation)
                break;
        }
        System.out.println("ended calculation, return move: "+ nextMove.fromX+":"+nextMove.fromY+" -> "+nextMove.toX+":"+nextMove.toY);


        this.currentBest = -99999;

        return nextMove;
    }

    // TODO toggle ownField
    // DONE : Moves are konsistent on gameboard
    private int alphaBetaMAX(Move move, int depth, int alpha, int beta) {

        //System.out.println("MAX called with: "+move.fromX+":"+move.fromY+" -> "+move.toX+":"+move.toY+" depth:"+depth+" alpha:"+alpha+" beta:"+beta);

        // initialise value of node with minues infinity
        int value = -9999;

        if(depth == 0) {
            int xx = evaluate((move.toX * 100) + move.toY, true);
            //System.out.println(System.currentTimeMillis()+" value of:"+move.fromX+":"+move.fromY+" -> "+move.toX+":"+move.toY+" is "+xx);
            return xx;
        }

        else {

            // -----------------------------------------------------------------------------------------------------------------------
            // Enemy 1

            // persist given move to gameboard
            moveStone(move);

            // TODO could be problematic if storage does not get overwritten?
            // generate all possible jumps for enemy 1
            Map<Integer, StoneStack> msEnemy1 = getMovableStonesForColor(enemyColors[0]);

            for (int s1 : msEnemy1.keySet()) {

                Set<Integer> e1StoneSet = new HashSet<>();
                e1StoneSet.add(s1);

                Set<Integer> jfEnemy1 = null;

                try {

                    jfEnemy1 = generateFields(msEnemy1.get(s1).getSize(), e1StoneSet);
                }
                catch (StackOverflowError exc) {
                    exc.printStackTrace();
                }

                if (jfEnemy1 != null) {

                    // iterate over every generated field to make a game tree
                    for (int jf1 : jfEnemy1) {

                        // -----------------------------------------------------------------------------------------------------------------------
                        // Enemy 2

                        // persist given move to gameboard
                        moveStone(new Move(s1 / 100, s1 % 100, jf1 / 100, jf1 % 100));

                        // generate all possible jumps for enemy 2
                        Map<Integer, StoneStack> msEnemy2 = getMovableStonesForColor(enemyColors[1]);

                        for (int s2 : msEnemy2.keySet()) {

                            Set<Integer> e2StoneSet = new HashSet<>();
                            e2StoneSet.add(s2);

                            Set<Integer> jfEnemy2 = null;
                            try {

                                jfEnemy2 = generateFields(msEnemy2.get(s2).getSize(), e2StoneSet);
                            } catch (StackOverflowError exc) {
                                exc.printStackTrace();
                            }

                            if (jfEnemy2 != null) {

                                int newDepth = depth - 1;

                                // iterate over every generated field to make a game tree
                                for (int jf2 : jfEnemy2) {


                                    int score = alphaBetaMIN(new Move(s2 / 100, s2 % 100, jf2 / 100, jf2 % 100), newDepth, value, beta);

                                    //System.out.println("MAX evaluate score:"+score+" > value:"+value);

                                    if (score > value)
                                        value = score;

                                    // pruning - stop calculation if value is greater than beta
                                    if (value > beta)
                                        break;
                                }
                            }
                        }
                        // undo my own move on gameboard for enemy 2
                        moveStone(new Move(jf1 / 100, jf1 % 100, s1 / 100, s1 % 100));
                    }
                }
            }

        }
        // undo my own move on gameboard
        moveStone(new Move(move.toX, move.toY, move.fromX, move.fromY));

        return value;
    }

    private int alphaBetaMIN(Move move, int depth, int alpha, int beta) {

        // initialise value of node with plus infinity
        int value = 9999;

        if(depth <= 0) {
            int xx = evaluate((move.toX * 100) + move.toY, false);
            return xx;
        }
        else {

            // persist given move to gameboard
            moveStone(move);

            // TODO could be problematic if storage does not get overwritten?

            // TODO last moved stone is missing
            // generate all possible jumps
            Map<Integer, StoneStack> movableStones = getMovableStonesForColor(getStoneColor());

            for(int stone : movableStones.keySet()) {

                Set<Integer> stoneSet = new HashSet<>();
                stoneSet.add(stone);

                Set<Integer> jumpFields = generateFields(movableStones.get(stone).getSize(), stoneSet);

                int newDepth = depth - 1;

                // iterate over every generated field to make a game tree
                for(int jumpField : jumpFields) {

                    // check if jumpField is occupied by full stack
                    //if(this.getGameboard().get(field).getSize() < 3)

                    int score = alphaBetaMAX(new Move(stone/100, stone%100, jumpField/100, jumpField%100), newDepth, alpha, value);

                    if(score < value)
                        value = score;

                    // pruning - stop calculation if value is higher than beta
                    if(value < alpha)
                        break;
                }
            }
        }
        // undo my own move on gameboard for enemy 1
        moveStone(new Move(move.toX, move.toY, move.fromX, move.fromY));

        return value;
    }

    public Set<Integer> generateFields(int depth, Set<Integer> moves) {

         /*
        Only the moves from the last iteration CAN be valid moves.
        To filter out the invalid moves from the last iteration,
        the generated moves from the previous iterations must be removed from the result set.
         */

        Set<Integer> tempFields = new HashSet<>();

        if(depth <= 1) {

            for(int move : moves)
                tempFields.addAll(getNeighborFields(move));

            tempFields.removeAll(moves);

            moves.clear();

            // only add where are no full stone stacks
            for (int field : tempFields) {
                if(this.getGameboard().get(field) != null) {
                    if(this.getGameboard().get(field).getSize() < 3)
                        moves.add(field);
                }
                else
                    moves.add(field);

            }

            return moves;
        }
        else {

            for(int move : moves)
                tempFields.addAll(getNeighborFields(move));

            moves.addAll(tempFields);

            return generateFields(--depth, moves);
        }
    }

    // look at surrounding fields and evaluate field with this
    public int evaluate(int playerField, boolean ownField) {

         /*
         Value of fields

         A field next to me has - 0 stones          = 50
                                - 1 enemy stone     = 1
                                - 2 enemy stones    = 80
                                - 3 enemy stones    = 1
                                - N own stones      = 20
                                - the goal          = 100
          */

        int tmp = 0; int fieldFactor = 0;

        // get all surrounding fields to player field
        for(int field : getNeighborFields(playerField)) {

            //if(colorOfField(field) == getStoneColor()) return 20;

            switch(stonesOnField(field)) {

                case 0:
                    if(isGoal(field)) {
                        tmp = 100;
                        break;
                    }
                    tmp = 50;
                    break;
                case 1:
                    tmp = 1;
                    break;
                case 2:
                    tmp = 80;
                    break;
                case 3:
                    // cannot move on an full field
                    tmp = 1;
                    break;
            }

            // store the field with the highest factor
            if(fieldFactor < tmp) fieldFactor = tmp;
        }

        // if ownField is negative, tmp get's negated, because we are in a MIN node
        if(ownField) tmp = tmp * -1;

        return tmp;
    }

    private Set<Integer> getNeighborFields(int field) {

        Set<Integer> fields = new HashSet<>();

        // when x is even, y can be increased
        if(((field/100)%2) == 0) {
            // when gping up x & y have to be increased
            if(isValidField(field+101))
                fields.add(field+101);

            // horizontal
            if(isValidField(field+100))
                fields.add(field+100);
            if(isValidField(field-100))
                fields.add(field-100);
        }

        // when x is odd, y can be decreased
        else {
            // when going down x & y have to be decreased
            if(isValidField(field-101))
                fields.add(field-101);

            // horizontal
            if(isValidField(field+100))
                fields.add(field+100);
            if(isValidField(field-100))
                fields.add(field-100);
        }
        return fields;
    }

    // TODO: This function is only valid for this game!
    private boolean isValidField(int field) {

        int x = field / 100;
        int y = field % 100;

        // min - max values for x & y
        if(x > 11 || x < 0 || y < 1 || y > 6)
            return false;
        else {
            if(x > (2*y))
                return false;
            else
                return true;
        }

    }

    // TODO check if hard coded goals are reasonable
    private boolean isGoal(int field) {

        int x = field / 100;
        int y = field % 100;

        switch (this.color) {

            case RED:
                if(y == 6 && x > 2 && x < 10)
                    return true;
                break;
            case GREEN:
                if(y > 1 && y < 5 && x > 2 && x < 10)
                    return true;
                break;
            case BLUE:
                if(y > 1 && y < 6 && x == 0)
                    return true;
                break;
        }

        return false;
    }

    public Move getNextMove() {

        stopCalculation = true;
        return nextMove;
    }


    public Stone getStoneColor() {
        return this.color;
    }

}

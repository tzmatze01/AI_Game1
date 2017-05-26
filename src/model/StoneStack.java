package model;

public class StoneStack {

    private Stone stack[];

    public StoneStack() {

        this.stack = new Stone[]{null, null, null};
    }

    public StoneStack(Stone first, Stone second, Stone third) {

        this.stack = new Stone[]{first, second, third};
    }

    // returns null if already empty
    public Stone removeTop() {

        Stone temp = this.stack[getSize()-1];
        this.stack[getSize()-1] = null;

        return temp;
    }

    public void addStone(Stone newStone) {

        // TODO error handling - see Gameb:generateFields:239-246
        this.stack[getSize()] = newStone;
    }

    public  Stone getTopColor() {
        return this.stack[getSize()-1];
    }

    public int getSize() {

        int size = 0;

        for(Stone x : stack) {
            if(x != null)
                ++size;
        }

        return size;
    }

    public boolean isEmpty() {

        return (this.stack[0] == null);
    }

    public String toString() {

        return "" + stack[0] + ", " + stack[1] + ", " + stack[2];
    }

}

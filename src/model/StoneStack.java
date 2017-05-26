package model;

public class StoneStack {

    private Stone stack[];
    private int index;

    public StoneStack() {
        this.stack = new Stone[]{null, null, null};
        this.index = 0;
    }

    public StoneStack(Stone first, Stone second, Stone third) {
        this.stack = new Stone[]{first, second, third};
        this.index = 2;
    }


    // returns null if already empty
    public Stone removeTop() {

        Stone temp = this.stack[this.index];
        this.stack[this.index] = null;

        // gets negative eventually, but does not concern because StoneStack gets deleted in this case
        --this.index;

        return temp;
    }

    public void addStone(Stone newStone) {


        if((getSize()-1) == 2) {}

        else
            this.stack[getSize()-1] = newStone;
            ++this.index;

        // TODO FIX !!!
        if(this.index ==  3)
            this.index = 2;


    }

    public  Stone getTopColor() {
        return this.stack[this.index];
    }
    public int getSize() {
        int size = 0;

        for(Stone x : stack)
            ++size;

        return size;
    }
    public boolean isEmpty() {
        return (this.stack[0] == null);
    }

    public String toString() {
        return ""+stack[0]+", "+stack[1]+", "+stack[2];
    }

}

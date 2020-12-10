package main.java.c0.util;

public class Pos {

    public int row;
    public int col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Pos nextCol() {
        return new Pos(row, col + 1);
    }

    public Pos nextRow() {
        return new Pos(row + 1, 0);
    }

    @Override
    public String toString() {
        // "Pos(row: row, col: col)"
        return new StringBuilder().append("Pos(row: ").append(row).append(", col: ").append(col).append(")").toString();
    }
}

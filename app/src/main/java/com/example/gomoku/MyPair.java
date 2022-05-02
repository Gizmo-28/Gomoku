package com.example.gomoku;

import java.io.Serializable;

public class MyPair implements Serializable {
    private int row;
    private int column;

    public MyPair(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}

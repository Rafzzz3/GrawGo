package com.example;

import java.io.Serializable;

public class Stone implements Serializable {
    private StoneColor color;
    private Stone top;
    private Stone bottom;
    private Stone left;
    private Stone right;

    public Stone(StoneColor color) {
        this.color = color;
    }

    public void setNeighbors(Stone top, Stone bottom, Stone left, Stone right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    // Gettery
    public Stone getTop() {
        return top;
    }
    public Stone getBottom() {
        return bottom;
    }
    public Stone getLeft() {
        return left;
    }
    public Stone getRight() {   
        return right;
    }

    public StoneColor getColor() {
        return color;
    }
}

package com.example.demo155.Model;

public class Led {
    public enum Color {
        Red, Yellow, Green
    }

    private Color color;
    private int duration;

    public Led(Color color, int duration) {
        this.color = color;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public Color getColor() {
        return color;
    }
}
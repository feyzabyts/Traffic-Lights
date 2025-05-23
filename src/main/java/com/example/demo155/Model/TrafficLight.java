package com.example.demo155.Model;

public class TrafficLight {
    private Led redLed;
    private Led greenLed;
    private Led yellowLed;

    public TrafficLight(int greenDuration, int redDuration, int yellowDuration) {
        this.greenLed = new Led(Led.Color.Green, greenDuration);
        this.redLed = new Led(Led.Color.Red, redDuration);
        this.yellowLed = new Led(Led.Color.Yellow, yellowDuration);
    }

    public Led getGreenLed() {
        return greenLed;
    }

    public Led getRedLed() {
        return redLed;
    }

    public Led getYellowLed() {
        return yellowLed;
    }
}
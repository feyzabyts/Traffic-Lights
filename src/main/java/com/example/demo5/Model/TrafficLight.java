package com.example.demo5.Model;

public class TrafficLight {
    private com.example.demo5.Model.Led redLed;
    private com.example.demo5.Model.Led greenLed;
    private com.example.demo5.Model.Led yellowLed;

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
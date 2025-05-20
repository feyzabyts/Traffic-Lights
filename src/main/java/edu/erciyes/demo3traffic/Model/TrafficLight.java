package edu.erciyes.demo3traffic.Model;

public class TrafficLight {
    private edu.erciyes.demo3traffic.Model.Led redLed;
    private edu.erciyes.demo3traffic.Model.Led greenLed;
    private edu.erciyes.demo3traffic.Model.Led yellowLed;

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
package com.example.demo122.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarModel {
    private List<Car> cars;
    private Random random;

    public enum Direction { LEFT, RIGHT, STRAIGHT }

    public static class Car {
        private double x, y; // Aracın konumu
        private Direction direction; // Gideceği yön
        private int road; // Başlangıç yolu (0: Kuzey, 1: Doğu, 2: Güney, 3: Batı)
        private double speed;

        public Car(int road, Random random) {
            this.road = road;
            this.speed = 2.0; // Sabit hız
            // Başlangıç konumları (kavşak merkezi 200,200)
            switch (road) {
                case 0: x = 200; y = 0; break; // Kuzeyden başla
                case 1: x = 400; y = 200; break; // Doğudan başla
                case 2: x = 200; y = 400; break; // Güneyden başla
                case 3: x = 0; y = 200; break; // Batıdan başla
            }
            // Rastgele yön seç
            Direction[] directions = Direction.values();
            this.direction = directions[random.nextInt(directions.length)];
        }

        public double getX() { return x; }
        public double getY() { return y; }
        public Direction getDirection() { return direction; }
        public int getRoad() { return road; }


        public void stop() {
            // Trafik ışığı koordinatları
            double northLightX = 230.0, northLightY = 200.0;
            double westLightX = 200.0, westLightY = 370.0;
            double southLightX = 370.0, southLightY = 370.0;
            double eastLightX = 370.0, eastLightY = 200.0;

            // Durma mesafesi (araba trafik ışığına bu kadar yakınsa durur)
            double stopDistance = 10.0;

            // Arabanın hangi yolda olduğuna ve yönüne bağlı olarak durma kontrolü
            switch (road) {
                case 0: // Kuzeyden gelen (güneye doğru hareket)
                    if (Math.abs(y - northLightY) < stopDistance && x >= northLightX - stopDistance && x <= northLightX + stopDistance) {
                        speed = 0.0; // Kuzey trafik ışığında dur
                    }
                    break;
                case 1: // Doğudan gelen (batıya doğru hareket)
                    if (Math.abs(x - eastLightX) < stopDistance && y >= eastLightY - stopDistance && y <= eastLightY + stopDistance) {
                        speed = 0.0; // Doğu trafik ışığında dur
                    }
                    break;
                case 2: // Güneyden gelen (kuzeye doğru hareket)
                    if (Math.abs(y - southLightY) < stopDistance && x >= southLightX - stopDistance && x <= southLightX + stopDistance) {
                        speed = 0.0; // Güney trafik ışığında dur
                    }
                    break;
                case 3: // Batıdan gelen (doğuya doğru hareket)
                    if (Math.abs(x - westLightX) < stopDistance && y >= westLightY - stopDistance && y <= westLightY + stopDistance) {
                        speed = 0.0; // Batı trafik ışığında dur
                    }
                    break;
            }
        }

        public void move() {
            // Kavşağa yaklaşana kadar düz ilerle
            if (road == 0 && y < 150) y += speed; // Kuzeyden güneye
            else if (road == 1 && x > 250) x -= speed; // Doğudan batıya
            else if (road == 2 && y > 250) y -= speed; // Güneyden kuzeye
            else if (road == 3 && x < 150) x += speed; // Batıdan doğuya
            else {
                // Kavşakta yön seçimi
                switch (road) {
                    case 0: // Kuzeyden gelen
                        if (direction == Direction.LEFT) x -= speed; // Sola dön
                        else if (direction == Direction.RIGHT) x += speed; // Sağa dön
                        else y += speed; // Düz devam et
                        break;
                    case 1: // Doğudan gelen
                        if (direction == Direction.LEFT) y -= speed;
                        else if (direction == Direction.RIGHT) y += speed;
                        else x -= speed;
                        break;
                    case 2: // Güneyden gelen
                        if (direction == Direction.LEFT) x += speed;
                        else if (direction == Direction.RIGHT) x -= speed;
                        else y -= speed;
                        break;
                    case 3: // Batıdan gelen
                        if (direction == Direction.LEFT) y += speed;
                        else if (direction == Direction.RIGHT) y -= speed;
                        else x += speed;
                        break;
                }
            }
        }

        public boolean isOutOfBounds() {
            return x < -20 || x > 420 || y < -20 || y > 420;
        }
    }

    public CarModel() {
        cars = new ArrayList<>();
        random = new Random();
    }

    public void addCar() {
        int road = random.nextInt(4); // Rastgele yol (0-3)
        cars.add(new Car(road, random));
    }

    public List<Car> getCars() {
        return cars;
    }

    public void updateCars() {
        for (Car car : new ArrayList<>(cars)) {
            car.move();
            if (car.isOutOfBounds()) {
                cars.remove(car); // Sınırların dışına çıkan arabayı kaldır
            }
        }
    }
}
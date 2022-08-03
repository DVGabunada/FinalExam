package com.example.midterms;

public class Pipe {
    String brand;
    double diameter;

    public Pipe(String brand, double diameter) {
        this.brand = brand;
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return brand + "(" + diameter + ")";
    }
}

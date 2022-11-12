package com.shurman.homevisits.data;

public class DEntry {
    public int price;
    public int salary;
    public int count;

    public DEntry(int price, int salary, int count) {       //  // TODO: 03.11.2022 make it with default access back!!!! (used in preset day)
        this.price = price;
        this.salary = salary;
        this.count = count;
    }

    public boolean corresponds(DEntry de) { return this.price == de.price && this.salary == de.salary; }
}

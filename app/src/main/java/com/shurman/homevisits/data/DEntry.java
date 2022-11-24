package com.shurman.homevisits.data;

import java.util.Comparator;

public class DEntry {
    public int price;
    public int salary;
    public int count;

    public DEntry(int price, int salary, int count) {       //  // TODO: 03.11.2022 make it with default access back!!!! (used in preset day)
        this.price = price;
        this.salary = salary;
        this.count = count;
    }

    public DEntry(int price_salary, int count) {
        this(price_salary >>> 16, price_salary & 0xFFFF, count);
    }

    public boolean corresponds(DEntry de) { return this.price == de.price && this.salary == de.salary; }

    public static int combinePriceSalary(int price, int salary) {
        return (price << 16) | salary;
    }

    public static int priceFromPair(int price_salary) {
        return price_salary >>> 16;
    }

    public static int salaryFromPair(int price_salary) {
        return price_salary & 0xFFFF;
    }

    public static Comparator<DEntry> comparator = (e1, e2) -> {
        if (e1.price == e2.price) return e1.salary - e2.salary;
        return e1.price - e2.price;
    };
}

package com.shurman.homevisits;

import com.shurman.homevisits.data.DEntry;

import org.junit.Test;

import static org.junit.Assert.*;

public class DEntryTest {
    @Test
    public void combinePriceSalaryTest() {
        int price = 0xABFE;
        int salary = 0xCDAB;
        int combo = DEntry.combinePriceSalary(price, salary);
        DEntry de = new DEntry(combo, 0);
        assertEquals(price, de.price);
        assertEquals(salary, de.salary);
    }
}

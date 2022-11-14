package com.shurman.homevisits.table;

public class Scroller {
    private final int minScroll;
    private int maxScroll;
    private int currentScroll;
    private boolean scrollable;

    public Scroller() {
        minScroll = 0;
        reset(0);
    }

    public void reset(int range) {
        maxScroll = range;
        currentScroll = minScroll;                    //      TODO        savedInstanceState
        scrollable = maxScroll > minScroll;
    }

    public int scroll(int d) {
        int previousScroll = currentScroll;
        currentScroll += d;
        if (currentScroll < minScroll) currentScroll = minScroll;
        else if (currentScroll > maxScroll) currentScroll = maxScroll;
        return currentScroll - previousScroll;
    }

    public int getPosition() { return currentScroll; }

    public boolean isScrollable() { return scrollable; }
}

package com.shurman.homevisits.table;

public class Scroller {
    private static final int DEFAULT_SENSITIVITY = 0;
    private final int sensitivity;
    private int minScroll;
    private int maxScroll;
    private int currentScroll;
    private int accumulator;
    private boolean scrollable;

    public Scroller() {
        sensitivity = DEFAULT_SENSITIVITY;
        accumulator = 0;
        reset(0, 0);
    }

    public void reset(int min, int max) {
        minScroll = min;
        maxScroll = max;
        currentScroll = min;                    //      TODO        savedInstanceState
        scrollable = max > min;
    }

    public int scroll(int d) {
        accumulator += d;
        if (sensitivity > (accumulator < 0 ? -accumulator : accumulator))
            return 0;
        int previousScroll = currentScroll;
        currentScroll += accumulator;
        if (currentScroll < minScroll) currentScroll = minScroll;
        else if (currentScroll > maxScroll) currentScroll = maxScroll;
        accumulator = 0;
        return currentScroll - previousScroll;
    }

    public int getPosition() { return currentScroll; }

    public boolean isScrollable() { return scrollable; }
}

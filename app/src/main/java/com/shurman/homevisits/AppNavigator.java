package com.shurman.homevisits;

public class AppNavigator {
    public enum Request {DAYS, MONTHS, YEARS, PREFS}
    public enum Screen {DAYS, MONTHS_LIST, MONTHS_TABLE, YEARS, PREFS, LAUNCH}

    private Screen mScreen;

    public AppNavigator() { mScreen = Screen.LAUNCH; }

    public AppNavigator(Request reqScreen) {
        goTo(reqScreen);
    }

    public void goTo(Request reqScreen) {
        switch (reqScreen) {
            case DAYS:
                mScreen = Screen.DAYS;
                break;
            case MONTHS:
                mScreen = (mScreen == Screen.MONTHS_LIST) ? Screen.MONTHS_TABLE : Screen.MONTHS_LIST;
                break;
            case YEARS:
                mScreen = Screen.YEARS;
                break;
            case PREFS:
                mScreen = Screen.PREFS;
                break;
        }
    }

    public Screen screen() { return mScreen; }
}

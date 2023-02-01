package com.shurman.homevisits;

import android.view.Menu;
import android.view.MenuItem;

public class MenuComposer {
    public static final int ITEM_PREFS =    0x01;
    public static final int ITEM_YEAR =     0x02;
    public static final int ITEM_MONTH =    0x04;
    public static final int ITEM_DAY =      0x08;
    public static final int ITEM_EDIT =     0x10;
    //public static final int ITEM_BACK =     0x20;
                            //                    ---e dmys
    private static final int MASK_DAYS =        0b0001_0111;
    private static final int MASK_MON_LIST =    0b0000_1111;
    private static final int MASK_MON_TABLE =   0b0000_1111;
    private static final int MASK_YEARS =       0b0000_1101;
    private static final int MASK_PREFS =       0b0000_1110;
    private static final int MASK_DEFAULT = 0;

    public static void composeMenu(Menu menu, AppNavigator.Screen screen) {
        int mask = getItemsMask(screen);
        if ((mask & ITEM_EDIT) != 0)
            menu.add(0, ITEM_EDIT, 0, R.string.menu_main_edit)
                    .setIcon(android.R.drawable.ic_menu_edit)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if ((mask & ITEM_DAY) != 0)
            menu.add(0, ITEM_DAY, 0, R.string.menu_main_day)
                    .setIcon(android.R.drawable.ic_menu_day)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        if ((mask & ITEM_MONTH) != 0)
            menu.add(0, ITEM_MONTH, 0, R.string.menu_main_month)
                    .setIcon(android.R.drawable.ic_menu_month)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        if ((mask & ITEM_YEAR) != 0)
            menu.add(0, ITEM_YEAR, 0, R.string.menu_main_year)
                    .setIcon(android.R.drawable.ic_menu_my_calendar)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        if ((mask & ITEM_PREFS) != 0)
            menu.add(0, ITEM_PREFS, 0, R.string.menu_main_prefs)
                    .setIcon(android.R.drawable.ic_menu_preferences)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    private static int getItemsMask(AppNavigator.Screen screen) {
        switch (screen) {
            case DAYS: return MASK_DAYS;
            case MONTHS_LIST: return MASK_MON_LIST;
            case MONTHS_TABLE: return MASK_MON_TABLE;
            case YEARS: return MASK_YEARS;
            case PREFS: return MASK_PREFS;
            default: return MASK_DEFAULT;
        }
    }
}

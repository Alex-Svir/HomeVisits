package com.shurman.homevisits;

import android.content.Context;

public class RepresentationUtilities {
    public static String representDaysOffset(Context context, int offset) {
        String[] values;
        if (offset < 0) {
            values = context.getResources().getStringArray(R.array.daysBefore);
            offset = -offset - 1;
            if (offset < 3) return values[offset];
            if (offset < 7) return values[3];
            if (offset < 31) return values[4];
            if (offset < 366) return values[5];
            return values[6];
        } else {
            values = context.getResources().getStringArray(R.array.daysAfterAndToday);
            if (offset < 3) return values[offset];
            return values[3];
        }
    }
}

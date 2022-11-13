package com.shurman.homevisits.table;

import android.util.Log;

public class CellsCoordinator {
    static final int COLUMNS = 10;
    static final int CAPACITY = 175;
//==================================================================================================
    public static final int FIXED_TOP = 2;
    public static final int FIXED_LEFT = 1;

    private int columns;
    private int rows;

    private int cellWidth;
    private int cellHeight;
    private int fixWidth;
    private int fixHeight;

    private int viewWidth;
    private int viewHeight;

    public CellsCoordinator() {
        setTableDimensions(COLUMNS, CAPACITY / COLUMNS + (CAPACITY % COLUMNS > 0 ? 1 : 0));
        setCellDimensions(0,0,0,0,0,0);
    }

    public void setTableDimensions(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void setCellDimensions(int cellX, int cellY, int hdrX, int hdrY, int width, int height) {
        cellWidth = cellX;
        cellHeight = cellY;
        fixWidth = hdrX;
        fixHeight = hdrY;
        viewWidth = width;
        viewHeight = height;
    }

    public Bounds visibleItems(final int x1, final int y1, final int x2, final int y2) {
        int left, right, top, bottom, startX, startY;
        if (x1 <= fixWidth * FIXED_LEFT) {
            left = x1 / fixWidth;
            startX = left * fixWidth - x1;
        } else {
            left = (x1 - fixWidth * FIXED_LEFT) / cellWidth + FIXED_LEFT;
            if (left >= columns) left = columns - 1;                        //      TODO    ???????????????????????
            startX = fixWidth * FIXED_LEFT + (left - FIXED_LEFT) * cellWidth - x1;
        }
        if (x2 <= fixWidth * FIXED_LEFT) {
            right = x2 / fixWidth;
        } else {
            right = (x2 - fixWidth * FIXED_LEFT) / cellWidth + FIXED_LEFT;
            if (right >= columns) right = columns - 1;
        }
        if (y1 <= fixHeight * FIXED_TOP) {
            top = y1 / fixHeight;
            startY = top * fixHeight - y1;
        } else {
            top = (y1 - fixHeight * FIXED_TOP) / cellHeight + FIXED_TOP;
            if (top >= rows) top = rows - 1;                                //      TODO    ???????????????????????
            startY = fixHeight * FIXED_TOP + (top - FIXED_TOP) * cellHeight - y1;
        }
        if (y2 <= fixHeight * FIXED_TOP) {
            bottom = y2 / fixHeight;
        } else {
            bottom = (y2 - fixHeight * FIXED_TOP) / cellHeight + FIXED_TOP;
            if (bottom >= rows) bottom = rows - 1;
        }
        return new Bounds(left, right, top, bottom, columns, startX, startY);
    }

    public int getColumns() {return columns;}
    public int getRows() {return rows;}

    public int[] calculateScrollableBounds() {
        int[] bounds = new int[4];
        bounds[0] = 0;                      //              TODO    fixed columns
        bounds[1] = fixWidth * FIXED_LEFT + cellWidth * (columns - FIXED_LEFT) - viewWidth;
        if (bounds[1] < 0) bounds[1] = 0;
        bounds[2] = 0;                      //              TODO    fixed rows
        bounds[3] = fixHeight * FIXED_TOP + cellHeight * (rows - FIXED_TOP) - viewHeight;
        if (bounds[3] < 0) bounds[3] = 0;
        return bounds;
    }

//--------------------------------------------------------------------------------------------------
    public static class Bounds {
        private int current;
        private int nextEnd;
        private final int finish;
        private final int rowIncrement;
        private final int cols;
        private boolean rowSwitched;
        private final int x;
        private final int y;

        private Bounds(int left, int right, int top, int bottom, int columns, int startX, int startY) {
            current = columns * top + left - 1;
            nextEnd = columns * top + right;
            finish = columns * bottom + right;
            rowIncrement = columns - right + left - 1;
            cols = columns;
            rowSwitched = false;
            x = startX;
            y = startY;
        }

        public int next() {
            rowSwitched = false;
            if (++current > finish) return -1;
            if (current > nextEnd) {
                rowSwitched = true;
                nextEnd += cols;
                return current += rowIncrement;
            }
            return current;
        }
        public boolean newRow() { return rowSwitched; }
        public int startX() { return x; }
        public int startY() { return y; }
    }

//==================================================================================================
    private static void l(String text) { Log.d("LOG_TAG::", text); }
}

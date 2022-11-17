package com.shurman.homevisits.table;

import android.util.Log;

public class CellsCoordinator {
    private static final int FIXED_ROWS = 2;
    private static final int FIXED_COLS = 1;

    private int mColumns;
    private int mRows;
    private int mFixedColumns;
    private int mFixedRows;
    private int mScrollableColumns;
    private int mScrollableRows;

    private int mCellWidth;
    private int mCellHeight;
    private int mFixedColumnsWidth;
    private int mFixedRowsHeight;

    private int mFixedWidth;
    private int mFixedHeight;
    private int mScrollableWidth;
    private int mScrollableHeight;
    private int mTotalWidth;
    private int mTotalHeight;

    private int mFrameWidth;
    private int mFrameHeight;
    private int mSubframeWidth;
    private int mSubframeHeight;

    private int mScrollRangeHor;
    private int mScrollRangeVert;

    private final Frame mFrame;

    public CellsCoordinator() {
        mFrame = new Frame();
        setTableDimensions(0, 0);
        setCellDimensions(0,0,0,0,0,0);
    }

    public void setTableDimensions(int columns, int rows) {
        mColumns = columns;
        mRows = rows;
        mFixedColumns = FIXED_COLS;                //      TODO
        mFixedRows = FIXED_ROWS;                    //      TODO
        mScrollableColumns = Math.max(mColumns - mFixedColumns, 0);
        mScrollableRows = Math.max(mRows - mFixedRows, 0);
    }

    public int[] setCellDimensions(int cellX, int cellY, int hdrX, int hdrY, int width, int height) {
        mCellWidth = cellX;
        mCellHeight = cellY;
        mFixedColumnsWidth = hdrX;
        mFixedRowsHeight = hdrY;

        mFixedWidth = mFixedColumnsWidth * mFixedColumns;
        mFixedHeight = mFixedRowsHeight * mFixedRows;
        mScrollableWidth = mCellWidth * mScrollableColumns;
        mScrollableHeight = mCellHeight * mScrollableRows;
        mTotalWidth = mFixedWidth + mScrollableWidth;
        mTotalHeight = mFixedHeight + mScrollableHeight;

        mFrameWidth = width;
        mFrameHeight = height;
        mSubframeWidth = Math.max(mFrameWidth - mFixedWidth, 0);
        mSubframeHeight = Math.max(mFrameHeight - mFixedHeight, 0);

        mScrollRangeHor = Math.max(mScrollableWidth - mSubframeWidth, 0);
        mScrollRangeVert = Math.max(mScrollableHeight - mSubframeHeight, 0);

        return new int[] {mScrollRangeHor, mScrollRangeVert};
    }

    public Frame visibleItems(int scrollX, int scrollY) {
        mFrame.setup(scrollX, scrollY);
        return mFrame;
    }

    public int getColumns() {return mColumns;}
    public int getRows() {return mRows;}
    public int getFixedColumns() { return mFixedColumns; }
    public int getFixedRows() { return mFixedRows; }

//==================================================================================================
//==================================================================================================
//==================================================================================================

    public class Frame {
        private static final int PHASE_END = 0;
        private static final int PHASE_CORNER = 1;
        private static final int PHASE_LEFT = 2;
        private static final int PHASE_TOP = 3;
        private static final int PHASE_SCROLLABLE = 4;
        private static final int PHASE_INIT = 5;
        private int mPhase;

        private int mColFirst;
        private int mColLast;
        private int mRowFirst;
        private int mRowLast;

        private int mCurrent;
        private int mNextEnd;
        private int mFinish;
        private int mRowIncrement;
        private boolean mRowSwitched;

        private int startX;
        private int startY;

        private Frame() { mPhase = PHASE_END; }

        private void setup(int scrollX, int scrollY) {
            final int colsRef = CellsCoordinator.this.mColumns > 0 ? CellsCoordinator.this.mColumns - 1 : 0;
            final int rowsRef = CellsCoordinator.this.mRows > 0 ? CellsCoordinator.this.mRows - 1 : 0;
            final int columnsSkipped = (0 == CellsCoordinator.this.mCellWidth ? 0 : scrollX / CellsCoordinator.this.mCellWidth);
            final int rowsSkipped = (0 == CellsCoordinator.this.mCellHeight ? 0 : scrollY / CellsCoordinator.this.mCellHeight);
            mColFirst = Math.min(columnsSkipped
                    + CellsCoordinator.this.mFixedColumns, colsRef);
            mColLast = Math.min(
                    (0 == CellsCoordinator.this.mCellWidth ?
                            0 : (scrollX + CellsCoordinator.this.mSubframeWidth) / CellsCoordinator.this.mCellWidth)
                    + CellsCoordinator.this.mFixedColumns, colsRef);
            mRowFirst = Math.min(rowsSkipped
                    + CellsCoordinator.this.mFixedRows, rowsRef);
            mRowLast = Math.min(
                    (0 == CellsCoordinator.this.mCellHeight ?
                        0 : (scrollY + CellsCoordinator.this.mSubframeHeight) / CellsCoordinator.this.mCellHeight)
                    + CellsCoordinator.this.mFixedRows, rowsRef);

            mCurrent = CellsCoordinator.this.mColumns * mRowFirst + mColFirst - 1;
            mNextEnd = CellsCoordinator.this.mColumns * mRowFirst + mColLast;
            mFinish = CellsCoordinator.this.mColumns * mRowLast + mColLast;
            mRowIncrement = CellsCoordinator.this.mColumns - mColLast + mColFirst - 1;
            mRowSwitched = false;
            startX = columnsSkipped * CellsCoordinator.this.mCellWidth - scrollX + CellsCoordinator.this.mFixedWidth;
            startY = rowsSkipped * CellsCoordinator.this.mCellHeight - scrollY + CellsCoordinator.this.mFixedHeight;

            mPhase = PHASE_INIT;
        }

        public int next() {
            mRowSwitched = false;
            if (++mCurrent > mFinish) return -1;
            if (mCurrent > mNextEnd) {
                mRowSwitched = true;
                mNextEnd += CellsCoordinator.this.mColumns;
                return mCurrent += mRowIncrement;
            }
            return mCurrent;
        }

        public int[] nextBlock() {
            switch (--mPhase) {
                case PHASE_SCROLLABLE:
                    return new int[] {startX, startY};
                case PHASE_TOP:
                    return jumpToTop();
                case PHASE_LEFT:
                    return jumpToLeft();
                case PHASE_CORNER:
                    return jumpToCorner();
                default:
                    return null;
            }
        }

        private int[] jumpToTop() {
            mCurrent = mColFirst - 1;
            if (CellsCoordinator.this.mFixedRows > 0) {
                mNextEnd = mColLast;
                mFinish = (CellsCoordinator.this.mFixedRows - 1) * CellsCoordinator.this.mColumns + mColLast;
            } else {
                mFinish = mCurrent;
            }
            return new int[] {startX, 0};
        }

        private int[] jumpToLeft() {
            mCurrent = mRowFirst * CellsCoordinator.this.mColumns - 1;
            if (CellsCoordinator.this.mFixedColumns > 0) {
                mNextEnd = mCurrent + CellsCoordinator.this.mFixedColumns;  //  rowFirst * columns + fixCols - 1 = (cur + 1) + fixCols - 1
                mFinish = mRowLast * CellsCoordinator.this.mColumns + CellsCoordinator.this.mFixedColumns - 1;
                mRowIncrement = CellsCoordinator.this.mScrollableColumns;
            } else {
                mFinish = mCurrent;
            }
            return new int[] {0, startY};
        }

        private int[] jumpToCorner() {
            mCurrent = -1;
            if (CellsCoordinator.this.mFixedColumns > 0 || CellsCoordinator.this.mFixedRows > 0) {
                mNextEnd = CellsCoordinator.this.getFixedColumns() - 1;
                mFinish = (CellsCoordinator.this.mFixedRows - 1) * CellsCoordinator.this.mColumns
                        + CellsCoordinator.this.getFixedColumns() - 1;
            } else {
                mFinish = mCurrent;
            }
            return new int[] {0, 0};
        }

        public boolean newRow() { return mRowSwitched; }
    }

//==================================================================================================
    private static void l(String text) { Log.d("LOG_TAG::", text); }
}

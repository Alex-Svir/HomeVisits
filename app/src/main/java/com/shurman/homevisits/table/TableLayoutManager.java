package com.shurman.homevisits.table;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TableLayoutManager extends RecyclerView.LayoutManager {
    private final CellsCoordinator coordinator;
    private final Scroller scrollHor;
    private final Scroller scrollVert;

    public TableLayoutManager(CellsCoordinator coordinator) {
        this.coordinator = coordinator;
        scrollHor = new Scroller();
        scrollVert = new Scroller();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            l(" onLayoutChildren: start");
        int parentWidth = getWidth();
        int parentHeight = getHeight();
            l(" RV measures: " + parentWidth + ", " + parentHeight);

        if (state.didStructureChange())
                    recalculateCellsSizes(recycler, state, parentWidth, parentHeight);
        layoutVisibleArea(recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = scrollHor.scroll(dx);
        if (scrolled != 0) {
            layoutVisibleArea(recycler, state);
        }
        return scrolled;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = scrollVert.scroll(dy);
        if (scrolled != 0) {
            layoutVisibleArea(recycler, state);
        }
        return scrolled;
    }

    @Override
    public boolean canScrollHorizontally() { return scrollHor.isScrollable(); }

    @Override
    public boolean canScrollVertically() { return scrollVert.isScrollable(); }

    public void recalculateCellsSizes(RecyclerView.Recycler recycler, RecyclerView.State state, int width, int height) {
        //detachAndScrapAttachedViews(recycler);
        //removeAllViews();
        l(" recalculating in progress");
        final int cols = coordinator.getColumns();
        int hdrX = 0, hdrY = 0, cellX = 0, cellY = 0;
        for (int i = 0; i < state.getItemCount(); ++i) {
            final View view = recycler.getViewForPosition(i);
            measureChildWithMargins(view, 0, 0);

            int m = view.getMeasuredHeight();
            if (i / cols < coordinator.getFixedRows()) { if (hdrY < m) hdrY = m; }
            else { if (cellY < m) cellY = m; }

            m = view.getMeasuredWidth();
            if (i % cols < coordinator.getFixedColumns()) { if (hdrX < m) hdrX = m; }
            else { if (cellX < m) cellX = m; }
        }
        int[] bounds = coordinator.setCellDimensions(cellX, cellY, hdrX, hdrY, width, height);
        scrollHor.reset(bounds[0]);
        scrollVert.reset(bounds[1]);
    }

    private void layoutVisibleArea(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //l(" layouting in progress");
        detachAndScrapAttachedViews(recycler);
        CellsCoordinator.Frame boundItems = coordinator.visibleItems(scrollHor.getPosition(), scrollVert.getPosition());
        int[] coordinates;
        while (null != (coordinates = boundItems.nextBlock())) {
            int left = coordinates[0];
            int top = coordinates[1];
            int right;
            int bottom = top;
            final int leftBoundary = left;
            for (int it = boundItems.next(); it >= 0 && it < state.getItemCount(); it = boundItems.next()) {
                final View view = recycler.getViewForPosition(it);
                addView(view);
                measureChildWithMargins(view, 0, 0);                //  TODO    validate => remeasure table
                if (boundItems.newRow()) {
                    left = leftBoundary;
                    top = bottom;
                }
                right = left + view.getMeasuredWidth();
                bottom = top + view.getMeasuredHeight();
                layoutDecoratedWithMargins(view, left, top, right, bottom);
                left = right;
            }
        }
        recycler.clear();
    }

    @Override
    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter, @Nullable RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        l("oldAdapter " + (oldAdapter == null ? "-" : "+") + ", newAdapter " + (newAdapter == null ? "-" : "+"));
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

//==================================================================================================
    private static void l(String text) { Log.d("LOG_TAG::", text); }
}

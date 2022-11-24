package com.shurman.homevisits.table;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FrameDrawer extends RecyclerView.ItemDecoration {
    private final Paint paint;
    private final Rect frame;

    public FrameDrawer() {
        paint = new Paint();
        frame = new Rect();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int tableWidth = parent.getWidth();
        int tableHeight = parent.getHeight();
        int horBias = 0;
        int vertBias = 0;

        for (int i = 0; i < state.getItemCount(); ++i) {
            final View v = parent.getChildAt(i);
            if (null == v) continue;
            parent.getDecoratedBoundsWithMargins(v, frame);

            if (frame.bottom > vertBias) {
                vertBias = frame.bottom;
                c.drawLine(0, vertBias, tableWidth, vertBias, paint);
            }
            if (frame.right > horBias) {
                horBias = frame.right;
                c.drawLine(horBias, 0, horBias, tableHeight, paint);
            }
        }

        /*
        for (int i = 0; i < state.getItemCount(); ++i) {
            final View v = parent.getChildAt(i);

            if (null == v) continue;
            parent.getDecoratedBoundsWithMargins(v, frame);
            //getItemOffsets(frame, v, parent, state);
            c.drawRect(frame, paint);
        }
         */
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < state.getItemCount(); ++i) {
            final View v = parent.getChildAt(i);
            if (null == v) continue;
            getItemOffsets(frame, v, parent, state);
            c.drawRect(frame, paint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}

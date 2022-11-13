package com.shurman.homevisits.table;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FixedBorders extends RecyclerView.ItemDecoration {
    private final Paint paint;

    public FixedBorders() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.drawLine(0,0,20,20,paint);
        l(">");
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        c.drawLine(0,20, 20, 0, paint);
        l("@");
        View v = parent.getChildAt(0);
        if (v != null) {
            Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            v.draw(new Canvas(bm));
            if (bm != null) c.drawBitmap(bm, 0, 0, paint);
            else l("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        } else l("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }


//==================================================================================================


    private static void l(String text) {
        Log.d("LOG_TAG::", text);
    }
}
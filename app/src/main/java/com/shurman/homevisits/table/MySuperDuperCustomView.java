package com.shurman.homevisits.table;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class MySuperDuperCustomView extends View {
    private final Paint paint;
    private final RectF rectf;

    public MySuperDuperCustomView(Context context) {
        super(context);
        //l("Constructor");
        paint = new Paint();
        rectf = new RectF();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //l("onAttachedToWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int WIDTH = 252;
        final int HEIGHT = 252;
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int horMode = MeasureSpec.getMode(widthMeasureSpec);
        int vertMode = MeasureSpec.getMode(heightMeasureSpec);
        int reqWidth = MeasureSpec.getSize(widthMeasureSpec);
        int reqHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (horMode == MeasureSpec.UNSPECIFIED) {
            reqWidth = WIDTH;
        } else if (horMode == MeasureSpec.AT_MOST && reqWidth > WIDTH) {
            reqWidth = WIDTH;
        }
        if (vertMode == MeasureSpec.UNSPECIFIED) {
            reqHeight = HEIGHT;
        } else if (vertMode == MeasureSpec.AT_MOST && reqHeight > HEIGHT) {
            reqHeight = HEIGHT;
        }

        setMeasuredDimension(reqWidth, reqHeight);

        //l("onMeasure: " + MeasureSpec.toString(widthMeasureSpec) + ", " + MeasureSpec.toString(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //l("onDraw sizes: " + getWidth() + ", " + getHeight());
        int w = getWidth();
        int h = getHeight();

        if (w < 50 || h < 50) {
            paint.setColor(Color.RED);
            canvas.drawCircle(w/2f, h/2f, (w < h ? w : h) / 2f, paint);
            return;
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2.5f);
        rectf.set(5,5, w-5, h-5);
        canvas.drawRoundRect(rectf, 20, 20, paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(4.5f);
        rectf.set(12, 12, w-12, h-12);
        canvas.drawRoundRect(rectf, 14, 14, paint);
    }

    static void l(String text) { Log.d("LOG_TAG::MSDCV::", text); }
}

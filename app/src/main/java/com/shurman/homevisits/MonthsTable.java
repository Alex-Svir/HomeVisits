package com.shurman.homevisits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthsTable {
    private static final int COLUMNS = 26;
    private static final int CAPACITY = 570;
    private RecyclerView rvTable;
    private Adapter adapter;
    private RecyclerView.LayoutManager layMan;


    public MonthsTable(Context context, View root) {
        rvTable = root.findViewById(R.id.rv_table);
        rvTable.setAdapter(adapter = new Adapter());
        rvTable.setLayoutManager(
                new GridLayoutManager(context, COLUMNS, RecyclerView.VERTICAL, false)
        );
        rvTable.addItemDecoration(new FixedBorders());
    }
//--------------------------------------------------------------------------------------------------
    public static class Adapter extends RecyclerView.Adapter<BaseVH> {

        @Override
        public int getItemViewType(int position) {
            return (position % 2);
        }

        @NonNull
        @Override
        public BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VHData(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_data, parent, false), viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseVH holder, int position) {
            ((VHData)holder).textView().setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return CAPACITY;
        }
    }
//==================================================================================================
    public static abstract class BaseVH extends RecyclerView.ViewHolder {
        public BaseVH(@NonNull View itemView) { super(itemView); }
    }

    public static class VHData extends BaseVH {
        private TextView tv;
        private int t;
        public VHData(View view, int type) {
            super(view);
            tv = view.findViewById(R.id.text);
            t = type;
            tv.setTextColor(t==0 ? Color.BLUE : Color.RED);
        }
        public TextView textView() { return tv; }
        public int type() { return t; }
    }
//==================================================================================================
    private static class FixedBorders extends RecyclerView.ItemDecoration {
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


    }

    private static void l(String text) {
        Log.d("LOG_TAG::", text);
    }

    class lm extends RecyclerView.LayoutManager {

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return null;
        }
    }

}

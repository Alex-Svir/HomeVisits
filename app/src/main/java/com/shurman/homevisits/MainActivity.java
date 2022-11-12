package com.shurman.homevisits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    private static final int MENU_ITEM_EDIT = 1;
    private static final int MENU_ITEM_DAYS = 2;
    private static final int MENU_ITEM_MONTH = 4;
    private static final int MENU_ITEM_YEAR = 8;
    private MainViewModel viewModel;
    private int mMenuItemsDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.navLiveData().observe(this, nav -> goToScreen(nav.screen()));

        if (savedInstanceState == null) { viewModel.requestScreen(AppNavigator.Request.DAYS); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int dropPtr = 1;
        int itemPtr = 0;
        while (dropPtr <= mMenuItemsDrop) {
            if ((dropPtr & mMenuItemsDrop) != 0) { menu.getItem(itemPtr).setVisible(false); }
            dropPtr <<= 1;
            itemPtr++;
        }
        l("create menu");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        l("prep menu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            viewModel.toggleEditMode();
        } else if (id == R.id.month) {
            viewModel.requestScreen(AppNavigator.Request.MONTHS);
        } else if (id == R.id.day) {
            viewModel.requestScreen(AppNavigator.Request.DAYS);
        } else if (id == R.id.year) {
            //viewModel.requestScreen(AppNavigator.Request.YEARS);
        }
        return true;
    }

    private void goToScreen(AppNavigator.Screen screen) {
        Fragment frag;
        switch (screen) {
            case DAYS:
                frag = new FragmentDay();
                mMenuItemsDrop = MENU_ITEM_DAYS;
                break;
            case MONTHS_LIST:
                frag = new FragmentMonthList();
                mMenuItemsDrop = MENU_ITEM_EDIT;
                break;
            case MONTHS_TABLE:
                frag = new FragmentMonthTable();
                mMenuItemsDrop = MENU_ITEM_EDIT;
                break;
            default:
                frag = new FragmentDay();
                mMenuItemsDrop = MENU_ITEM_EDIT | MENU_ITEM_YEAR;   //  as for year
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_root, frag).commit();
        invalidateOptionsMenu();
    }
//========================================================================================================================
    GestureDetector.SimpleOnGestureListener gestLsnr = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int sens = 50;
            if (e1.getX() - e2.getX() > sens) l("Gest left");
            else if (e2.getX() - e1.getX() > sens) l("Gest right");
            if (e1.getY() - e2.getY() > sens) l("Gest up");
            else if (e2.getY() - e1.getY() > sens) l("Gest down");

            return true;
        }
    };

    GestureDetector detec = new GestureDetector(getBaseContext(), gestLsnr);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detec.onTouchEvent(event);
    }

    private static void l(String text) {
        Log.d("LOG_TAG::", text);
    }
}
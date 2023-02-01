package com.shurman.homevisits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.shurman.homevisits.preferences.FragmentPreferences;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.navLiveData().observe(this, nav -> goToScreen(nav.screen()));

        if (savedInstanceState == null) { viewModel.appInitialLoad(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuComposer.composeMenu(menu, viewModel.getCurrentScreen());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == MenuComposer.ITEM_EDIT) {
            viewModel.toggleEditMode();
        } else if (id == MenuComposer.ITEM_MONTH) {
            viewModel.requestScreen(AppNavigator.Request.MONTHS);
        } else if (id == MenuComposer.ITEM_DAY) {
            viewModel.requestScreen(AppNavigator.Request.DAYS);
        } else if (id == MenuComposer.ITEM_YEAR) {

        } else if (id == MenuComposer.ITEM_PREFS) {
            viewModel.requestScreen(AppNavigator.Request.PREFS);
        }
        return true;
    }

    private void goToScreen(AppNavigator.Screen screen) {
        Fragment frag;
        switch (screen) {
            case DAYS:
                frag = new FragmentDay();
                break;
            case MONTHS_LIST:
                frag = new FragmentMonthList();
                break;
            case MONTHS_TABLE:
                frag = new FragmentMonthTable();
                break;
            case PREFS:
                frag = new FragmentPreferences();
                break;
            default:
                frag = new FragmentDay();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_root, frag).commit();
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
package com.hieunguyen.snacktruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hieunguyen.snacktruck.ui.main.FoodMenuFragment;

/**
 * Main activity of the application, this is the launcher activity and also the parent component to
 * hold fragment views.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // If the activity is first created, start the main FoodMenuFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FoodMenuFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mi_Add:
                // Return false, FoodMenuFragment will handle this action
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


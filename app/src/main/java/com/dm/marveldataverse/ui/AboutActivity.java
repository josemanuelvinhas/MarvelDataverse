package com.dm.marveldataverse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dm.marveldataverse.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_go_back, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean toret;

        switch (item.getItemId()) {
            case R.id.itGoBack:
                AboutActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }

        return toret;
    }

}
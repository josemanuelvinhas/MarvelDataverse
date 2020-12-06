package com.dm.marveldataverse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dm.marveldataverse.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Personalizar action-bar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.about);

        TextView TV_VINHAS = this.findViewById(R.id.vinhas);
        TV_VINHAS.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/josemanuelvinhas");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        TextView TV_YOMAR = this.findViewById(R.id.yomar);
        TV_YOMAR.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/Yomiquesh");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        TextView TV_RAUL = this.findViewById(R.id.raul);
        TV_RAUL.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/raulvarandela");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
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
package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;

public class CoreActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        CoreActivity.this.session = Session.getSession(CoreActivity.this);

        final Button BT_CHARACTERS_MANAGEMENT = CoreActivity.this.findViewById(R.id.btnCharactersManagement);
        BT_CHARACTERS_MANAGEMENT.setOnClickListener(v -> {
                CoreActivity.this.startCharactersActivity();
        });

        final Button BT_COMICS_MANAGEMENT = CoreActivity.this.findViewById(R.id.btnComicsManagement);
        BT_COMICS_MANAGEMENT.setOnClickListener(v -> {
            Toast.makeText( CoreActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show();
        });

        final Button BT_SERIES_MANAGEMENT = CoreActivity.this.findViewById(R.id.btnSeriesManagement);
        BT_SERIES_MANAGEMENT.setOnClickListener(v -> {
            Toast.makeText( CoreActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show();
        });

        if (!CoreActivity.this.session.isSessionActive()){
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CoreActivity.this.session.isSessionActive()){
            CoreActivity.this.startMainActivity();
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        CoreActivity.this.getMenuInflater().inflate(R.menu.menu_core, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CoreActivity.this.session.closeSession();
                CoreActivity.this.startMainActivity();
                CoreActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CoreActivity.this.startAboutActivity();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        CoreActivity.this.startActivity(new Intent(CoreActivity.this, AboutActivity.class));
    }

    private void startCharactersActivity() {
        CoreActivity.this.startActivity(new Intent(CoreActivity.this, CharactersActivity.class));
    }

    private void startMainActivity() {
        CoreActivity.this.startActivity(new Intent(CoreActivity.this, MainActivity.class));
    }


}
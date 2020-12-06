package com.dm.marveldataverse.ui.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.ui.AboutActivity;
import com.dm.marveldataverse.ui.MainActivity;

public class CoreUserActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Inicialización de variables
        CoreUserActivity.this.session = Session.getSession(CoreUserActivity.this);

        //Personalizar ActionBar
        if (CoreUserActivity.this.session.isAdmin()) {
            final ActionBar ACTION_BAR = this.getSupportActionBar();
            ACTION_BAR.setTitle(R.string.user_view);
        }

        //Inicialización de eventos
        final Button BT_CHARACTERS = CoreUserActivity.this.findViewById(R.id.btnCharacters);
        BT_CHARACTERS.setOnClickListener(v -> CoreUserActivity.this.startCharactersUserActivity());

        final Button BT_COMICS = CoreUserActivity.this.findViewById(R.id.btnComics);
        BT_COMICS.setOnClickListener(v -> Toast.makeText( CoreUserActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        final Button BT_SERIES = CoreUserActivity.this.findViewById(R.id.btnSeries);
        BT_SERIES.setOnClickListener(v -> Toast.makeText( CoreUserActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        CoreUserActivity.this.getOnBackPressedDispatcher().addCallback( CoreUserActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!CoreUserActivity.this.session.isAdmin()) {
                    CoreUserActivity.this.startMainActivity();
                }
                CoreUserActivity.this.finish();
            }
        });

        //Control de sesión
        if (!CoreUserActivity.this.session.isSessionActive()) {
            if (!CoreUserActivity.this.session.isAdmin()) {
                CoreUserActivity.this.startMainActivity();
            }
            CoreUserActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CoreUserActivity.this.session.isSessionActive()) {
            if (!CoreUserActivity.this.session.isAdmin()) {
                CoreUserActivity.this.startMainActivity();
            }
            CoreUserActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CoreUserActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CoreUserActivity.this.logOut();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CoreUserActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                CoreUserActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void logOut(){
        CoreUserActivity.this.session.closeSession();
        if (!CoreUserActivity.this.session.isAdmin()) {
            CoreUserActivity.this.startMainActivity();
        }
        CoreUserActivity.this.finish();
    }

    private void startAboutActivity() {
        CoreUserActivity.this.startActivity(new Intent(this, AboutActivity.class));
    }

    private void startMainActivity() {
        CoreUserActivity.this.startActivity(new Intent(CoreUserActivity.this, MainActivity.class));
    }

    private void startCharactersUserActivity() {
        CoreUserActivity.this.startActivity(new Intent(CoreUserActivity.this, CharactersUserActivity.class));
    }

}
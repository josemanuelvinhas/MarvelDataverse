package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;

public class CharactersActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        //Inicialización las variables
        CharactersActivity.this.session = Session.getSession(CharactersActivity.this);

        //Inicialización de eventos
        //TODO eventos de actividad de personajes

        //Si no existe la sesion
        if (!CharactersActivity.this.session.isSessionActive()){
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CharactersActivity.this.session.isSessionActive()){
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        CharactersActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CharactersActivity.this.session.closeSession();
                CharactersActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CharactersActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                CharactersActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(CharactersActivity.this, AboutActivity.class));
    }
}
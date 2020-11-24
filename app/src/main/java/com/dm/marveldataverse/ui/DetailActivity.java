package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;

public class DetailActivity extends AppCompatActivity {

    private CharacterMapper characterMapper;
    private Cursor cursorAdapter;
    private Session session;
    private Character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DetailActivity.this.session = Session.getSession(DetailActivity.this);

        //DetailActivity.this.show(this.getPersonaje());

        //Salir si existe una sesión
        if (!DetailActivity.this.session.isSessionActive()) {
            DetailActivity.this.finish();
        }

    }

    private Cursor getCharactersList() {
        return characterMapper.getCharactersList();
    }

    private Character getPersonaje() {
        Character personaje = null;


        cursorAdapter = getCharactersList();
        final int nombre = cursorAdapter.getColumnIndexOrThrow(DBManager.CAMPO_PERSONAJES_NAME);
        final int descripción = cursorAdapter.getColumnIndexOrThrow(DBManager.CAMPO_PERSONAJES_DESCRIPTION);
        if (cursorAdapter.moveToFirst()) {
            do {
                if (cursorAdapter.getString(nombre).equals("Thor")) {
                    personaje = new Character(cursorAdapter.getString(nombre), cursorAdapter.getString(descripción));
                }
            } while (cursorAdapter.moveToNext());
        }


        return personaje;
    }

    private void show(Character personaje) {
        final TextView TW_NAME = this.findViewById(R.id.edName);
        final TextView TW_DESCRIPTION = this.findViewById(R.id.edDescription);
        TW_NAME.setText(personaje.getName());
        TW_DESCRIPTION.setText(personaje.getDescription());

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (!DetailActivity.this.session.isSessionActive()) {
            DetailActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        DetailActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                DetailActivity.this.session.closeSession();
                DetailActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                DetailActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                DetailActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(DetailActivity.this, AboutActivity.class));
    }
}
package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;

public class AddCharacterActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        //Inicialización de variables
        AddCharacterActivity.this.session = Session.getSession(AddCharacterActivity.this);



        //Salir si existe una sesión
        if (!AddCharacterActivity.this.session.isSessionActive()) {
            AddCharacterActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (!AddCharacterActivity.this.session.isSessionActive()) {
            AddCharacterActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        AddCharacterActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                AddCharacterActivity.this.session.closeSession();
                AddCharacterActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                AddCharacterActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                AddCharacterActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(AddCharacterActivity.this, AboutActivity.class));
    }
}
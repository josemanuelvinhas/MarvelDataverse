package com.dm.marveldataverse.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.ui.AboutActivity;

public class CharactersUserActivity extends AppCompatActivity {

    private Session session;
    private SimpleCursorAdapter cursorAdapter;
    private CharacterMapper characterMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters_user);

        CharactersUserActivity.this.session = Session.getSession(CharactersUserActivity.this);

        CharactersUserActivity.this.characterMapper = new CharacterMapper(this);

        CharactersUserActivity.this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.entry_character_user,
                null,
                new String[]{DBManager.CAMPO_PERSONAJES_NAME},
                new int[]{R.id.lblName},
                0
        );

        final ListView LV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.lvCharacters);
        LV_CHARACTERS.setAdapter(this.cursorAdapter);

        CharactersUserActivity.this.refresh();

        if (!CharactersUserActivity.this.session.isSessionActive()) {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CharactersUserActivity.this.session.isSessionActive()) {
            this.finish();
        }
    }

    private void search(String query) {
        this.cursorAdapter.swapCursor(this.characterMapper.searchCharacter(query));
    }


    private void refresh() {
        this.search("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CharactersUserActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CharactersUserActivity.this.logOut();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CharactersUserActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                CharactersUserActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void logOut(){
        CharactersUserActivity.this.session.closeSession();
        CharactersUserActivity.this.finish();
    }

    private void startAboutActivity() {
        CharactersUserActivity.this.startActivity(new Intent(this, AboutActivity.class));
    }
}
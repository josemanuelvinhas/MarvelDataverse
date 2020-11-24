package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.CharacterMapper;

public class CharactersActivity extends AppCompatActivity {

    private Session session;
    private SimpleCursorAdapter cursorAdapter;
    private CharacterMapper characterMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        //Inicialización las variables
        CharactersActivity.this.session = Session.getSession(CharactersActivity.this);
        this.characterMapper = new CharacterMapper(this);
        this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.entry_character,
                null,
                new String[]{DBManager.CAMPO_PERSONAJES_NAME},
                new int[]{R.id.lblName},
                0
        );

        //Inicialización de eventos
        //TODO eventos de actividad de personajes
        //el boton de añadir click listener llamar metodo abajo

        final Button BT_ADDCHAR = CharactersActivity.this.findViewById(R.id.btnAddCharacter);
        final ListView LV_CHARACTERS = CharactersActivity.this.findViewById(R.id.lvCharacters);
        final SearchView SV_CHARACTERS = CharactersActivity.this.findViewById(R.id.svSearch);
        CharactersActivity.this.registerForContextMenu(LV_CHARACTERS);
        BT_ADDCHAR.setOnClickListener(v -> CharactersActivity.this.startAddCharacterActivity());
        LV_CHARACTERS.setAdapter(this.cursorAdapter);
        SV_CHARACTERS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharactersActivity.this.search(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                CharactersActivity.this.search(newText);
                return false;
            }
        });

        LV_CHARACTERS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = cursorAdapter.getCursor();
                cursor.moveToFirst();
                cursor.move(position);
                CharactersActivity.this.startDetailActivity(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
                //Toast.makeText(CharactersActivity.this, cursor.getString(1), Toast.LENGTH_SHORT).show();
            }
        });




        this.refresh();

        //Si no existe la sesion
        if (!CharactersActivity.this.session.isSessionActive()) {
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
    protected void onResume() {
        super.onResume();
        this.deleteSearchContent();
        this.refresh();
        if (!CharactersActivity.this.session.isSessionActive()) {
            this.finish();
        }
    }

    private void deleteSearchContent() {
        final SearchView SV_CHARACTERS = CharactersActivity.this.findViewById(R.id.svSearch);
        SV_CHARACTERS.setQuery("", false);
        SV_CHARACTERS.clearFocus();
        SV_CHARACTERS.onActionViewCollapsed();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.lvCharacters) {
            this.getMenuInflater().inflate(R.menu.menu_contex_character_activity, menu);
        }
    }

    private void startAddCharacterActivity() {
        this.startActivity(new Intent(CharactersActivity.this, AddCharacterActivity.class));
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(CharactersActivity.this, AboutActivity.class));
    }

    private void startDetailActivity(int id) {
        Intent intent = new Intent(CharactersActivity.this,DetailCharacterActivity.class);
        intent.putExtra("id",id);
        this.startActivity(intent);
    }

    private void startEditActivity() {
        this.startActivity(new Intent(CharactersActivity.this, EditCharacterActivity.class));
    }
}
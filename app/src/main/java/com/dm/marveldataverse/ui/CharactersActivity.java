package com.dm.marveldataverse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.dm.marveldataverse.model.Character;
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
        CharactersActivity.this.characterMapper = new CharacterMapper(this);
        CharactersActivity.this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.entry_character,
                null,
                new String[]{DBManager.CAMPO_PERSONAJES_NAME},
                new int[]{R.id.lblName},
                0
        );

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

        LV_CHARACTERS.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = cursorAdapter.getCursor();
            cursor.moveToFirst();
            cursor.move(position);
            CharactersActivity.this.startDetailCharacterActivity(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
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
        CharactersActivity.this.deleteSearchContent();
        CharactersActivity.this.refresh();
        CharactersActivity.this.cursorAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        boolean toret = false;
        Cursor cursor;
        AdapterView.AdapterContextMenuInfo menu = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.itEdit:
                cursor = cursorAdapter.getCursor();
                cursor.moveToFirst();
                cursor.move(menu.position);
                CharactersActivity.this.startEditCharacterActivity(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
                toret = true;
                break;
            case R.id.itDelete:
                cursor = cursorAdapter.getCursor();
                cursor.moveToFirst();
                cursor.move(menu.position);
                CharactersActivity.this.deleteCharacter(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
                toret = true;
                break;
        }

        return toret;
    }

    private void deleteCharacter(long id) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setTitle(R.string.delete);
        DLG.setMessage(R.string.delete_character_msg);
        DLG.setPositiveButton(R.string.yes, (dialog, which) -> {
            CharactersActivity.this.characterMapper.deleteCharacter(id);
            Toast.makeText(CharactersActivity.this,R.string.character_remove_successful,Toast.LENGTH_SHORT).show();
            CharactersActivity.this.refresh();
        });

        DLG.setNegativeButton(R.string.no,null);

        DLG.create().show();


    }

    private void startAddCharacterActivity() {
        CharactersActivity.this.startActivity(new Intent(CharactersActivity.this, AddCharacterActivity.class));
    }

    private void startAboutActivity() {
        CharactersActivity.this.startActivity(new Intent(CharactersActivity.this, AboutActivity.class));
    }

    private void startDetailCharacterActivity(long id) {
        Intent intent = new Intent(CharactersActivity.this, DetailCharacterActivity.class);
        intent.putExtra("id", id);
        CharactersActivity.this.startActivity(intent);
    }

    private void startEditCharacterActivity(long id) {
        Intent intent = new Intent(CharactersActivity.this, EditCharacterActivity.class);
        intent.putExtra("id", id);
        CharactersActivity.this.startActivity(intent);
    }
}
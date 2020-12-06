package com.dm.marveldataverse.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
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
import com.dm.marveldataverse.ui.AboutActivity;
import com.dm.marveldataverse.ui.user.CharactersUserActivity;

public class CharactersAdminActivity extends AppCompatActivity {

    private Session session;

    private CharacterMapper characterMapper;

    private SimpleCursorAdapter cursorAdapter;

    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters_admin);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.characters);

        //Inicialización las atributos
        CharactersAdminActivity.this.session = Session.getSession(CharactersAdminActivity.this);
        CharactersAdminActivity.this.characterMapper = new CharacterMapper(this);
        CharactersAdminActivity.this.cursorAdapter = new SimpleCursorAdapter(this,
                R.layout.entry_character_admin,
                null,
                new String[]{DBManager.CAMPO_PERSONAJES_NAME},
                new int[]{R.id.lblName},
                0
        );
        CharactersAdminActivity.this.currentQuery = "";

        //Inicialización de eventos
        //Evento de añadir
        final Button BT_ADDCHAR = CharactersAdminActivity.this.findViewById(R.id.btnAddCharacter);
        BT_ADDCHAR.setOnClickListener(v -> CharactersAdminActivity.this.startAddCharacterActivity());

        //Evento de buscar
        final SearchView SV_CHARACTERS = CharactersAdminActivity.this.findViewById(R.id.svSearch);
        SV_CHARACTERS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharactersAdminActivity.this.currentQuery = query;
                CharactersAdminActivity.this.search(CharactersAdminActivity.this.currentQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CharactersAdminActivity.this.currentQuery = newText;
                CharactersAdminActivity.this.search(CharactersAdminActivity.this.currentQuery);
                return false;
            }
        });

        //Evento de listview de personajes
        final ListView LV_CHARACTERS = CharactersAdminActivity.this.findViewById(R.id.lvCharacters);
        LV_CHARACTERS.setAdapter(this.cursorAdapter);
        CharactersAdminActivity.this.registerForContextMenu(LV_CHARACTERS);
        LV_CHARACTERS.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = cursorAdapter.getCursor();
            cursor.moveToFirst();
            cursor.move(position);
            CharactersAdminActivity.this.startDetailCharacterActivity(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
        });

        this.refresh();

        //Control de sesión
        if (!CharactersAdminActivity.this.session.isSessionActive() || !CharactersAdminActivity.this.session.isAdmin()) {
            CharactersAdminActivity.this.finish();
        }
    }

    private void search(String query) {
        this.cursorAdapter.swapCursor(this.characterMapper.searchCharacter(query));
    }


    private void refresh() {
        this.search(CharactersAdminActivity.this.currentQuery);
    }


    @Override
    protected void onResume() {
        super.onResume();
        CharactersAdminActivity.this.refresh();
        CharactersAdminActivity.this.cursorAdapter.notifyDataSetChanged();
        if (!CharactersAdminActivity.this.session.isSessionActive() || !CharactersAdminActivity.this.session.isAdmin()) {
            CharactersAdminActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CharactersAdminActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CharactersAdminActivity.this.session.closeSession();
                CharactersAdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CharactersAdminActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                CharactersAdminActivity.this.finish();
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
                CharactersAdminActivity.this.startEditCharacterActivity(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
                toret = true;
                break;
            case R.id.itDelete:
                cursor = cursorAdapter.getCursor();
                cursor.moveToFirst();
                cursor.move(menu.position);
                CharactersAdminActivity.this.deleteCharacter(cursor.getInt(cursor.getColumnIndex(DBManager.CAMPO_PERSONAJES_ID)));
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
            CharactersAdminActivity.this.characterMapper.deleteCharacter(id);
            Toast.makeText(CharactersAdminActivity.this,R.string.character_remove_successful,Toast.LENGTH_SHORT).show();
            CharactersAdminActivity.this.refresh();
        });

        DLG.setNegativeButton(R.string.no,null);

        DLG.create().show();


    }

    private void startAddCharacterActivity() {
        CharactersAdminActivity.this.startActivity(new Intent(CharactersAdminActivity.this, AddCharacterAdminActivity.class));
    }

    private void startAboutActivity() {
        CharactersAdminActivity.this.startActivity(new Intent(CharactersAdminActivity.this, AboutActivity.class));
    }

    private void startDetailCharacterActivity(long id) {
        Intent intent = new Intent(CharactersAdminActivity.this, DetailCharacterAdminActivity.class);
        intent.putExtra("id", id);
        CharactersAdminActivity.this.startActivity(intent);
    }

    private void startEditCharacterActivity(long id) {
        Intent intent = new Intent(CharactersAdminActivity.this, EditCharacterAdminActivity.class);
        intent.putExtra("id", id);
        CharactersAdminActivity.this.startActivity(intent);
    }
}
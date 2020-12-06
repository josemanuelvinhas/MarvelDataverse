package com.dm.marveldataverse.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.CharacterUserArrayAdapter;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.model.Fav;
import com.dm.marveldataverse.model.FavMapper;
import com.dm.marveldataverse.ui.AboutActivity;

import java.util.ArrayList;

public class CharactersUserActivity extends AppCompatActivity {

    private Session session;

    private CharacterMapper characterMapper;
    private FavMapper favMapper;

    private CharacterUserArrayAdapter characterUserArrayAdapter;
    private ArrayList<Pair<Character, Long>> lista;

    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters_user);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.characters);

        //Inicialización de atributos
        CharactersUserActivity.this.session = Session.getSession(CharactersUserActivity.this);

        CharactersUserActivity.this.characterMapper = new CharacterMapper(CharactersUserActivity.this);
        CharactersUserActivity.this.favMapper = new FavMapper(CharactersUserActivity.this);

        CharactersUserActivity.this.currentQuery = "";

        CharactersUserActivity.this.lista = CharactersUserActivity.this.characterMapper.searchCharacterWithFav(CharactersUserActivity.this.currentQuery, CharactersUserActivity.this.session.getUsername());
        CharactersUserActivity.this.characterUserArrayAdapter = new CharacterUserArrayAdapter(CharactersUserActivity.this, CharactersUserActivity.this.lista);

        //Inicialización de eventos
        //Eventos de ListView de Personajes
        final ListView LV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.lvCharacters);

        LV_CHARACTERS.setAdapter(CharactersUserActivity.this.characterUserArrayAdapter);

        LV_CHARACTERS.setOnItemLongClickListener((parent, view, position, id) -> {
            Pair<Character, Long> par = CharactersUserActivity.this.lista.get(position);
            if (par.second == -1) {
                CharactersUserActivity.this.favMapper.addFav(new Fav(CharactersUserActivity.this.session.getUsername(), par.first.getId()));
            } else {
                CharactersUserActivity.this.favMapper.deleteFav(par.second);
            }
            search(CharactersUserActivity.this.currentQuery);
            return true;

        });

        LV_CHARACTERS.setOnItemClickListener((parent, view, position, id) -> {
            Pair<Character, Long> pair = CharactersUserActivity.this.lista.get(position);
            CharactersUserActivity.this.startDetailCharacterActivity(pair.first.getId());
        });

        //Eventos de la barra de busqueda
        final SearchView SV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.svSearch);
        SV_CHARACTERS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharactersUserActivity.this.currentQuery = query;
                CharactersUserActivity.this.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CharactersUserActivity.this.currentQuery = newText;
                CharactersUserActivity.this.search(newText);
                return false;
            }
        });

        //Control de sesión
        if (!CharactersUserActivity.this.session.isSessionActive()) {
            this.finish();
        }
    }

    private void search(String query) {
        final ListView LV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.lvCharacters);
        CharactersUserActivity.this.lista = CharactersUserActivity.this.characterMapper.searchCharacterWithFav(query, CharactersUserActivity.this.session.getUsername());
        CharactersUserActivity.this.characterUserArrayAdapter = new CharacterUserArrayAdapter(this, CharactersUserActivity.this.lista);
        LV_CHARACTERS.setAdapter(CharactersUserActivity.this.characterUserArrayAdapter);
    }



    @Override
    protected void onResume() {
        super.onResume();

        CharactersUserActivity.this.search(CharactersUserActivity.this.currentQuery);

        if (!CharactersUserActivity.this.session.isSessionActive()) {
            this.finish();
        }
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
                CharactersUserActivity.this.session.closeSession();
                CharactersUserActivity.this.finish();
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

    private void startAboutActivity() {
        CharactersUserActivity.this.startActivity(new Intent(this, AboutActivity.class));
    }

    private void startDetailCharacterActivity(long id) {
        Intent intent = new Intent(CharactersUserActivity.this, DetailCharacterUser.class);
        intent.putExtra("id", id);
        CharactersUserActivity.this.startActivity(intent);
    }
}
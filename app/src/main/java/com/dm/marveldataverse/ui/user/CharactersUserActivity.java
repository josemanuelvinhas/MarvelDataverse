package com.dm.marveldataverse.ui.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.CharacterUserArrayAdapter;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.model.Fav;
import com.dm.marveldataverse.model.FavMapper;
import com.dm.marveldataverse.ui.AboutActivity;
import com.dm.marveldataverse.ui.admin.CharactersAdminActivity;
import com.dm.marveldataverse.ui.admin.DetailCharacterAdminActivity;

import java.util.ArrayList;

public class CharactersUserActivity extends AppCompatActivity {

    private Session session;
    private CharacterUserArrayAdapter characterUserArrayAdapter;
    private CharacterMapper characterMapper;
    private FavMapper favMapper;
    private ArrayList<Pair<Character, Long>> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters_user);

        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.characters);

        CharactersUserActivity.this.session = Session.getSession(CharactersUserActivity.this);

        CharactersUserActivity.this.characterMapper = new CharacterMapper(this);

        CharactersUserActivity.this.favMapper = new FavMapper(this);

        CharactersUserActivity.this.lista = CharactersUserActivity.this.characterMapper.searchCharacterWithFav("",session.getUsername());

        CharactersUserActivity.this.characterUserArrayAdapter = new CharacterUserArrayAdapter(this,CharactersUserActivity.this.lista);

        final ListView LV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.lvCharacters);
        LV_CHARACTERS.setAdapter(CharactersUserActivity.this.characterUserArrayAdapter);
        LV_CHARACTERS.setOnItemLongClickListener((parent, view, position, id) -> {
            Pair<Character,Long> par = lista.get(position);
            if (par.second == -1){
                favMapper.addFav(new Fav(session.getUsername(),par.first.getId()));
            }else {
                favMapper.deleteFav(par.second);
            }
            search("");//TODO Poner la query
            return true;
        });


        final SearchView SV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.svSearch);
        SV_CHARACTERS.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharactersUserActivity.this.search(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                CharactersUserActivity.this.search(newText);
                return false;
            }
        });

        if (!CharactersUserActivity.this.session.isSessionActive()) {
            this.finish();
        }
    }




    private void search(String query) {
        final ListView LV_CHARACTERS = CharactersUserActivity.this.findViewById(R.id.lvCharacters);
        CharactersUserActivity.this.lista = CharactersUserActivity.this.characterMapper.searchCharacterWithFav(query,session.getUsername());
        CharactersUserActivity.this.characterUserArrayAdapter = new CharacterUserArrayAdapter(this,CharactersUserActivity.this.lista);
        LV_CHARACTERS.setAdapter(CharactersUserActivity.this.characterUserArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
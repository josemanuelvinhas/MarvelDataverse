package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;

public class DetailCharacterActivity extends AppCompatActivity {

    private CharacterMapper characterMapper;
    private Cursor cursorAdapter;
    private Session session;
    private Character character;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character);
        DetailCharacterActivity.this.session = Session.getSession(DetailCharacterActivity.this);

        //inicializar las variables
        id = this.getIntent().getIntExtra("id", -1);
        characterMapper = new CharacterMapper(this);
        character = characterMapper.getCharacterById(id);
        final Button BT_EDITAR = DetailCharacterActivity.this.findViewById(R.id.btnEditCharacter);
        final Button BT_ELIMINAR = DetailCharacterActivity.this.findViewById(R.id.btnDelete);

        BT_EDITAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditCharacterActivity(id);
            }
        });


        DetailCharacterActivity.this.show();

        //Salir si existe una sesión
        if (!DetailCharacterActivity.this.session.isSessionActive()) {
            DetailCharacterActivity.this.finish();
        }

    }

    private Cursor getCharactersList() {
        return characterMapper.getCharactersList();
    }


    private void show() {
        final TextView TW_NAME = this.findViewById(R.id.edName);
        final TextView TW_DESCRIPTION = this.findViewById(R.id.edDescription);
        TW_NAME.setText(character.getName());
        TW_DESCRIPTION.setText(character.getDescription());

    }

    @Override
    protected void onResume() {
        super.onResume();
        character = characterMapper.getCharacterById(id);
        this.show();
        //Salir si existe una sesión
        if (!DetailCharacterActivity.this.session.isSessionActive()) {
            DetailCharacterActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        DetailCharacterActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                DetailCharacterActivity.this.session.closeSession();
                DetailCharacterActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                DetailCharacterActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                DetailCharacterActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(DetailCharacterActivity.this, AboutActivity.class));
    }

    private void startEditCharacterActivity(int id) {
        Intent intent = new Intent(DetailCharacterActivity.this, EditCharacterActivity.class);
        intent.putExtra("id", id);
        this.startActivity(intent);
    }
}
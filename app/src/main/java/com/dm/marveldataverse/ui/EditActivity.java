package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;

public class EditActivity extends AppCompatActivity {


    private Session session;
    private Character character;
    private CharacterMapper characterMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Inicialización de variables
        this.character = new Character();
        this.characterMapper = new CharacterMapper(this);
        EditActivity.this.session = Session.getSession(EditActivity.this);
        final Button BT_RESET = EditActivity.this.findViewById(R.id.btnReset);
        final Button BT_SEND = EditActivity.this.findViewById(R.id.btnAddCharacter);
        final EditText ED_NAME = EditActivity.this.findViewById(R.id.edName);
        final EditText ED_DESC = EditActivity.this.findViewById(R.id.edDescription);


        BT_RESET.setOnClickListener(v -> EditActivity.this.reset());
        BT_SEND.setOnClickListener(v -> EditActivity.this.editCharacter());
        ED_NAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateName(ED_NAME.getText().toString());
            } catch (ValidationException e) {
                ED_NAME.setError(getResources().getString(e.getError()));
            }
        });

        ED_DESC.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateDescription(ED_DESC.getText().toString());
            } catch (ValidationException e) {
                ED_DESC.setError(getResources().getString(e.getError()));
            }
        });


        //Salir si existe una sesión
        if (!EditActivity.this.session.isSessionActive()) {
            EditActivity.this.finish();
        }
    }

    private void reset() {
        final EditText ED_NAME = EditActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditActivity.this.findViewById(R.id.edDescription);
        ED_DESCRIPTION.setText("");
        ED_NAME.setText("");
    }

    private void editCharacter() {
        //todo: hacer que aparezcan en los campos los datos que hay actualmente en la base de datos y recuperar el personaje primero
        final EditText ED_NAME = EditActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditActivity.this.findViewById(R.id.edDescription);
        this.character.setName(ED_NAME.getText().toString());
        this.character.setDescription(ED_DESCRIPTION.getText().toString());
        try {
            this.character.validateForCreate();
            try {
                characterMapper.updateCharacter(character);
                Toast.makeText(EditActivity.this, R.string.character_edit_successful, Toast.LENGTH_SHORT).show();
                EditActivity.this.finish();
            } catch (RuntimeException ex) {
                Toast.makeText(EditActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
            }
        } catch (ValidationException e) {
            Toast.makeText(EditActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (!EditActivity.this.session.isSessionActive()) {
            EditActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        EditActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                EditActivity.this.session.closeSession();
                EditActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                EditActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                EditActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(EditActivity.this, AboutActivity.class));
    }
}
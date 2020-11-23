package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.model.User;

public class AddCharacterActivity extends AppCompatActivity {

    private Session session;
    private Character character;
    private CharacterMapper characterMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        //Inicialización de variables
        this.character = new Character();
        this.characterMapper = new CharacterMapper(this);
        AddCharacterActivity.this.session = Session.getSession(AddCharacterActivity.this);
        final Button BT_RESET = AddCharacterActivity.this.findViewById(R.id.btnReset);
        final Button BT_SEND = AddCharacterActivity.this.findViewById(R.id.btnAddCharacter);
        final EditText ED_NAME = AddCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESC = AddCharacterActivity.this.findViewById(R.id.edDescription);


        BT_RESET.setOnClickListener(v -> AddCharacterActivity.this.reset());
        BT_SEND.setOnClickListener(v -> AddCharacterActivity.this.addCharacter());
        ED_NAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateName(ED_NAME.getText().toString());
                try {
                    if (AddCharacterActivity.this.characterMapper.thisCharacterExist(ED_NAME.getText().toString())) {
                        ED_NAME.setError(getResources().getString(R.string.character_exists));
                    }
                } catch (RuntimeException ex) {
                    Log.e("BD", "Error BD, no se puede saber si existe");
                }
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
        if (!AddCharacterActivity.this.session.isSessionActive()) {
            AddCharacterActivity.this.finish();
        }
    }

    private void reset() {
        final EditText ED_NAME = AddCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = AddCharacterActivity.this.findViewById(R.id.edDescription);
        ED_DESCRIPTION.setText("");
        ED_NAME.setText("");
    }

    private void addCharacter() {
        final EditText ED_NAME = AddCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = AddCharacterActivity.this.findViewById(R.id.edDescription);
        this.character.setName(ED_NAME.getText().toString());
        this.character.setDescription(ED_DESCRIPTION.getText().toString());
        if (this.characterMapper.thisCharacterExist(this.character.getName())) {
            Toast.makeText(AddCharacterActivity.this, R.string.character_exists, Toast.LENGTH_SHORT).show();
        } else {
            try {
                this.character.validateForCreate();
                try {
                    characterMapper.addCharacter(character);
                    Toast.makeText(AddCharacterActivity.this, R.string.character_add_successful, Toast.LENGTH_SHORT).show();
                    AddCharacterActivity.this.finish();//TODO RAÜL Cambiar a pantalla de personaje creado
                    //TODO Ver si cambiamos los toast por LENGTH_SHORT
                } catch (RuntimeException ex) {
                    Toast.makeText(AddCharacterActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
                }
            } catch (ValidationException e) {
                Toast.makeText(AddCharacterActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
            }

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
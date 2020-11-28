package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;

public class EditCharacterActivity extends AppCompatActivity {


    private Session session;
    private Character character;
    private Character characterOld;
    private CharacterMapper characterMapper;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character);

        //Inicialización de variables
        EditCharacterActivity.this.character = new Character();
        EditCharacterActivity.this.characterMapper = new CharacterMapper(this);
        EditCharacterActivity.this.session = Session.getSession(EditCharacterActivity.this);

        final Button BT_RESET = EditCharacterActivity.this.findViewById(R.id.btnReset);
        final Button BT_EDIT = EditCharacterActivity.this.findViewById(R.id.btEditCharacter);

        final EditText ED_NAME = EditCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESC = EditCharacterActivity.this.findViewById(R.id.edDescription);

        EditCharacterActivity.this.id = this.getIntent().getLongExtra("id", -1);
        EditCharacterActivity.this.characterMapper = new CharacterMapper(this);
        EditCharacterActivity.this.character = characterMapper.getCharacterById(id);
        EditCharacterActivity.this.characterOld = new Character(EditCharacterActivity.this.character.getName(), EditCharacterActivity.this.character.getDescription());

        ED_NAME.setText(EditCharacterActivity.this.character.getName());
        ED_DESC.setText(EditCharacterActivity.this.character.getDescription());

        BT_RESET.setOnClickListener(v -> EditCharacterActivity.this.reset());
        BT_EDIT.setOnClickListener(v -> EditCharacterActivity.this.editCharacter());
        ED_NAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateName(ED_NAME.getText().toString());
                if (!EditCharacterActivity.this.characterOld.getName().equals(character.getName()) && EditCharacterActivity.this.characterMapper.thisCharacterExist(EditCharacterActivity.this.character.getName())) {
                    ED_NAME.setError(getResources().getString(R.string.character_exists));
                }
            } catch (ValidationException e) {
                ED_NAME.setError(EditCharacterActivity.this.getResources().getString(e.getError()));
            }
        });

        ED_DESC.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateDescription(ED_DESC.getText().toString());
            } catch (ValidationException e) {
                ED_DESC.setError(EditCharacterActivity.this.getResources().getString(e.getError()));
            }
        });


        //Salir si existe una sesión
        if (!EditCharacterActivity.this.session.isSessionActive()) {
            EditCharacterActivity.this.finish();
        }
    }

    private void reset() {
        final EditText ED_NAME = EditCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditCharacterActivity.this.findViewById(R.id.edDescription);

        ED_NAME.setText(characterOld.getName());
        ED_DESCRIPTION.setText(characterOld.getDescription());

    }

    private void editCharacter() {
        final EditText ED_NAME = EditCharacterActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditCharacterActivity.this.findViewById(R.id.edDescription);
        Toast.makeText(this, ED_NAME.getText().toString(), Toast.LENGTH_SHORT);
        EditCharacterActivity.this.character.setName(ED_NAME.getText().toString());
        EditCharacterActivity.this.character.setDescription(ED_DESCRIPTION.getText().toString());
        if (!EditCharacterActivity.this.characterOld.getName().equals(EditCharacterActivity.this.character.getName()) && EditCharacterActivity.this.characterMapper.thisCharacterExist(EditCharacterActivity.this.character.getName())) {
            Toast.makeText(EditCharacterActivity.this, R.string.character_exists, Toast.LENGTH_SHORT).show();
        } else {
            try {
                EditCharacterActivity.this.character.validateForCreate();
                try {
                    EditCharacterActivity.this.characterMapper.updateCharacter(character);
                    Toast.makeText(EditCharacterActivity.this, R.string.character_edit_successful, Toast.LENGTH_SHORT).show();
                    EditCharacterActivity.this.finish();
                } catch (RuntimeException ex) {
                    Toast.makeText(EditCharacterActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
                }
            } catch (ValidationException e) {
                Toast.makeText(EditCharacterActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (!EditCharacterActivity.this.session.isSessionActive()) {
            EditCharacterActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        EditCharacterActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                EditCharacterActivity.this.session.closeSession();
                EditCharacterActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                EditCharacterActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                EditCharacterActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        EditCharacterActivity.this.startActivity(new Intent(EditCharacterActivity.this, AboutActivity.class));
    }
}
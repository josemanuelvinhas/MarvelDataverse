package com.dm.marveldataverse.ui.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.dm.marveldataverse.ui.AboutActivity;

public class EditCharacterAdminActivity extends AppCompatActivity {

    private Session session;

    private CharacterMapper characterMapper;

    private Character character;
    private Character characterOld;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character_admin);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.edit_character);

        //Inicialización de atributos
        EditCharacterAdminActivity.this.session = Session.getSession(EditCharacterAdminActivity.this);
        EditCharacterAdminActivity.this.characterMapper = new CharacterMapper(this);

        EditCharacterAdminActivity.this.id = this.getIntent().getLongExtra("id", -1);
        EditCharacterAdminActivity.this.character = characterMapper.getCharacterById(id);
        EditCharacterAdminActivity.this.characterOld = new Character(EditCharacterAdminActivity.this.character.getName(), EditCharacterAdminActivity.this.character.getDescription());

        //Inicialización de eventos
        final Button BT_RESET = EditCharacterAdminActivity.this.findViewById(R.id.btnReset);
        BT_RESET.setOnClickListener(v -> EditCharacterAdminActivity.this.reset());

        final Button BT_EDIT = EditCharacterAdminActivity.this.findViewById(R.id.btEditCharacter);
        BT_EDIT.setOnClickListener(v -> EditCharacterAdminActivity.this.editCharacter());

        final EditText ED_DESC = EditCharacterAdminActivity.this.findViewById(R.id.edDescription);
        ED_DESC.setText(EditCharacterAdminActivity.this.character.getDescription());
        ED_DESC.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateDescription(ED_DESC.getText().toString());
            } catch (ValidationException e) {
                ED_DESC.setError(EditCharacterAdminActivity.this.getResources().getString(e.getError()));
            }
        });

        final EditText ED_NAME = EditCharacterAdminActivity.this.findViewById(R.id.edName);
        ED_NAME.setText(EditCharacterAdminActivity.this.character.getName());
        ED_NAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateName(ED_NAME.getText().toString());
                if (!EditCharacterAdminActivity.this.characterOld.getName().equals(character.getName()) && EditCharacterAdminActivity.this.characterMapper.thisCharacterExist(EditCharacterAdminActivity.this.character.getName())) {
                    ED_NAME.setError(getResources().getString(R.string.character_exists));
                }
            } catch (ValidationException e) {
                ED_NAME.setError(EditCharacterAdminActivity.this.getResources().getString(e.getError()));
            }
        });

        //Control de sesión
        if (!EditCharacterAdminActivity.this.session.isSessionActive() || !EditCharacterAdminActivity.this.session.isAdmin()) {
            EditCharacterAdminActivity.this.finish();
        }
    }

    private void reset() {
        final EditText ED_NAME = EditCharacterAdminActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditCharacterAdminActivity.this.findViewById(R.id.edDescription);

        ED_NAME.setText(characterOld.getName());
        ED_DESCRIPTION.setText(characterOld.getDescription());
    }

    private void editCharacter() {
        final EditText ED_NAME = EditCharacterAdminActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = EditCharacterAdminActivity.this.findViewById(R.id.edDescription);
        Toast.makeText(this, ED_NAME.getText().toString(), Toast.LENGTH_SHORT);
        EditCharacterAdminActivity.this.character.setName(ED_NAME.getText().toString());
        EditCharacterAdminActivity.this.character.setDescription(ED_DESCRIPTION.getText().toString());
        if (!EditCharacterAdminActivity.this.characterOld.getName().equals(EditCharacterAdminActivity.this.character.getName()) && EditCharacterAdminActivity.this.characterMapper.thisCharacterExist(EditCharacterAdminActivity.this.character.getName())) {
            Toast.makeText(EditCharacterAdminActivity.this, R.string.character_exists, Toast.LENGTH_SHORT).show();
        } else {
            try {
                EditCharacterAdminActivity.this.character.validateForUpdate();
                try {
                    EditCharacterAdminActivity.this.characterMapper.updateCharacter(character);
                    Toast.makeText(EditCharacterAdminActivity.this, R.string.character_edit_successful, Toast.LENGTH_SHORT).show();
                    EditCharacterAdminActivity.this.finish();
                } catch (RuntimeException ex) {
                    Toast.makeText(EditCharacterAdminActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
                }
            } catch (ValidationException e) {
                Toast.makeText(EditCharacterAdminActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (!EditCharacterAdminActivity.this.session.isSessionActive() || !EditCharacterAdminActivity.this.session.isAdmin()) {
            EditCharacterAdminActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        EditCharacterAdminActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                EditCharacterAdminActivity.this.session.closeSession();
                EditCharacterAdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                EditCharacterAdminActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                EditCharacterAdminActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        EditCharacterAdminActivity.this.startActivity(new Intent(EditCharacterAdminActivity.this, AboutActivity.class));
    }
}
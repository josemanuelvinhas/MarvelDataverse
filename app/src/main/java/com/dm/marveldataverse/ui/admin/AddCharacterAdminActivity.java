package com.dm.marveldataverse.ui.admin;

import androidx.appcompat.app.ActionBar;
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
import com.dm.marveldataverse.ui.AboutActivity;

public class AddCharacterAdminActivity extends AppCompatActivity {

    private Session session;
    private Character character;
    private CharacterMapper characterMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character_admin);

        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.add_character);

        //Inicialización de variables
        AddCharacterAdminActivity.this.character = new Character();
        AddCharacterAdminActivity.this.characterMapper = new CharacterMapper(AddCharacterAdminActivity.this);
        AddCharacterAdminActivity.this.session = Session.getSession(AddCharacterAdminActivity.this);

        final Button BT_RESET = AddCharacterAdminActivity.this.findViewById(R.id.btnReset);
        final Button BT_SEND = AddCharacterAdminActivity.this.findViewById(R.id.btnAddCharacter);

        final EditText ED_NAME = AddCharacterAdminActivity.this.findViewById(R.id.edName);
        final EditText ED_DESC = AddCharacterAdminActivity.this.findViewById(R.id.edDescription);

        BT_RESET.setOnClickListener(v -> AddCharacterAdminActivity.this.reset());
        BT_SEND.setOnClickListener(v -> AddCharacterAdminActivity.this.addCharacter());

        ED_NAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateName(ED_NAME.getText().toString());
                try {
                    if (AddCharacterAdminActivity.this.characterMapper.thisCharacterExist(ED_NAME.getText().toString())) {
                        ED_NAME.setError(getResources().getString(R.string.character_exists));
                    }
                } catch (RuntimeException ex) {
                    Log.e("BD", "Error BD, no se puede saber si existe");
                }
            } catch (ValidationException e) {
                ED_NAME.setError(AddCharacterAdminActivity.this.getResources().getString(e.getError()));
            }
        });

        ED_DESC.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                Character.validateDescription(ED_DESC.getText().toString());
            } catch (ValidationException e) {
                ED_DESC.setError(AddCharacterAdminActivity.this.getResources().getString(e.getError()));
            }
        });

        //Salir si no existe una sesión
        if (!AddCharacterAdminActivity.this.session.isSessionActive() || !AddCharacterAdminActivity.this.session.isAdmin()) {
            AddCharacterAdminActivity.this.finish();
        }
    }

    private void reset() {
        final EditText ED_NAME = AddCharacterAdminActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = AddCharacterAdminActivity.this.findViewById(R.id.edDescription);
        ED_DESCRIPTION.setText("");
        ED_NAME.setText("");
    }

    private void addCharacter() {
        final EditText ED_NAME = AddCharacterAdminActivity.this.findViewById(R.id.edName);
        final EditText ED_DESCRIPTION = AddCharacterAdminActivity.this.findViewById(R.id.edDescription);

        AddCharacterAdminActivity.this.character.setName(ED_NAME.getText().toString());
        AddCharacterAdminActivity.this.character.setDescription(ED_DESCRIPTION.getText().toString());

        if (AddCharacterAdminActivity.this.characterMapper.thisCharacterExist(AddCharacterAdminActivity.this.character.getName())) {
            Toast.makeText(AddCharacterAdminActivity.this, R.string.character_exists, Toast.LENGTH_SHORT).show();
        } else {
            try {
                this.character.validateForCreate();
                try {
                   long id = characterMapper.addCharacter(character);
                    Toast.makeText(AddCharacterAdminActivity.this, R.string.character_add_successful, Toast.LENGTH_SHORT).show();
                    AddCharacterAdminActivity.this.finish();
                    AddCharacterAdminActivity.this.startDetailCharacterActivity(id);
                } catch (RuntimeException ex) {
                    Toast.makeText(AddCharacterAdminActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
                }
            } catch (ValidationException e) {
                Toast.makeText(AddCharacterAdminActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si no existe una sesión
        if (!AddCharacterAdminActivity.this.session.isSessionActive() || !AddCharacterAdminActivity.this.session.isAdmin()) {
            AddCharacterAdminActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        AddCharacterAdminActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                AddCharacterAdminActivity.this.session.closeSession();
                AddCharacterAdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                AddCharacterAdminActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                AddCharacterAdminActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        this.startActivity(new Intent(AddCharacterAdminActivity.this, AboutActivity.class));
    }

    private void startDetailCharacterActivity(long id) {
        Intent intent = new Intent(AddCharacterAdminActivity.this, DetailCharacterAdminActivity.class);
        intent.putExtra("id", id);
        this.startActivity(intent);
    }
}
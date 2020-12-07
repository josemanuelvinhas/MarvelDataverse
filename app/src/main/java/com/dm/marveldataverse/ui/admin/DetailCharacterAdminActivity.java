package com.dm.marveldataverse.ui.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.ui.AboutActivity;

public class DetailCharacterAdminActivity extends AppCompatActivity {

    private Session session;

    private CharacterMapper characterMapper;

    private Character character;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character_admin);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.character_details);

        //Inicialización de atributos
        DetailCharacterAdminActivity.this.session = Session.getSession(DetailCharacterAdminActivity.this);

        DetailCharacterAdminActivity.this.characterMapper = new CharacterMapper(this);

        DetailCharacterAdminActivity.this.id = this.getIntent().getLongExtra("id", -1);
        DetailCharacterAdminActivity.this.character = characterMapper.getCharacterById(id);

        //Inicialización de eventos
        final Button BT_EDITAR = DetailCharacterAdminActivity.this.findViewById(R.id.btnEditCharacter);
        BT_EDITAR.setOnClickListener(v -> startEditCharacterActivity(id));

        final Button BT_ELIMINAR = DetailCharacterAdminActivity.this.findViewById(R.id.btnDelete);
        BT_ELIMINAR.setOnClickListener(v -> DetailCharacterAdminActivity.this.deleteCharacter());

        //Mostrar el personaje
        DetailCharacterAdminActivity.this.showCharacter();

        //Control de sesión
        if (!DetailCharacterAdminActivity.this.session.isSessionActive() || !DetailCharacterAdminActivity.this.session.isAdmin()) {
            DetailCharacterAdminActivity.this.finish();
        }
    }

    private void showCharacter() {
        final TextView TW_NAME = DetailCharacterAdminActivity.this.findViewById(R.id.edName);
        final TextView TW_DESCRIPTION = DetailCharacterAdminActivity.this.findViewById(R.id.edDescription);

        TW_NAME.setText(DetailCharacterAdminActivity.this.character.getName());
        TW_DESCRIPTION.setText(DetailCharacterAdminActivity.this.character.getDescription());
    }

    @Override
    protected void onResume() {
        super.onResume();
        DetailCharacterAdminActivity.this.character = DetailCharacterAdminActivity.this.characterMapper.getCharacterById(id);
        this.showCharacter();
        //Salir si existe una sesión
        if (!DetailCharacterAdminActivity.this.session.isSessionActive() || !DetailCharacterAdminActivity.this.session.isAdmin()) {
            DetailCharacterAdminActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        DetailCharacterAdminActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                DetailCharacterAdminActivity.this.session.closeSession();
                DetailCharacterAdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                DetailCharacterAdminActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                DetailCharacterAdminActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        DetailCharacterAdminActivity.this.startActivity(new Intent(DetailCharacterAdminActivity.this, AboutActivity.class));
    }

    private void startEditCharacterActivity(long id) {
        Intent intent = new Intent(DetailCharacterAdminActivity.this, EditCharacterAdminActivity.class);
        intent.putExtra("id", id);
        DetailCharacterAdminActivity.this.startActivity(intent);
    }

    private void deleteCharacter() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setTitle(R.string.delete);
        DLG.setMessage(R.string.delete_character_msg);
        DLG.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DetailCharacterAdminActivity.this.characterMapper.deleteCharacter(id);
                Toast.makeText(DetailCharacterAdminActivity.this,R.string.character_remove_successful,Toast.LENGTH_SHORT).show();
                DetailCharacterAdminActivity.this.finish();
            }
        });

        DLG.setNegativeButton(R.string.no,null);

        DLG.create().show();
    }
}
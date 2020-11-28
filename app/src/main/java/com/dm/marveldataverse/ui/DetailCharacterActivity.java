package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character);
        DetailCharacterActivity.this.session = Session.getSession(DetailCharacterActivity.this);

        //inicializar las variables
        DetailCharacterActivity.this.id = this.getIntent().getLongExtra("id", -1);
        DetailCharacterActivity.this.characterMapper = new CharacterMapper(this);
        DetailCharacterActivity.this.character = characterMapper.getCharacterById(id);
        final Button BT_EDITAR = DetailCharacterActivity.this.findViewById(R.id.btnEditCharacter);
        final Button BT_ELIMINAR = DetailCharacterActivity.this.findViewById(R.id.btnDelete);

        BT_EDITAR.setOnClickListener(v -> startEditCharacterActivity(id));

        BT_ELIMINAR.setOnClickListener(v -> DetailCharacterActivity.this.deleteCharacter());


        DetailCharacterActivity.this.show();

        //Salir si existe una sesión
        if (!DetailCharacterActivity.this.session.isSessionActive()) {
            DetailCharacterActivity.this.finish();
        }

    }

    private void show() {
        final TextView TW_NAME = DetailCharacterActivity.this.findViewById(R.id.edName);
        final TextView TW_DESCRIPTION = DetailCharacterActivity.this.findViewById(R.id.edDescription);

        TW_NAME.setText(DetailCharacterActivity.this.character.getName());
        TW_DESCRIPTION.setText(DetailCharacterActivity.this.character.getDescription());
    }

    @Override
    protected void onResume() {
        super.onResume();
        DetailCharacterActivity.this.character = DetailCharacterActivity.this.characterMapper.getCharacterById(id);
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
        DetailCharacterActivity.this.startActivity(new Intent(DetailCharacterActivity.this, AboutActivity.class));
    }

    private void startEditCharacterActivity(long id) {
        Intent intent = new Intent(DetailCharacterActivity.this, EditCharacterActivity.class);
        intent.putExtra("id", id);
        DetailCharacterActivity.this.startActivity(intent);
    }

    private void deleteCharacter() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setTitle(R.string.delete);
        DLG.setMessage(R.string.delete_character_msg);
        DLG.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DetailCharacterActivity.this.characterMapper.deleteCharacter(id);
                Toast.makeText(DetailCharacterActivity.this,R.string.character_remove_successful,Toast.LENGTH_SHORT).show();
                DetailCharacterActivity.this.finish();
            }
        });

        DLG.setNegativeButton(R.string.no,null);

        DLG.create().show();
    }
}
package com.dm.marveldataverse.ui.user;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.ComentUserCursorAdapter;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.model.Comment;
import com.dm.marveldataverse.model.CommentMapper;
import com.dm.marveldataverse.model.Fav;
import com.dm.marveldataverse.model.FavMapper;
import com.dm.marveldataverse.ui.AboutActivity;

public class DetailCharacterUser extends AppCompatActivity {

    private Session session;

    private CharacterMapper characterMapper;
    private CommentMapper commentMapper;
    private FavMapper favMapper;

    private Character character;
    private Comment comment;

    private long idFav;

    private ComentUserCursorAdapter commentUserCursorAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character_user);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.character_details);

        //Inicialización de atributos
        DetailCharacterUser.this.session = Session.getSession(DetailCharacterUser.this);

        DetailCharacterUser.this.characterMapper = new CharacterMapper(this);
        DetailCharacterUser.this.commentMapper = new CommentMapper(DetailCharacterUser.this);
        DetailCharacterUser.this.favMapper = new FavMapper(DetailCharacterUser.this);

        DetailCharacterUser.this.character = characterMapper.getCharacterById(DetailCharacterUser.this.getIntent().getLongExtra("id", -1));
        DetailCharacterUser.this.comment = new Comment();

        DetailCharacterUser.this.idFav = this.favMapper.isFav(DetailCharacterUser.this.character.getId(), DetailCharacterUser.this.session.getUsername());

        DetailCharacterUser.this.cursor = commentMapper.getCommentList(DetailCharacterUser.this.character.getId());
        DetailCharacterUser.this.commentUserCursorAdapter = new ComentUserCursorAdapter(DetailCharacterUser.this, DetailCharacterUser.this.cursor);

        //Inicialización de eventos
        //Eventos de comentar
        final Button BT_COMMENT = DetailCharacterUser.this.findViewById(R.id.btComment);
        BT_COMMENT.setOnClickListener(v -> DetailCharacterUser.this.commentDialog(""));

        //Eventos de ListView de Comentarios
        final ListView LV_COMMENTS = DetailCharacterUser.this.findViewById(R.id.lvComment);

        LV_COMMENTS.setAdapter(DetailCharacterUser.this.commentUserCursorAdapter);

        LV_COMMENTS.setOnItemLongClickListener((parent, view, position, id) -> {
            if (DetailCharacterUser.this.cursor.moveToFirst()) {
                DetailCharacterUser.this.cursor.move(position);
                if (DetailCharacterUser.this.cursor.getString(DetailCharacterUser.this.cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO_USUARIO)).equals(DetailCharacterUser.this.session.getUsername())) {
                    DetailCharacterUser.this.deleteDialog(DetailCharacterUser.this.cursor.getLong(DetailCharacterUser.this.cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO_ID)));
                }
            }
            return false;
        });

        //Eventos de Fav
        final ImageView IV_FAV = DetailCharacterUser.this.findViewById(R.id.ivFav);

        if (this.idFav != -1) {
            IV_FAV.setImageResource(R.drawable.ic_estrella_llena);
        } else {
            IV_FAV.setImageResource(R.drawable.ic_estrella_vacia);
        }

        IV_FAV.setOnClickListener(v -> {
            if (DetailCharacterUser.this.idFav == -1) {
                DetailCharacterUser.this.idFav = DetailCharacterUser.this.favMapper.addFav(new Fav(DetailCharacterUser.this.session.getUsername(), DetailCharacterUser.this.character.getId()));
                IV_FAV.setImageResource(R.drawable.ic_estrella_llena);
            } else {
                DetailCharacterUser.this.favMapper.deleteFav(this.idFav);
                DetailCharacterUser.this.idFav = -1;
                IV_FAV.setImageResource(R.drawable.ic_estrella_vacia);
            }
        });

        //Mostrar la informacion del personaje
        DetailCharacterUser.this.showCharacter();


        //Control de sesión
        if (!DetailCharacterUser.this.session.isSessionActive()) {
            DetailCharacterUser.this.finish();
        }
    }

    //Diálogo para realizar un comentario
    private void commentDialog(String comentario) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        final EditText ED_COMMENT = new EditText(this);

        ED_COMMENT.setText(comentario);

        DLG.setTitle(R.string.enter_comment);
        DLG.setView(ED_COMMENT);

        DLG.setPositiveButton(R.string.ok, null);

        DLG.setNegativeButton(R.string.cancel, null);
        AlertDialog ALERT_DLG = DLG.create();

        ALERT_DLG.setOnShowListener(dialog -> {
            Button b = ALERT_DLG.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> {
                try {
                    DetailCharacterUser.this.comment.setCharacter(DetailCharacterUser.this.character.getId());
                    DetailCharacterUser.this.comment.setComment(ED_COMMENT.getText().toString());
                    DetailCharacterUser.this.comment.setUser(DetailCharacterUser.this.session.getUsername());
                    DetailCharacterUser.this.comment.validateComment();
                    DetailCharacterUser.this.commentMapper.addComment(DetailCharacterUser.this.comment);
                    DetailCharacterUser.this.refresh();
                    ALERT_DLG.dismiss();
                } catch (ValidationException e) {
                    Toast.makeText(DetailCharacterUser.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
                    ED_COMMENT.setError(DetailCharacterUser.this.getResources().getString(e.getError()));
                }
            });
        });

        ALERT_DLG.show();
    }

    //Actualizar el ListView de comentarios
    private void refresh() {
        DetailCharacterUser.this.cursor = DetailCharacterUser.this.commentMapper.getCommentList(DetailCharacterUser.this.character.getId());
        DetailCharacterUser.this.commentUserCursorAdapter.swapCursor(DetailCharacterUser.this.cursor);
    }

    //Mostrar la informacion del personaje
    private void showCharacter() {
        final TextView TV_NAME = DetailCharacterUser.this.findViewById(R.id.edName);
        final TextView TV_DESCRIPTION = DetailCharacterUser.this.findViewById(R.id.edDescription);

        TV_NAME.setText(DetailCharacterUser.this.character.getName());
        TV_DESCRIPTION.setText(DetailCharacterUser.this.character.getDescription());
    }

    //Borrar un comentario
    private void deleteDialog(long id_comment) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(DetailCharacterUser.this);
        DLG.setTitle(R.string.delete);
        DLG.setMessage(R.string.delete_comment_msg);
        DLG.setNegativeButton(R.string.no, null);
        DLG.setPositiveButton(R.string.yes, (dialog, which) -> {
            DetailCharacterUser.this.commentMapper.deleteComment(id_comment);
            DetailCharacterUser.this.refresh();
        });

        DLG.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        DetailCharacterUser.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                DetailCharacterUser.this.session.closeSession();
                DetailCharacterUser.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                DetailCharacterUser.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                DetailCharacterUser.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        DetailCharacterUser.this.startActivity(new Intent(DetailCharacterUser.this, AboutActivity.class));
    }
}
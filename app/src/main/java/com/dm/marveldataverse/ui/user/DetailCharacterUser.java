package com.dm.marveldataverse.ui.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.ComentUserCursorAdapter;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.CharacterMapper;
import com.dm.marveldataverse.model.Comment;
import com.dm.marveldataverse.model.CommentMapper;
import com.dm.marveldataverse.ui.AboutActivity;

public class DetailCharacterUser extends AppCompatActivity {

    private CharacterMapper characterMapper;
    private Session session;
    private Character character;
    private long id;
    private Comment comment;
    private CommentMapper commentMapper;
    private ComentUserCursorAdapter commentUserCursorAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character_user);

        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.character_details);


        DetailCharacterUser.this.session = Session.getSession(DetailCharacterUser.this);
        DetailCharacterUser.this.id = this.getIntent().getLongExtra("id", -1);
        DetailCharacterUser.this.characterMapper = new CharacterMapper(this);
        DetailCharacterUser.this.character = characterMapper.getCharacterById(id);
        DetailCharacterUser.this.commentMapper = new CommentMapper(DetailCharacterUser.this);
        DetailCharacterUser.this.cursor = commentMapper.getCommentList(DetailCharacterUser.this.id);
        DetailCharacterUser.this.comment = new Comment();
        DetailCharacterUser.this.commentUserCursorAdapter = new ComentUserCursorAdapter(this, DetailCharacterUser.this.cursor);
        final Button BT_COMMENT = this.findViewById(R.id.btComment);
        final ListView LV_COMMENTS = this.findViewById(R.id.lvCommets);
        LV_COMMENTS.setAdapter(DetailCharacterUser.this.commentUserCursorAdapter);

        BT_COMMENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailCharacterUser.this.comment();
            }
        });

        LV_COMMENTS.setOnItemLongClickListener((parent, view, position, id) -> {
            if (DetailCharacterUser.this.cursor.moveToFirst()) {
                DetailCharacterUser.this.cursor.move(position);
                if (DetailCharacterUser.this.cursor.getString(DetailCharacterUser.this.cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO_USUARIO)).equals(DetailCharacterUser.this.session.getUsername())) {
                    DetailCharacterUser.this.delete(DetailCharacterUser.this.cursor.getLong(DetailCharacterUser.this.cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO_ID)));
                }
            }
            return false;
        });

        DetailCharacterUser.this.show();

        //Salir si existe una sesión
        if (!DetailCharacterUser.this.session.isSessionActive()) {
            DetailCharacterUser.this.finish();
        }
    }

    private void comment() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        final EditText edComment = new EditText(this);
        DLG.setTitle(R.string.enter_comment);
        DLG.setView(edComment);
        DLG.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comment.setCharacter(character.getId());
                comment.setComment(edComment.getText().toString());
                comment.setUser(DetailCharacterUser.this.session.getUsername());
                commentMapper.addComment(comment);
                DetailCharacterUser.this.refresh();
            }
        });
        DLG.setNegativeButton(R.string.cancel, null);
        DLG.create().show();
    }

    private void refresh() {
        DetailCharacterUser.this.cursor = commentMapper.getCommentList(DetailCharacterUser.this.id);
        DetailCharacterUser.this.commentUserCursorAdapter.swapCursor(cursor);
    }

    private void show() {
        final TextView TW_NAME = DetailCharacterUser.this.findViewById(R.id.edName);
        final TextView TW_DESCRIPTION = DetailCharacterUser.this.findViewById(R.id.edDescription);

        TW_NAME.setText(DetailCharacterUser.this.character.getName());
        TW_DESCRIPTION.setText(DetailCharacterUser.this.character.getDescription());
    }

    private void delete(long id_comment) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
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
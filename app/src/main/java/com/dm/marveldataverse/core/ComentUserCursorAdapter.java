package com.dm.marveldataverse.core;

import android.content.Context;
import android.database.Cursor;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.model.Character;
import com.dm.marveldataverse.model.Comment;

import java.util.ArrayList;

public class ComentUserCursorAdapter extends CursorAdapter {

    public ComentUserCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.entry_comment_user, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView TV_USER = view.findViewById(R.id.lblUser);
        final TextView TV_COMMENT = view.findViewById(R.id.lblComment);


        TV_USER.setText(cursor.getString(cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO_USUARIO)));
        TV_COMMENT.setText(cursor.getString(cursor.getColumnIndex(DBManager.CAMPO_COMENTARIO)));

    }


}
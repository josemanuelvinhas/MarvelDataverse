package com.dm.marveldataverse.core;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dm.marveldataverse.R;

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
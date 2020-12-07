package com.dm.marveldataverse.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.dm.marveldataverse.core.DBManager.CAMPO_COMENTARIO;
import static com.dm.marveldataverse.core.DBManager.CAMPO_COMENTARIO_ID;
import static com.dm.marveldataverse.core.DBManager.CAMPO_COMENTARIO_PERSONAJE;
import static com.dm.marveldataverse.core.DBManager.CAMPO_COMENTARIO_USUARIO;
import static com.dm.marveldataverse.core.DBManager.TABLA_COMENTARIOS;

public class CommentMapper extends BaseMapper {
    /**
     * El método constructor obtiene la instancia de conexión a la base de datos
     *
     * @param context El contexto de la aplicación
     */
    public CommentMapper(Context context) {
        super(context);
    }

    /**
     * Este método permite insertar comentarios en la BD
     *
     * @param comment El personaje
     * @throws RuntimeException si se produce algun error en la BD
     */

    public long addComment(Comment comment) {
        final SQLiteDatabase DB = instance.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();
        long id = -1;

        VALORES.put(CAMPO_COMENTARIO_PERSONAJE, comment.getCharacter());
        VALORES.put(CAMPO_COMENTARIO, comment.getComment());
        VALORES.put(CAMPO_COMENTARIO_USUARIO, comment.getUser());

        try {
            Log.i("DB", "insertando comentario: " + comment.getComment());
            DB.beginTransaction();
            id = DB.insertOrThrow(
                    TABLA_COMENTARIOS,
                    null,
                    VALORES
            );

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        } finally {
            DB.endTransaction();
        }

        return id;
    }

    /**
     * Este método permite modificar personajes en la BD
     *
     * @param character El personaje
     * @throws RuntimeException si se produce algun error en la BD


    public void updateCharacter(Character character) {
    final SQLiteDatabase DB = instance.getWritableDatabase();
    final ContentValues VALORES = new ContentValues();

    String[] args = new String[]{Long.toString(character.getId())};

    VALORES.put(CAMPO_PERSONAJES_NAME, character.getName());
    VALORES.put(CAMPO_PERSONAJES_DESCRIPTION, character.getDescription());

    try {
    Log.i("DB", "actualizando personaje: " + character.getName());
    DB.beginTransaction();

    DB.update(TABLA_PERSONAJES, VALORES, CAMPO_PERSONAJES_ID + "=?", args);

    DB.setTransactionSuccessful();
    } catch (SQLException error) {
    Log.e("DB", error.getMessage());
    throw new RuntimeException("Error en la BD");
    } finally {
    DB.endTransaction();
    }
    }
     */


    /**
     * Este método permite eliminar comentatios de la BD
     *
     * @param id El id del comentario
     * @throws RuntimeException si se produce algun error en la BD
     */

    public void deleteComment(long id) {
        final SQLiteDatabase DB = instance.getWritableDatabase();

        String[] args = new String[]{Long.toString(id)};

        try {
            Log.i("DB", "eliminando comentario con ID: " + id);
            DB.beginTransaction();

            DB.delete(TABLA_COMENTARIOS, CAMPO_COMENTARIO_ID + "=?", args);

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        } finally {
            DB.endTransaction();
        }
    }


    /**
     * Este método devuelve los comentarios de un personaje.
     *
     * @return lista de comentarios
     * @throws RuntimeException si se produce algun error en la BD
     */

    public Cursor getCommentList(long character_id) {
        final SQLiteDatabase DB = instance.getReadableDatabase();
        String[] args = new String[]{Long.toString(character_id)};
        Log.i("DB", "recuperando lista de todos los comentarios de un personaje: ");

        Cursor cursor = DB.query(TABLA_COMENTARIOS, null, CAMPO_COMENTARIO_PERSONAJE + "=?", args, null, null, null, null);

        return cursor;
    }


/**
 public Character getCharacterById(long id) {
 final SQLiteDatabase DB = instance.getReadableDatabase();
 Character character = null;
 Log.i("DB", "recuperando un personaje por su id: " + id);

 String[] args = new String[]{Long.toString(id)};

 try (Cursor cursor = DB.query(TABLA_PERSONAJES, null, CAMPO_PERSONAJES_ID + " = ?", args, null, null, null, null)) {
 if (cursor.moveToFirst()) {
 character = new Character(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
 }
 }

 return character;
 }
 **/

}

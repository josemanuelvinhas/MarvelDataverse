package com.dm.marveldataverse.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_ID;
import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_PERSONAJE;
import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_USUARIO;
import static com.dm.marveldataverse.core.DBManager.TABLA_FAVS;

public class FavMapper extends BaseMapper {
    /**
     * El método constructor obtiene la instancia de conexión a la base de datos
     *
     * @param context El contexto de la aplicación
     */
    public FavMapper(Context context) {
        super(context);
    }

    /**
     * Este método permite insertar favs en la BD
     *
     * @param fav
     * @throws RuntimeException si se produce algun error en la BD
     */

    public long addFav(Fav fav) {
        final SQLiteDatabase DB = instance.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();
        long id = -1;

        VALORES.put(CAMPO_FAV_PERSONAJE, fav.getCharacter());
        VALORES.put(CAMPO_FAV_USUARIO, fav.getUser());


        try {
            Log.i("DB", "insertando fav: ");
            DB.beginTransaction();
            id = DB.insertOrThrow(
                    TABLA_FAVS,
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
     * Este método permite eliminar comentatios de la BD
     *
     * @param id El id del fav
     * @throws RuntimeException si se produce algun error en la BD
     */

    public void deleteFav(long id) {
        final SQLiteDatabase DB = instance.getWritableDatabase();

        String[] args = new String[]{Long.toString(id)};

        try {
            Log.i("DB", "eliminando fav con ID: " + id);
            DB.beginTransaction();

            DB.delete(TABLA_FAVS, CAMPO_FAV_ID + "=?", args);

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        } finally {
            DB.endTransaction();
        }
    }


    /**
     * Este método devuelve los favs de un usuario.
     *
     * @return lista de favs
     * @throws RuntimeException si se produce algun error en la BD
     */

    public Cursor getFavList(long user_id) {
        final SQLiteDatabase DB = instance.getReadableDatabase();
        String[] args = new String[]{Long.toString(user_id)};
        Log.i("DB", "recuperando lista de todos los favs de un usuario: ");

        Cursor cursor = DB.query(TABLA_FAVS, new String[]{CAMPO_FAV_PERSONAJE}, CAMPO_FAV_USUARIO + "=?", args, null, null, null, null);

        return cursor;
    }


}

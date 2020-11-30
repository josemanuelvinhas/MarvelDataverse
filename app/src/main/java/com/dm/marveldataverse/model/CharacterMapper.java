package com.dm.marveldataverse.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_ID;
import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_PERSONAJE;
import static com.dm.marveldataverse.core.DBManager.CAMPO_FAV_USUARIO;
import static com.dm.marveldataverse.core.DBManager.CAMPO_PERSONAJES_ID;
import static com.dm.marveldataverse.core.DBManager.CAMPO_PERSONAJES_NAME;
import static com.dm.marveldataverse.core.DBManager.CAMPO_PERSONAJES_DESCRIPTION;
import static com.dm.marveldataverse.core.DBManager.TABLA_FAVS;
import static com.dm.marveldataverse.core.DBManager.TABLA_PERSONAJES;

public class CharacterMapper extends BaseMapper {
    /**
     * El método constructor obtiene la instancia de conexión a la base de datos
     *
     * @param context El contexto de la aplicación
     */
    public CharacterMapper(Context context) {
        super(context);
    }

    /**
     * Este método permite insertar personajes en la BD
     *
     * @param character El personaje
     * @throws RuntimeException si se produce algun error en la BD
     */

    public long addCharacter(Character character) {
        final SQLiteDatabase DB = instance.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();
        long id = -1;

        VALORES.put(CAMPO_PERSONAJES_NAME, character.getName());
        VALORES.put(CAMPO_PERSONAJES_DESCRIPTION, character.getDescription());

        try {
            Log.i("DB", "insertando personaje: " + character.getName());
            DB.beginTransaction();
            id = DB.insertOrThrow(
                    TABLA_PERSONAJES,
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
     */

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

    /**
     * Este método permite eliminar personajes de la BD
     *
     * @param id El id del personaje
     * @throws RuntimeException si se produce algun error en la BD
     */

    public void deleteCharacter(long id) {
        final SQLiteDatabase DB = instance.getWritableDatabase();

        String[] args = new String[]{Long.toString(id)};

        try {
            Log.i("DB", "eliminando personaje con ID: " + id);
            DB.beginTransaction();

            DB.delete(TABLA_PERSONAJES, CAMPO_PERSONAJES_ID + "=?", args);

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        } finally {
            DB.endTransaction();
        }
    }


    /**
     * Este método permite saber si un personaje ya existe.
     *
     * @param name El nombre de personaje
     * @return true si existe el personaje y false si no
     * @throws RuntimeException si se produce algun error en la BD
     */
    public boolean thisCharacterExist(String name) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{name};

        boolean toret;
        try {
            Log.i("DB", "buscando personaje: " + name);

            try (Cursor cursor = DB.query(TABLA_PERSONAJES, null, CAMPO_PERSONAJES_NAME + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }
        return toret;
    }

    /**
     * Este método devuelve todos los personajes.
     *
     * @return lista de personajes
     * @throws RuntimeException si se produce algun error en la BD
     */

    public Cursor getCharactersList() {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        Log.i("DB", "recuperando lista de todos los personajes: ");

        Cursor cursor = DB.query(TABLA_PERSONAJES, new String[]{CAMPO_PERSONAJES_ID, CAMPO_PERSONAJES_NAME}, null, null, null, null, CAMPO_PERSONAJES_NAME + " ASC", null);

        return cursor;
    }


    /**
     * Este método busca a uno o varios personaje.
     *
     * @return una lista de personajes que coincidan con el criterio de busqueda
     * @throws RuntimeException si se produce algun error en la BD
     */
    public Cursor searchCharacter(String character) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{"%" + character + "%"};

        Log.i("DB", "recuperando lista de personajes buscados: ");

        Cursor cursor = DB.query(TABLA_PERSONAJES, null, CAMPO_PERSONAJES_NAME + " LIKE ?", args, null, null, CAMPO_PERSONAJES_NAME + " ASC", null);

        return cursor;
    }

    /**
     * Este método busca a uno o varios personaje.
     *
     * @return una lista de personajes que coincidan con el criterio de busqueda
     * @throws RuntimeException si se produce algun error en la BD
     */
    public ArrayList<Pair<Character, Long>> searchCharacterWithFav(String character, String username) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] argsFav = new String[]{username};
        Map<Long,Long> favs = new HashMap<>();
        try (Cursor cursor = DB.query(TABLA_FAVS, null, CAMPO_FAV_USUARIO + " = ?", argsFav, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    long tempChar=cursor.getLong(cursor.getColumnIndex(CAMPO_FAV_PERSONAJE));
                    long tempIdFav=cursor.getLong(cursor.getColumnIndex(CAMPO_FAV_ID));
                    favs.put(tempChar,tempIdFav);
                } while (cursor.moveToNext());
            }
        }

        String[] argsCharacter = new String[]{"%" + character + "%"};
        ArrayList<Pair<Character, Long>> toret = new ArrayList<>();
        try (Cursor cursor = DB.query(TABLA_PERSONAJES, null, CAMPO_PERSONAJES_NAME + " LIKE ?", argsCharacter, null, null, CAMPO_PERSONAJES_NAME + " ASC", null)) {
            if (cursor.moveToFirst()) {
                do {
                    String nameCharacter = cursor.getString(cursor.getColumnIndex(CAMPO_PERSONAJES_NAME));
                    long idCharacter = cursor.getLong(cursor.getColumnIndex(CAMPO_PERSONAJES_ID));
                    String descriptionCharacter = cursor.getString(cursor.getColumnIndex(CAMPO_PERSONAJES_DESCRIPTION));

                    if (favs.containsKey(idCharacter)){
                        toret.add(new Pair<>(new Character(nameCharacter,descriptionCharacter, idCharacter),favs.get(idCharacter)));
                    }else{
                        toret.add(new Pair<>(new Character(nameCharacter,descriptionCharacter, idCharacter),new Long(-1)));
                    }
                } while (cursor.moveToNext());
            }
        }
        return toret;
    }

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


}

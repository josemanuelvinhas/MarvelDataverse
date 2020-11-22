package com.dm.marveldataverse.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.dm.marveldataverse.core.DBManager.CAMPO_PERSONAJES_NAME;
import static com.dm.marveldataverse.core.DBManager.CAMPO_PERSONAJES_DESCRIPTION;
import static com.dm.marveldataverse.core.DBManager.TABLA_PERSONAJES;

public class CharacterMapper extends BaseMapper {
    /**
     * El método constructor obtiene la instancia de conexión a la base de datos
     *
     * @param context El contexto de la aplicación
     */
    protected CharacterMapper(Context context) {
        super(context);
    }

    /**
     * Este método permite insertar personajes en la BD
     *
     * @param character El personaje
     * @throws RuntimeException si se produce algun error en la BD
     */

    public void addCharacter(Character character) {
        final SQLiteDatabase DB = instance.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_PERSONAJES_NAME, character.getName());
        VALORES.put(CAMPO_PERSONAJES_DESCRIPTION, character.getDescription());

        try {
            Log.i("DB", "insertando personaje: " + character.getName());
            DB.beginTransaction();
            DB.insertOrThrow(
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

        String[] args = new String[]{character.getName(), character.getDescription()};

        VALORES.put(CAMPO_PERSONAJES_NAME, character.getName());
        VALORES.put(CAMPO_PERSONAJES_DESCRIPTION, character.getDescription());

        try {
            Log.i("DB", "actualizando personaje: " + character.getName());
            DB.beginTransaction();

            DB.update(TABLA_PERSONAJES, VALORES, CAMPO_PERSONAJES_NAME + "=? AND " + CAMPO_PERSONAJES_DESCRIPTION + "=?", args);

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        } finally {
            DB.endTransaction();
        }

        /** TODO Esto pone en los apuntes que es realizar una inserción o actualización en
         función de que el registro se encuentre o no.
         Cursor cursor = null;
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put( ESTUDIANTES_NOMBRE, nombre );
         values.put( ESTUDIANTES_DNI, dni );
         try {
         db.beginTransaction();
         cursor = db.query( TABLA_ESTUDIANTES, null, ESTUDIANTES_DNI + "=?",
         new String[]{ Integer.toString( dni ) },
         null, null, null, null );
         if ( cursor.getCount() > 0 ) {
         db.update( TABLA_ESTUDIANTES,
         values, ESTUDIANTES_NOMBRE + "= ?", new String[]{ nombre } );
         } else {
         db.insert( TABLA_ESTUDIANTES, null, values );
         }
         db.setTransactionSuccessful();
         } catch(SQLException exc) {
         Log.e( "DBManager.guarda", exc.getMessage() );
         } finally {
         if ( cursor != null ) {
         cursor.close();
         }
         db.endTransaction();
         }
         **/

    }

    /**
     * Este método permite eliminar personajes de la BD
     * @param character El personaje
     * @throws RuntimeException si se produce algun error en la BD
     */

    public void deleteCharacter(Character character) {
        final SQLiteDatabase DB = instance.getWritableDatabase();

        String[] args = new String[]{character.getName()};

        try {
            Log.i("DB", "eliminando personaje: " + character.getName());
            DB.beginTransaction();

            DB.delete( TABLA_PERSONAJES, CAMPO_PERSONAJES_NAME + "=?",args);

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
     * @param character El nombre de personaje
     * @return true si existe el personaje y false si no
     * @throws RuntimeException si se produce algun error en la BD
     */
    public boolean thisCharacterExist(String character) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{character};

        boolean toret;
        try {
            Log.i("DB", "buscando personaje: " + character);

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
     * @return lista de personajes
     * @throws RuntimeException si se produce algun error en la BD
     */
    public String[] getCharactersList() {
        final SQLiteDatabase DB = instance.getReadableDatabase();
        //TODO si se quiere ordenada poner en orderBy new String[]{CAMPO_PERSONAJES_NAME} + "DESC"
         String[] toret = new String[]{};
         int pos = 0;

        try {
            Log.i("DB", "recuperando lista de todos los personajes: ");

            try ( Cursor cursor = DB.query( TABLA_PERSONAJES, new String[]{CAMPO_PERSONAJES_NAME}, null, null, null, null, null, null );) {
                if ( cursor.moveToFirst() ) {
                    do {
                        toret[pos] = cursor.getString( 0 ); //TODO se puede poner el nombre del campo?
                        pos++;
                    } while ( cursor.moveToNext() );
                }
            }
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }
        return toret;
    }

    /**
     * Este método busca a uno o varios personaje.
     * @return una lista de personajes que coincidan con el criterio de busqueda
     * @throws RuntimeException si se produce algun error en la BD
     */
    public Character[] searchCharacter(String character) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{character};
        Character[] toret = new Character[]{};
        int pos=0;

        try {
            Log.i("DB", "recuperando lista de personajes buscados: ");

            try ( Cursor cursor = DB.query( TABLA_PERSONAJES, null, CAMPO_PERSONAJES_NAME + "LIKE ?", args, null, null, null, null );) {
                if ( cursor.moveToFirst() ) {
                    do {
                        Character c = new Character(cursor.getString( 0),cursor.getString( 1));//TODO se puede poner el nombre del campo?
                        toret[pos] = c;
                        pos++;
                    } while ( cursor.moveToNext() );
                }
            }
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }
        return toret;
    }


}

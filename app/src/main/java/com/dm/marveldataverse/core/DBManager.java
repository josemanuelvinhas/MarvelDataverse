package com.dm.marveldataverse.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * DBManager representa la conexión a la base de datos. Como a la aplicación solo accede un usuario
 * a la vez se emplea el patrón Singleton para que solo exista una instancia.
 */
public class DBManager extends SQLiteOpenHelper {

    private static DBManager instance;

    private static final String DB_NOMBRE = "Marvel_Dataverse";
    private static final int DB_VERSION = 1;

    public static final String TABLA_USUARIOS = "users";
    public static final String CAMPO_USUARIOS_USERNAME = "username";
    public static final String CAMPO_USUARIOS_PASSWD = "passwd";
    public static final String CAMPO_USUARIOS_EMAIL = "email";

    /**
     * Este método devuelve la instancia de conexión con la base de datos. Si no existe, la crea.
     * @param context El contexto de la applicación
     * @return La instancia de conexión con la base de datos
     */
    public static DBManager getManager(Context context) {
        if (instance == null) {
            instance = new DBManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Es el método constructor de la conexión con la base da datos.
     * @param context El contexto de la applicación
     */
    private DBManager(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }

    /**
     * Este método crea la base de datos
     * @param db La base de datos a crear
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(DB_NOMBRE, "Creando tablas");
            db.beginTransaction();

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_USUARIOS
                    + "("
                    + CAMPO_USUARIOS_USERNAME + " TEXT PRIMARY KEY,"
                    + CAMPO_USUARIOS_PASSWD + " TEXT NOT NULL,"
                    + CAMPO_USUARIOS_EMAIL + " TEXT NOT NULL UNIQUE"
                    + ")"
            );

            db.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NOMBRE, error.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Este método borra la base de datos cuando se va a actualizar
     * @param db La base de datos a crear
     * @param oldVersion Número de versión vieja de la base de datos
     * @param newVersion Número de versión nueva de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i(DB_NOMBRE, "Actualizando DB");
            db.beginTransaction();

            db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);

            db.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NOMBRE, error.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }


}

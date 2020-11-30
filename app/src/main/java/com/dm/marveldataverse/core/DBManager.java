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
    public static final String CAMPO_USUARIOS_IS_ADMIN = "isAdmin";

    public static final String TABLA_PERSONAJES = "characters";
    public static final String CAMPO_PERSONAJES_ID = "_id";
    public static final String CAMPO_PERSONAJES_NAME = "name";
    public static final String CAMPO_PERSONAJES_DESCRIPTION = "description";

    public static final String TABLA_COMENTARIOS = "comments";
    public static final String CAMPO_COMENTARIO_ID = "_id";
    public static final String CAMPO_COMENTARIO_USUARIO = "user";
    public static final String CAMPO_COMENTARIO = "comment";
    public static final String CAMPO_COMENTARIO_PERSONAJE = "character";

    public static final String TABLA_FAVS = "favs";
    public static final String CAMPO_FAV_ID = "_id";
    public static final String CAMPO_FAV_PERSONAJE = "character";
    public static final String CAMPO_FAV_USUARIO = "user";

    /**
     * Este método devuelve la instancia de conexión con la base de datos. Si no existe, la crea.
     *
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
     *
     * @param context El contexto de la applicación
     */
    private DBManager(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * Este método crea la base de datos
     *
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
                    + CAMPO_USUARIOS_EMAIL + " TEXT NOT NULL UNIQUE,"
                    + CAMPO_USUARIOS_IS_ADMIN + " INTEGER NOT NULL"
                    + ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_PERSONAJES
                    + "("
                    + CAMPO_PERSONAJES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CAMPO_PERSONAJES_NAME + " TEXT NOT NULL UNIQUE,"
                    + CAMPO_PERSONAJES_DESCRIPTION + " TEXT NOT NULL"
                    + ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_COMENTARIOS
                    + "("
                    + CAMPO_COMENTARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CAMPO_COMENTARIO + " TEXT NOT NULL,"
                    + CAMPO_COMENTARIO_PERSONAJE + " INTEGER NOT NULL,"
                    + CAMPO_COMENTARIO_USUARIO + " TEXT NOT NULL,"
                    + "FOREIGN KEY(" + CAMPO_COMENTARIO_PERSONAJE + ") REFERENCES " + TABLA_PERSONAJES + "(" + CAMPO_PERSONAJES_ID + "),"
                    + "FOREIGN KEY(" + CAMPO_COMENTARIO_USUARIO + ") REFERENCES " + TABLA_USUARIOS + "(" + CAMPO_USUARIOS_USERNAME + ")"
                    + ")"
            );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_FAVS
                    + "("
                    + CAMPO_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CAMPO_FAV_PERSONAJE + " INTEGER NOT NULL ,"
                    + CAMPO_FAV_USUARIO + " TEXT NOT NULL,"
                    + "FOREIGN KEY(" + CAMPO_FAV_PERSONAJE + ") REFERENCES " + TABLA_PERSONAJES + "(" + CAMPO_PERSONAJES_ID + "),"
                    + "FOREIGN KEY(" + CAMPO_FAV_USUARIO + ") REFERENCES " + TABLA_USUARIOS + "(" + CAMPO_USUARIOS_USERNAME + ")"
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
     *
     * @param db         La base de datos a crear
     * @param oldVersion Número de versión vieja de la base de datos
     * @param newVersion Número de versión nueva de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i(DB_NOMBRE, "Actualizando DB");
            db.beginTransaction();

            db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERSONAJES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMENTARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_FAVS);

            db.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NOMBRE, error.getMessage());
        } finally {
            db.endTransaction();
        }

        this.onCreate(db);
    }


}

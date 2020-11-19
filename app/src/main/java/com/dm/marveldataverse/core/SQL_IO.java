package com.dm.marveldataverse.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dm.marveldataverse.model.User;

public class SQL_IO extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "Marvel_Dataverse";
    private static final int DB_VERSION = 1;

    public static final String TABLA_USUARIOS = "users";
    public static final String CAMPO_USUARIOS_USERNAME = "username";
    public static final String CAMPO_USUARIOS_PASSWD = "passwd";
    public static final String CAMPO_USUARIOS_EMAIL = "email";

    public SQL_IO(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }

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

    public void addUser(User user){
        final SQLiteDatabase DB = this.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_USERNAME, user.getUsername() );
        VALORES.put(CAMPO_USUARIOS_PASSWD, user.getPasswd());
        VALORES.put(CAMPO_USUARIOS_EMAIL, user.getEmail());

        try {
            Log.i(DB_NOMBRE, "insertando usuario: " + user.getUsername());
            DB.beginTransaction();

            DB.insert(
                    TABLA_USUARIOS,
                    null,
                    VALORES
            );

            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NOMBRE, error.getMessage());
        } finally {
            DB.endTransaction();
        }
    }

    public boolean isValidUser(User user){
        //TODO
        return false;
    }




}

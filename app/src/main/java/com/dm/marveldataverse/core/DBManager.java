package com.dm.marveldataverse.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dm.marveldataverse.model.User;

public class DBManager extends SQLiteOpenHelper {

    private static DBManager instancia;

    private static final String DB_NOMBRE = "Marvel_Dataverse";
    private static final int DB_VERSION = 1;

    public static final String TABLA_USUARIOS = "users";
    public static final String CAMPO_USUARIOS_USERNAME = "username";
    public static final String CAMPO_USUARIOS_PASSWD = "passwd";
    public static final String CAMPO_USUARIOS_EMAIL = "email";

    public static DBManager getManager(Context c) {
        if (instancia == null) {
            instancia = new DBManager(c.getApplicationContext());
        }
        return instancia;
    }

    private DBManager(Context context) {
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

    public void addUser(User user) {
        final SQLiteDatabase DB = instancia.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_USERNAME, user.getUsername());
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

    public boolean isValidUser(User user) {
        final SQLiteDatabase DB = instancia.getReadableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_USERNAME, user.getUsername());
        VALORES.put(CAMPO_USUARIOS_PASSWD, user.getPasswd());
        VALORES.put(CAMPO_USUARIOS_EMAIL, user.getEmail());
        boolean toret;
        try {
            Log.i(DB_NOMBRE, "validando usuario: " + user.getUsername());
            DB.beginTransaction();

            String[] args = new String[]{user.getUsername(), user.getPasswd()};
            try (Cursor cursor = DB.query(TABLA_USUARIOS, null, CAMPO_USUARIOS_USERNAME + "=? AND " + CAMPO_USUARIOS_PASSWD + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }
            DB.setTransactionSuccessful();
        } catch (SQLException error) {
            Log.e(DB_NOMBRE, error.getMessage());
            toret = false;
        } finally {
            DB.endTransaction();
        }
        return toret;
    }


}

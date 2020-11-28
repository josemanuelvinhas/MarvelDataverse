package com.dm.marveldataverse.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.dm.marveldataverse.core.DBManager.CAMPO_USUARIOS_EMAIL;
import static com.dm.marveldataverse.core.DBManager.CAMPO_USUARIOS_IS_ADMIN;
import static com.dm.marveldataverse.core.DBManager.CAMPO_USUARIOS_PASSWD;
import static com.dm.marveldataverse.core.DBManager.CAMPO_USUARIOS_USERNAME;
import static com.dm.marveldataverse.core.DBManager.TABLA_USUARIOS;

/**
 * Esta clase permite pasar datos de tipo User a la base de datos y viceversa
 */
public class UserMapper extends BaseMapper {

    /**
     * Método constructor que llama al constructor de la clase padre para obtener la instancia
     * de la conexión con la base de datos
     *
     * @param context
     */
    public UserMapper(Context context) {
        super(context);
    }

    /**
     * Este método permite añadir un usuario (User) a la base de datos.
     *
     * @param user El objeto User a mapear
     * @throws RuntimeException si se produce algun error en la BD
     */
    public void addUser(User user) {
        final SQLiteDatabase DB = instance.getWritableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_USERNAME, user.getUsername());
        VALORES.put(CAMPO_USUARIOS_PASSWD, user.getPasswd());
        VALORES.put(CAMPO_USUARIOS_EMAIL, user.getEmail());
        VALORES.put(CAMPO_USUARIOS_IS_ADMIN, user.isAdmin());

        try {
            Log.i("DB", "insertando usuario: " + user.getUsername());
            DB.beginTransaction();
            DB.insertOrThrow(
                    TABLA_USUARIOS,
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
     * Este método permite saber si un nombre de usuario ya existe.
     *
     * @param username El nombre de usuario a comprobar
     * @return true si existe el usuario y false si no
     * @throws RuntimeException si se produce algun error en la BD
     */
    public boolean isUsernameExist(String username) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{username};

        boolean toret;
        try {
            Log.i("DB", "buscando usuario: " + username);

            try (Cursor cursor = DB.query(TABLA_USUARIOS, null, CAMPO_USUARIOS_USERNAME + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }
        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }
        return toret;
    }

    /**
     * Este método permite saber si un email ya existe.
     *
     * @param email El email a comprobar
     * @return true si existe el email y false si no
     * @throws RuntimeException si se produce algun error en la BD
     */
    public boolean isEmailExist(String email) {
        final SQLiteDatabase DB = instance.getReadableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_EMAIL, email);
        String[] args = new String[]{email};

        boolean toret = true;
        try {
            Log.i("DB", "buscando email: " + email);

            try (Cursor cursor = DB.query(TABLA_USUARIOS, null, CAMPO_USUARIOS_EMAIL + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }

        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }

        return toret;
    }

    /**
     * Este método permite saber si el usuario y la contraseña coinciden con los que hay en la base
     * de datos.
     *
     * @param user El usuario (basta con que tenga username y passwd) a comprobar
     * @return true si el username y el passwd coinciden y false si no
     * @throws RuntimeException si se produce algun error en la BD
     */
    public boolean isValidUser(User user) {
        final SQLiteDatabase DB = instance.getReadableDatabase();
        final ContentValues VALORES = new ContentValues();

        VALORES.put(CAMPO_USUARIOS_USERNAME, user.getUsername());
        VALORES.put(CAMPO_USUARIOS_PASSWD, user.getPasswd());

        String[] args = new String[]{user.getUsername(), user.getPasswd()};


        boolean toret;
        try {
            Log.i("DB", "validando usuario: " + user.getUsername());

            try (Cursor cursor = DB.query(TABLA_USUARIOS, null, CAMPO_USUARIOS_USERNAME + "=? AND " + CAMPO_USUARIOS_PASSWD + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }

        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }

        return toret;
    }

    public boolean isAdminUser(String username) {
        final SQLiteDatabase DB = instance.getReadableDatabase();

        String[] args = new String[]{username, "1"};

        boolean toret;
        try {

            try (Cursor cursor = DB.query(TABLA_USUARIOS, null, CAMPO_USUARIOS_USERNAME + "=? AND " + CAMPO_USUARIOS_IS_ADMIN + "=?", args, null, null, null)) {
                toret = cursor.getCount() == 1;
            }

        } catch (SQLException error) {
            Log.e("DB", error.getMessage());
            throw new RuntimeException("Error en la BD");
        }

        return toret;
    }
}

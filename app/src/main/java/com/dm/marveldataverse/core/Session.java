package com.dm.marveldataverse.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.dm.marveldataverse.model.User;
import com.dm.marveldataverse.model.UserMapper;

/**
 * La clase Session representa una sesion de un usuario. Como a la aplicación solo puede acceder
 * un usuario a la vez se emplea el patrón Singleton para garantizar que solo exista una instancia (
 * una sola sesión) durante la ejecución de la aplicación
 */
public class Session {

    private static Session session;

    private boolean sessionActive;
    private SharedPreferences prefs;
    private User user;
    private UserMapper userMapper;


    /**
     * Este método permite acceder a la sesión. Si no existe, la crea.
     * @param context El contexto de la applicación
     * @return La instancia de la sesión.
     */
    public static Session getSession(Context context) {
        if (session == null) {
            session = new Session(context.getApplicationContext());
        }
        return session;
    }

    /**
     * Es el método constructor de la sesión. Obtiene una instancia de UserMapper para poder iniciar
     * una sesión, obtiene acceso a las preferencias para obtener una sesión guardada.
     *
     * @param context El contexto de la applicación
     */
    private Session(Context context) {
        this.userMapper = new UserMapper(context);
        this.prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        this.sessionActive = getSesion();
    }

    /**
     * Este método inicia la sesión con los datos almacenados en las preferencias.
     * @return true si los datos son correctos y false en otro cualquier caso
     */
    private boolean getSesion() {
        String username = prefs.getString("username", null);
        String passwd = prefs.getString("passwd", null);
        if (username != null && passwd != null) {
            user = new User(username, passwd);
            try {
                user.validateForLogin();
                if (userMapper.isValidUser(user)) {
                    return true;
                } else {
                    return false;
                }
            } catch (ValidationException ve) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Este método inicia la sesión con un User.
     * @param user Almacena los datos del usuario
     * @return true si los datos son correctos y false en cualquier otro caso
     */
    public boolean startSession(User user) {

        try {
            user.validateForLogin();
        } catch (ValidationException ve) {
            return false;
        }

        if (userMapper.isValidUser(user)) {
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString("username", user.getUsername());
            editor.putString("passwd", user.getPasswd());
            editor.commit();
            this.user = user;
            this.sessionActive = true;
            return true;
        } else {
            return false;
        }

    }

    /**
     * Este método cierra la sesión (la elimina también de las preferencias)
     */
    public void closeSession() {

        SharedPreferences.Editor editor = this.prefs.edit();
        editor.remove("username");
        editor.remove("passwd");
        editor.commit();

        this.sessionActive = false;
    }

    /**
     * Este método permite saber si la sesión está o no activa
     * @return true si está activa y false si no lo está
     */
    public boolean isSessionActive() {
        return sessionActive;
    }
}

package com.dm.marveldataverse.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.dm.marveldataverse.model.User;

public class Session {

    private static Session session;

    private DBManager sqlIo;
    SharedPreferences prefs;
    private boolean sessionActive;
    private User user;

    public static Session getSession(Context c) {
        if (session == null) {
            session = new Session(c.getApplicationContext());
        }
        return session;
    }

    private Session(Context context) {
        this.sqlIo = DBManager.getManager(context);
        this.prefs = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        this.sessionActive = obtenerSesion();
    }

    private boolean obtenerSesion() {
        String username = prefs.getString("username", null);
        String passwd = prefs.getString("passwd", null);
        if(username != null && passwd != null){
            user = new User(username, passwd);
            return true;
        }
        return false;
    }

    public boolean iniciarSesion(User user){

        if(sqlIo.isValidUser(user)){
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString("username", user.getUsername());
            editor.putString("passwd", user.getPasswd());
            editor.commit();
            this.user = user;
            this.sessionActive = true;
            return true;
        }else{
            return false;
        }
    }

    public void cerrarSesion(){
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.remove("username");
        editor.remove("passwd");
        editor.commit();

        this.sessionActive = false;
    }

    public boolean isSessionActive() {
        return sessionActive;
    }
}

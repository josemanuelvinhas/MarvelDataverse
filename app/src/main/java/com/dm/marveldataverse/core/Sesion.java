package com.dm.marveldataverse.core;

import android.content.SharedPreferences;

import com.dm.marveldataverse.model.User;

public class Sesion {

    private SQL_IO sqlIo;
    SharedPreferences prefs;
    private boolean sessionActive;
    private User user;

    public Sesion(SQL_IO sqlIo, SharedPreferences prefs) {
        this.sqlIo = sqlIo;
        this.prefs = prefs;
        this.sessionActive = obtenerSesion();
    }

    private boolean obtenerSesion() {
        String username = prefs.getString("username", null);
        String passwd = prefs.getString("passwd", null);
        if(username!= null && passwd != null){
            user = new User(username, passwd);
        }
        return true;
    }

    public boolean iniciarSesion(User user){

        if(sqlIo.isValidUser(user)){
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString("username", user.getUsername());
            editor.putString("passwd", user.getPasswd());
            editor.commit();
            this.user = user;
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

        sessionActive = false;
    }
}

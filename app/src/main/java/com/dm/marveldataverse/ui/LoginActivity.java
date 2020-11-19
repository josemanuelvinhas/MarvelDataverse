package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.DBManager;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Session session = Session.getSession(LoginActivity.this);
        if (session.isSessionActive()) {
            LoginActivity.this.finish();
        }

        Button BT_LOGIN = LoginActivity.this.findViewById(R.id.btnLogin);
        BT_LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.login();
            }
        });

    }

    private void login() {
        final EditText ED_USERNAME = LoginActivity.this.findViewById(R.id.edUsername);
        final EditText ED_PASSWD = LoginActivity.this.findViewById(R.id.edPasswd);

        User user = new User(ED_USERNAME.getText().toString(),
                ED_PASSWD.getText().toString());

        if (user.isValidForLogin()){
            DBManager dbManager = DBManager.getManager(LoginActivity.this);
            Session session = Session.getSession(LoginActivity.this);
            if (session.iniciarSesion(user)){
                //TODO internacionalizacion del toast
                Toast.makeText( LoginActivity.this, "Sesión iniciada", Toast.LENGTH_LONG ).show();
                LoginActivity.this.finish();
            }
            else{
                //TODO internacionalizacion del toast
                Toast.makeText( LoginActivity.this, "Credenciales no validas", Toast.LENGTH_LONG ).show();
            }
        }else{
            //TODO internacionalizacion del toast
            Toast.makeText( LoginActivity.this, "No se ha podido iniciar sesion", Toast.LENGTH_LONG ).show();
        }

    }
}
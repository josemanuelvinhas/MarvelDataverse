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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Session session = Session.getSession(RegisterActivity.this);
        if (session.isSessionActive()) {
            RegisterActivity.this.finish();
        }

        Button BT_REGISTER = RegisterActivity.this.findViewById(R.id.btnSingin);
        BT_REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.register();
            }
        });
    }

    private void register() {
        final EditText ED_USERNAME = RegisterActivity.this.findViewById(R.id.edUsername);
        final EditText ED_EMAIL = RegisterActivity.this.findViewById(R.id.edEmail);
        final EditText ED_PASSWD = RegisterActivity.this.findViewById(R.id.edPasswd);

        User user = new User(ED_USERNAME.getText().toString(),
                ED_PASSWD.getText().toString(),
                ED_EMAIL.getText().toString());

        if (user.isValidForRegister()){
            DBManager dbManager = DBManager.getManager(RegisterActivity.this);
            dbManager.addUser(user);
            //TODO mostrar errores personalizados e internacionalizacion del toast
            Toast.makeText( RegisterActivity.this, "Registro realizado correctamente", Toast.LENGTH_LONG ).show();
            RegisterActivity.this.finish();
        }else{
            //TODO internacionalizacion del toast
            Toast.makeText( RegisterActivity.this, "No se ha podido realizar el registro", Toast.LENGTH_LONG ).show();
        }
    }
}
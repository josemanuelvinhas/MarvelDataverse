package com.dm.marveldataverse.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.User;

public class LoginActivity extends AppCompatActivity {

    private Session session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Personalizar ActionBar
        final ActionBar ACTION_BAR = this.getSupportActionBar();
        ACTION_BAR.setTitle(R.string.login);

        //Inicialización de atributos
        LoginActivity.this.session = Session.getSession(LoginActivity.this);
        LoginActivity.this.user = new User();

        //Inicialización de eventos
        final Button BT_LOGIN = LoginActivity.this.findViewById(R.id.btnLogin);
        BT_LOGIN.setOnClickListener(v -> LoginActivity.this.login());

        final EditText ED_USERNAME = LoginActivity.this.findViewById(R.id.edUsername);
        ED_USERNAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                User.validateUsername(ED_USERNAME.getText().toString());
            } catch (ValidationException e) {
                ED_USERNAME.setError(getResources().getString(e.getError()));
            }
        });

        final EditText ED_PASSWD = LoginActivity.this.findViewById(R.id.edPasswd);
        ED_PASSWD.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                User.validatePasswd(ED_PASSWD.getText().toString());
            } catch (ValidationException e) {
                ED_PASSWD.setError(getResources().getString(e.getError()));
            }
        });

        //Control de sesión
        if (LoginActivity.this.session.isSessionActive()) {
            LoginActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (LoginActivity.this.session.isSessionActive()) {
            LoginActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_go_back, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean toret;

        switch (item.getItemId()) {
            case R.id.itGoBack:
                LoginActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }

        return toret;
    }

    private void login() {
        final EditText ED_USERNAME = LoginActivity.this.findViewById(R.id.edUsername);
        final EditText ED_PASSWD = LoginActivity.this.findViewById(R.id.edPasswd);

        LoginActivity.this.user.setUsername(ED_USERNAME.getText().toString());
        LoginActivity.this.user.setPasswd(ED_PASSWD.getText().toString());


        try{
            LoginActivity.this.user.validateForLogin();
            try {
                if (LoginActivity.this.session.startSession(LoginActivity.this.user)) {
                    Toast.makeText(LoginActivity.this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                }
            }catch (RuntimeException e){
                Toast.makeText(LoginActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
            }

        }catch (ValidationException ve){
            Toast.makeText( LoginActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT ).show();
        }

    }

}
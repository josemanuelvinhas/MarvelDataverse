package com.dm.marveldataverse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.core.ValidationException;
import com.dm.marveldataverse.model.User;
import com.dm.marveldataverse.model.UserMapper;

public class RegisterActivity extends AppCompatActivity {

    private Session session;
    private UserMapper userMapper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicialización de variables
        RegisterActivity.this.session = Session.getSession(RegisterActivity.this);
        RegisterActivity.this.userMapper = new UserMapper(RegisterActivity.this);
        RegisterActivity.this.user = new User();

        //Inicialización de eventos
        Button BT_REGISTER = RegisterActivity.this.findViewById(R.id.btnSingin);
        BT_REGISTER.setOnClickListener(v -> RegisterActivity.this.register());

        final EditText ED_USERNAME = RegisterActivity.this.findViewById(R.id.edUsername);
        ED_USERNAME.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                User.validateUsername(ED_USERNAME.getText().toString());
                try {
                    if (RegisterActivity.this.userMapper.isUsernameExist(ED_USERNAME.getText().toString())) {
                        ED_USERNAME.setError(getResources().getString(R.string.username_exists));
                    }
                } catch (RuntimeException ex) {
                    Log.e("BD", "Error BD, no se puede saber si existe");
                }

            } catch (ValidationException e) {
                ED_USERNAME.setError(getResources().getString(e.getError()));
            }
        });

        final EditText ED_PASSWD = RegisterActivity.this.findViewById(R.id.edPasswd);
        ED_PASSWD.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                User.validatePasswd(ED_PASSWD.getText().toString());
            } catch (ValidationException e) {
                ED_PASSWD.setError(getResources().getString(e.getError()));
            }
        });

        final EditText ED_EMAIL = RegisterActivity.this.findViewById(R.id.edEmail);
        ED_EMAIL.setOnFocusChangeListener((v, hasFocus) -> {
            try {
                User.validateEmail(ED_EMAIL.getText().toString());
                try {
                    if (RegisterActivity.this.userMapper.isEmailExist(ED_EMAIL.getText().toString())) {
                        ED_EMAIL.setError(getResources().getString(R.string.email_exists));
                    }
                } catch (RuntimeException ex) {
                    Log.e("BD", "Error BD, no se puede saber si existe");
                }
            } catch (ValidationException e) {
                ED_EMAIL.setError(getResources().getString(e.getError()));
            }
        });

        //Salir si existe una sesión
        if (RegisterActivity.this.session.isSessionActive()) {
            RegisterActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Salir si existe una sesión
        if (RegisterActivity.this.session.isSessionActive()) {
            RegisterActivity.this.finish();
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
                RegisterActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }

        return toret;
    }

    private void register() {
        final EditText ED_USERNAME = RegisterActivity.this.findViewById(R.id.edUsername);
        final EditText ED_EMAIL = RegisterActivity.this.findViewById(R.id.edEmail);
        final EditText ED_PASSWD = RegisterActivity.this.findViewById(R.id.edPasswd);
        final CheckBox CB_IS_ADMIN = findViewById(R.id.cbIsAdmin);

        RegisterActivity.this.user.setUsername(ED_USERNAME.getText().toString());
        RegisterActivity.this.user.setEmail(ED_EMAIL.getText().toString());
        RegisterActivity.this.user.setPasswd(ED_PASSWD.getText().toString());
        RegisterActivity.this.user.setAdmin(CB_IS_ADMIN.isChecked());

        try {
            RegisterActivity.this.user.validateForRegister();
            try {
                boolean isUsernameExist = RegisterActivity.this.userMapper.isUsernameExist(RegisterActivity.this.user.getUsername());
                boolean isEmailExist = RegisterActivity.this.userMapper.isEmailExist(RegisterActivity.this.user.getEmail());

                if (isUsernameExist || isEmailExist) {
                    if (isUsernameExist) {
                        Toast.makeText(RegisterActivity.this, R.string.username_exists, Toast.LENGTH_SHORT).show();
                    }
                    if (isEmailExist) {
                        Toast.makeText(RegisterActivity.this, R.string.email_exists, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    RegisterActivity.this.userMapper.addUser(RegisterActivity.this.user);
                    Toast.makeText(RegisterActivity.this, R.string.singin_successful, Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                }

            } catch (RuntimeException e) {
                Toast.makeText(RegisterActivity.this, R.string.bd_error, Toast.LENGTH_SHORT).show();
            }

        } catch (ValidationException e) {
            Toast.makeText(RegisterActivity.this, R.string.invalid_fields, Toast.LENGTH_SHORT).show();
        }

    }
}
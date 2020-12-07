package com.dm.marveldataverse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.ui.admin.AdminActivity;
import com.dm.marveldataverse.ui.user.CoreUserActivity;

public class MainActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialización de atributos
        MainActivity.this.session = Session.getSession(MainActivity.this);

        //Inicialización de eventos
        final Button BT_LOGIN = MainActivity.this.findViewById(R.id.btnLogin);
        BT_LOGIN.setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        final Button BT_REGISTER = MainActivity.this.findViewById(R.id.btnSingin);
        BT_REGISTER.setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        //Control de sesión
        if (MainActivity.this.session.isSessionActive()) {
            if (MainActivity.this.session.isAdmin()){
                MainActivity.this.startAdminActivity();
            }else{
                MainActivity.this.startUserActivity();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.this.session.isSessionActive()) {
            if (MainActivity.this.session.isAdmin()){
                MainActivity.this.startAdminActivity();
            }else{
                MainActivity.this.startUserActivity();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean toret;

        switch (item.getItemId()) {
            case R.id.itAcercaDe:
                MainActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                MainActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }

        return toret;
    }

    private void startAdminActivity() {
        MainActivity.this.startActivity(new Intent(MainActivity.this, AdminActivity.class));
        MainActivity.this.finish();
        Log.i("CREAR ACTIVIDAD", " ACTIVIDAD DE ADMIN CREADA");
    }

    private void startUserActivity() {
        MainActivity.this.startActivity(new Intent(MainActivity.this, CoreUserActivity.class));
        MainActivity.this.finish();
        Log.i("CREAR ACTIVIDAD", " ACTIVIDAD DE USUARIO CREADA");
    }

    private void startAboutActivity() {
        MainActivity.this.startActivity(new Intent(this, AboutActivity.class));
    }
}
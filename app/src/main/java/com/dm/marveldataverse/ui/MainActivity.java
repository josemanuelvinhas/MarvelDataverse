package com.dm.marveldataverse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;

public class MainActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.session = Session.getSession(MainActivity.this);

        final Button BT_LOGIN = MainActivity.this.findViewById(R.id.btnLogin);
        BT_LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        final Button BT_REGISTER = MainActivity.this.findViewById(R.id.btnSingin);
        BT_REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        this.startCoreActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        this.startCoreActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        boolean toret = false;

        switch (item.getItemId()) {
            case R.id.lbAcercaDe:
                toret = true;
                acercaDe();
                break;
            case R.id.lbSalir:
                toret = true;
                System.exit(0);
                break;
            default:
                Toast.makeText(this,
                        "ERROR producido en el menu principal",
                        Toast.LENGTH_LONG);
        }

        return toret;
    }

    private void startCoreActivity(){
        if (this.session.isSessionActive()){
            this.startActivity(new Intent(MainActivity.this, CoreActivity.class));
        }
    }

    private void acercaDe() {
        this.startActivity(new Intent(this, AboutActivity.class));
    }
}
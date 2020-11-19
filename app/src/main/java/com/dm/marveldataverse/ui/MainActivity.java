package com.dm.marveldataverse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void acercaDe() {
        this.startActivity(new Intent(this, AboutActivity.class));
    }
}
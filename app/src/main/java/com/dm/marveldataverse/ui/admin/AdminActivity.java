package com.dm.marveldataverse.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.ui.AboutActivity;
import com.dm.marveldataverse.ui.MainActivity;
import com.dm.marveldataverse.ui.user.UserActivity;

public class AdminActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AdminActivity.this.session = Session.getSession(AdminActivity.this);

        final Button BT_ADMIN = AdminActivity.this.findViewById(R.id.btnAdmin);
        BT_ADMIN.setOnClickListener(v -> AdminActivity.this.startCoreAdminActivity());

        final Button BT_USER = AdminActivity.this.findViewById(R.id.btnUser);
        BT_USER.setOnClickListener(v -> AdminActivity.this.startUserActivity());

        //Salir si no existe una sesión
        if (!AdminActivity.this.session.isSessionActive() || !AdminActivity.this.session.isAdmin()) {
            AdminActivity.this.finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!AdminActivity.this.session.isSessionActive() || !AdminActivity.this.session.isAdmin()) {
            AdminActivity.this.startMainActivity();
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        AdminActivity.this.getMenuInflater().inflate(R.menu.menu_core, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                AdminActivity.this.session.closeSession();
                AdminActivity.this.startMainActivity();
                AdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                AdminActivity.this.startAboutActivity();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startMainActivity() {
        AdminActivity.this.startActivity(new Intent(AdminActivity.this, MainActivity.class));
    }

    private void startAboutActivity() {
        AdminActivity.this.startActivity(new Intent(AdminActivity.this, AboutActivity.class));
    }

    private void startCoreAdminActivity() {
        AdminActivity.this.startActivity(new Intent(AdminActivity.this, CoreAdminActivity.class));
    }

    private void startUserActivity() {
        AdminActivity.this.startActivity(new Intent(AdminActivity.this, UserActivity.class));
    }
}
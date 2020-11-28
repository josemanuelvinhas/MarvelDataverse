package com.dm.marveldataverse.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.Session;
import com.dm.marveldataverse.ui.AboutActivity;
import com.dm.marveldataverse.ui.MainActivity;

public class CoreAdminActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_admin);

        CoreAdminActivity.this.session = Session.getSession(CoreAdminActivity.this);

        final Button BT_CHARACTERS_MANAGEMENT = CoreAdminActivity.this.findViewById(R.id.btnCharactersManagement);
        BT_CHARACTERS_MANAGEMENT.setOnClickListener(v -> CoreAdminActivity.this.startCharactersActivity());

        final Button BT_COMICS_MANAGEMENT = CoreAdminActivity.this.findViewById(R.id.btnComicsManagement);
        BT_COMICS_MANAGEMENT.setOnClickListener(v -> Toast.makeText( CoreAdminActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        final Button BT_SERIES_MANAGEMENT = CoreAdminActivity.this.findViewById(R.id.btnSeriesManagement);
        BT_SERIES_MANAGEMENT.setOnClickListener(v -> Toast.makeText( CoreAdminActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        if (!CoreAdminActivity.this.session.isSessionActive() || !CoreAdminActivity.this.session.isAdmin()) {
            CoreAdminActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CoreAdminActivity.this.session.isSessionActive() || !CoreAdminActivity.this.session.isAdmin()) {
            CoreAdminActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        CoreAdminActivity.this.getMenuInflater().inflate(R.menu.menu_core, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                CoreAdminActivity.this.session.closeSession();
                CoreAdminActivity.this.startMainActivity();
                CoreAdminActivity.this.finish();
                toret = true;
                break;
            case R.id.itAcercaDe:
                CoreAdminActivity.this.startAboutActivity();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void startAboutActivity() {
        CoreAdminActivity.this.startActivity(new Intent(CoreAdminActivity.this, AboutActivity.class));
    }

    private void startCharactersActivity() {
        CoreAdminActivity.this.startActivity(new Intent(CoreAdminActivity.this, CharactersAdminActivity.class));
    }

    private void startMainActivity() {
        CoreAdminActivity.this.startActivity(new Intent(CoreAdminActivity.this, MainActivity.class));
    }


}
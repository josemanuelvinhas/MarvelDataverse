package com.dm.marveldataverse.ui.user;

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
import com.dm.marveldataverse.ui.LoginActivity;
import com.dm.marveldataverse.ui.MainActivity;
import com.dm.marveldataverse.ui.admin.AdminActivity;
import com.dm.marveldataverse.ui.admin.CharactersAdminActivity;
import com.dm.marveldataverse.ui.admin.CoreAdminActivity;

public class UserActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        UserActivity.this.session = Session.getSession(UserActivity.this);

        final Button BT_CHARACTERS = UserActivity.this.findViewById(R.id.btnCharacters);
        BT_CHARACTERS.setOnClickListener(v -> UserActivity.this.startCharactersUserActivity());

        final Button BT_COMICS = UserActivity.this.findViewById(R.id.btnComics);
        BT_COMICS.setOnClickListener(v -> Toast.makeText( UserActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        final Button BT_SERIES = UserActivity.this.findViewById(R.id.btnSeries);
        BT_SERIES.setOnClickListener(v -> Toast.makeText( UserActivity.this, R.string.soon, Toast.LENGTH_SHORT ).show());

        if (!UserActivity.this.session.isSessionActive()) {
            if (!UserActivity.this.session.isAdmin()) {
                UserActivity.this.startMainActivity();
            }
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserActivity.this.session.isSessionActive()) {
            if (!UserActivity.this.session.isAdmin()) {
                UserActivity.this.startMainActivity();
            }
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        UserActivity.this.getMenuInflater().inflate(R.menu.menu_characters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        boolean toret;

        switch (menuItem.getItemId()) {
            case R.id.itLogout:
                UserActivity.this.logOut();
                toret = true;
                break;
            case R.id.itAcercaDe:
                UserActivity.this.startAboutActivity();
                toret = true;
                break;
            case R.id.itGoBack:
                UserActivity.this.finish();
                toret = true;
                break;
            default:
                toret = false;
        }
        return toret;
    }

    private void logOut(){
        UserActivity.this.session.closeSession();
        if (!UserActivity.this.session.isAdmin()) {
            UserActivity.this.startMainActivity();
        }
        UserActivity.this.finish();
    }

    private void startAboutActivity() {
        UserActivity.this.startActivity(new Intent(this, AboutActivity.class));
    }

    private void startMainActivity() {
        UserActivity.this.startActivity(new Intent(UserActivity.this, MainActivity.class));
    }

    private void startCharactersUserActivity() {
        UserActivity.this.startActivity(new Intent(UserActivity.this, CharactersUserActivity.class));
    }

}
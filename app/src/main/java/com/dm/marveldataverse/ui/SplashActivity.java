package com.dm.marveldataverse.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.marveldataverse.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Animation ANIMACION_NOMBRE = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        final Animation ANIMACION_LOGO = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        final ImageView TV_LOGO = this.findViewById(R.id.ivLogoSplash);
        final TextView TV_NOMBRE = this.findViewById(R.id.tvNameApp);
        final TextView TV_WELCOME = this.findViewById(R.id.tvWelcome);

        TV_LOGO.setAnimation(ANIMACION_LOGO);
        TV_NOMBRE.setAnimation(ANIMACION_NOMBRE);
        TV_WELCOME.setAnimation(ANIMACION_NOMBRE);

        new Handler().postDelayed(() -> SplashActivity.this.startMainActivity(), 2000);


    }

    private void startMainActivity() {
        SplashActivity.this.startActivity(new Intent(this, MainActivity.class));
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
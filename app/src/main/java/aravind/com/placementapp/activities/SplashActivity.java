package aravind.com.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import aravind.com.placementapp.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView img = findViewById(R.id.indiralogo);
        TextView text = findViewById(R.id.welcome);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_slide_down_animation);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_slide_up_animation);
        img.startAnimation(anim1);
        text.startAnimation(anim2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }, 2000);

    }
}

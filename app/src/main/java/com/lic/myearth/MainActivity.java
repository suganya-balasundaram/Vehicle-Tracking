package com.lic.myearth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView logo;

    Animation aaa;

    boolean double_tap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeAdmin.class));
            return;
        }


        logo = findViewById(R.id.img_logo);

        aaa = AnimationUtils.loadAnimation(this,R.anim.blink);
        logo.setAnimation(aaa);

        new Thread()
        {
            public void run()
            {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //Intent used for jump from one page to another page
                    //Intent1
                    startActivity(new Intent(MainActivity.this,LoginPage.class));
                    finish();
//
//                    //Intent 2
//                    Intent intent = new Intent(MainActivity.this,LoginPage.class);
//                    startActivity(intent);
//                    finish();
                }
            }

        }.start();

    }
}
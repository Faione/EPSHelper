package com.example.faione.epshelper;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SupportActivity extends AppCompatActivity {
    private MediaPlayer mp=new MediaPlayer();
    private ImageButton alwayswithme;
    private TextView dg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        alwayswithme = findViewById(R.id.support_imgbtn) ;
        mp =MediaPlayer.create(this,R.raw.music);
        dg = findViewById(R.id.designer);
        dg.setVisibility(View.INVISIBLE);

        alwayswithme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                dg.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }
    }
//    作者：pythontojava
//    来源：CSDN
//    原文：https://blog.csdn.net/pythontojava/article/details/48058087
//    版权声明：本文为博主原创文章，转载请附上博文链接！


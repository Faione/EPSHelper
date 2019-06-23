package com.example.faione.epshelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class showbodyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbody);
        String name = getIntent().getStringExtra("showname");
        String date = getIntent().getStringExtra("showdate");
        String body = getIntent().getStringExtra("showbody");
        ((TextView) findViewById(R.id.showbodyttitle)).setText(name);
        ((TextView) findViewById(R.id.showbodydate)).setText(date);
        ((TextView) findViewById(R.id.showbodytv)).setText(body);
    }
}

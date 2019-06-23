package com.example.faione.epshelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity  extends AppCompatActivity {
    private Fragment mFragment[];
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction =
                    fragmentManager.beginTransaction().hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.show(mFragment[0]).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction.show(mFragment[1]).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentTransaction.show(mFragment[2]).commit();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFragment = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragment[0] = fragmentManager.findFragmentById(R.id.fragment_main);
        mFragment[1] = fragmentManager.findFragmentById(R.id.fragment_func);
        mFragment[2] = fragmentManager.findFragmentById(R.id.fragment_setting);
        fragmentTransaction =
                fragmentManager.beginTransaction().hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]);
        fragmentTransaction.show(mFragment[0]).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.epsmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_reset){
            reSetEpslistActivity();
        }else if(item.getItemId()==R.id.response_list){
            Intent Response = new Intent(this,SupportActivity.class);
            startActivityForResult(Response,7);
        }
        return super.onOptionsItemSelected(item);
    }
    private void reSetEpslistActivity() {
        Intent Reset = new Intent(this,EpslistActivity.class);
        String resetData= "请在完成后重新选择您还未领取的快递";
        Reset.putExtra("resetDate","");
        Reset.putExtra("resetData",resetData);
        startActivityForResult(Reset,1);
    }

}

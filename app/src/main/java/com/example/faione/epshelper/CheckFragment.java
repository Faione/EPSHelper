package com.example.faione.epshelper;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment  {
    public static final int  REQ_CODE_CONTACT = 1;
    ImageButton button;
    TextView check_tx;
    public CheckFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_check , container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.slidinig_tabs);
        FragmentManager man = getActivity().getSupportFragmentManager();
        MyPageAdapter pageAdapter = new MyPageAdapter(man);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }

    @Override
    public void onResume() {
        button.setImageResource(R.drawable.ic_fresh);
        check_tx.setText("开始同步");
        super.onResume();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (ImageButton) getActivity().findViewById(R.id.btn_check);
        check_tx = (TextView) getActivity().findViewById(R.id.check_tx);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setImageResource(R.drawable.ic_fresh_onclick);
                check_tx.setText("正在同步");
                checkSMSPermission();
                }
        });
    }

    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //未获取到读取短信权限

            //向系统申请权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_SMS}, REQ_CODE_CONTACT);
        } else {
            Intent list = new Intent(getActivity(),EpslistActivity.class);
            startActivity(list);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断用户是否，同意 获取短信授权
        if (requestCode == REQ_CODE_CONTACT && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //获取到读取短信权限
            Intent list = new Intent(getActivity(),EpslistActivity.class);
            startActivity(list);
        } else {
            Toast.makeText(getActivity(), "未获取到短信权限", Toast.LENGTH_SHORT).show();
        }
    }


}
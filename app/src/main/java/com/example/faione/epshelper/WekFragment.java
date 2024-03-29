package com.example.faione.epshelper;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WekFragment extends Fragment implements AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener {
    private ListView weklistView;
    private TextView wektx;
    private MylistViewAdapter mylistViewAdapter;
    private List<Map<String, Object>> weklist;
    private String wekbody;
    private int CheckId;

    public WekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View wekview = inflater.inflate(R.layout.fragment_wek, container, false);
        weklistView = (ListView) wekview.findViewById(R.id.checkweklistview);
        wektx =(TextView)wekview.findViewById(R.id.wektxview) ;
        weklist = getData();
        mylistViewAdapter = new MylistViewAdapter(getActivity(), weklist);
        weklistView.setAdapter(mylistViewAdapter);
        weklistView.setOnItemClickListener(this);
        weklistView.setOnItemLongClickListener(this);
        return wekview;
    }

    //用户从其他页面返回时，刷新页面
    public void onResume() {
        super.onResume();
        if(mylistViewAdapter != null){
            weklist=getData();
            mylistViewAdapter = new MylistViewAdapter(getActivity(), weklist);
            weklistView.setAdapter(mylistViewAdapter);
            Log.i("WEK","WekOnResume");
        }
    }

    //博客园，昵称：安卓笔记侠，指出在ViewPage中使用setUserVisibleHint可实现Fragment懒加载，而不用考虑其生命周期，实现ViewPage中的页面刷新
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mylistViewAdapter != null) {
            //统计代码 或者 fragment显示操作
            weklist=getData();
            mylistViewAdapter = new MylistViewAdapter(getActivity(), weklist);
            weklistView.setAdapter(mylistViewAdapter);
            Log.i("YD","YDOnResume");
        } else {

        }
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        EpsManager manager = new EpsManager(getActivity());
        for (EpsItem item : manager.listAll()) {
            if(item.getExpressSituation().equals("否")){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("epsid", item.getId());
            map.put("epsdate", item.getExpressDate());
            map.put("epsname", item.getExpressName());
            map.put("epsnum", item.getExpressNum());
            map.put("epsbody", item.getExpressBody());
            map.put("epssituation", item.getExpressSituation());
            list.add(map);
            wektx.setVisibility(View.GONE);
            }
        }
        if(list.size() ==0){
            wektx.setVisibility(View.VISIBLE);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) weklistView.getItemAtPosition(position);
        wekbody = (String) map.get("epsbody");
        String wekepsname = (String) map.get("epsname");
        String wekepsdate = (String) map.get("epsdate");

        Intent showwekbody = new Intent(getActivity(),showbodyActivity.class);
        showwekbody.putExtra("showname",wekepsname);
        showwekbody.putExtra("showbody",wekbody);
        showwekbody.putExtra("showdate",wekepsdate);
        startActivity(showwekbody);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //获得数据库中该组数据的id
        Map<String, Object> map =new HashMap<String, Object>();
        map = (HashMap<String, Object>)weklistView.getItemAtPosition(position);

            CheckId =(int) map.get("epsid");
            final ContentValues contentValues = new ContentValues();
            contentValues.put("EPSSITUATION","已收");

            builder.setTitle("提示").setMessage("请确认已经收到快递！").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //短信虽然从listView中删除，但实际上只是改变了Situation值，确认了快递的验收状况
                    weklist.remove(position);
                    if(weklist.size()==0){
                        wektx.setVisibility(View.VISIBLE);
                    }
                    EpsManager manager = new EpsManager(getActivity());
                    manager.updateEps(contentValues,String.valueOf(CheckId));
                    mylistViewAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton("否",null);
            builder.create().show();
        return true;
    }
}

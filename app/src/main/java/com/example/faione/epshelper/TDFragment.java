package com.example.faione.epshelper;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TDFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private ListView tdlistView;
    private TextView tdtx;
    private MylistViewAdapter mylistViewAdapter;
    private List<Map<String, Object>> tdlist;
    private int CheckId;
    private String TdStr ="";
    private String tdbody = "";

    public TDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View tdview = inflater.inflate(R.layout.fragment_td , container, false);
        tdtx =(TextView)tdview.findViewById(R.id.tdtxview) ;
        tdlistView = (ListView)tdview.findViewById(R.id.checktdlistview);
        tdlist=getData();
        mylistViewAdapter = new MylistViewAdapter(getActivity(), tdlist);
        tdlistView.setAdapter(mylistViewAdapter);
        tdlistView.setOnItemClickListener(this);
        tdlistView.setOnItemLongClickListener(this);
        TdStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        return tdview;
    }

    @Override
    //使得返回时页面刷新
    public void onResume() {
        super.onResume();
        if(mylistViewAdapter != null){
            tdlist=getData();
            mylistViewAdapter = new MylistViewAdapter(getActivity(), tdlist);
            tdlistView.setAdapter(mylistViewAdapter);
        }
    }


    //博客园，昵称：安卓笔记侠，指出在ViewPage中使用setUserVisibleHint可实现Fragment懒加载，而不用考虑其生命周期，实现ViewPage中的页面刷新
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mylistViewAdapter != null) {
            //统计代码 或者 fragment显示操作
            tdlist=getData();
            mylistViewAdapter = new MylistViewAdapter(getActivity(), tdlist);
            tdlistView.setAdapter(mylistViewAdapter);
        } else {
        }
    }


    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        EpsManager manager = new EpsManager(getActivity());
        for(EpsItem item : manager.listAll() ){
            if (item.getExpressDate().equals(TdStr) && item.getExpressSituation().equals("否")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("epsid", item.getId());
                map.put("epsdate", item.getExpressDate());
                map.put("epsname", item.getExpressName());
                map.put("epsnum", item.getExpressNum());
                map.put("epsbody", item.getExpressBody());
                map.put("epssituation", item.getExpressSituation());
                list.add(map);
                tdtx.setVisibility(View.GONE);
            }
        }
        if(list.size() ==0){
            tdtx.setVisibility(View.VISIBLE);
        }
        return list;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) tdlistView.getItemAtPosition(position);
        tdbody = (String) map.get("epsbody");
        String tdepsname = (String) map.get("epsname");
        String tdepsdate = (String) map.get("epsdate");

        Intent showtdbody = new Intent(getActivity(),showbodyActivity.class);
        showtdbody.putExtra("showname",tdepsname);
        showtdbody.putExtra("showbody",tdbody);
        showtdbody.putExtra("showdate",tdepsdate);
        startActivity(showtdbody);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //获得数据库中该组数据的id
        Map<String, Object> map =new HashMap<String, Object>();
        map = (HashMap<String, Object>)tdlistView.getItemAtPosition(position);
            CheckId = (int) map.get("epsid");
            final   ContentValues contentValues = new ContentValues();
            contentValues.put("EPSSITUATION","已收");

            builder.setTitle("提示").setMessage("请确认已经收到快递！").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //短信虽然从listView中删除，但实际上只是改变了Situation值，确认了快递的验收状况
                    tdlist.remove(position);
                    if(tdlist.size()==0){
                        tdtx.setVisibility(View.VISIBLE);
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

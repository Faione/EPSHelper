package com.example.faione.epshelper;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class EpslistActivity extends ListActivity implements  Runnable, AdapterView.OnItemClickListener {
    private  String data[]= {"请在同步完成后，点击添加您还未收取的快递"};
    String TAG = "EpslistActivity";
    String[] epName = new String[]{"圆通","中通","顺丰"};
    private String lastDateStr ="";
    private int firstUsing = 0;
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fs = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
    Handler handler;
    private SimpleAdapter adapterX;
    List<HashMap<String,String>> listEA;
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        lastDateStr = intent.getStringExtra("resetDate");
        String rdata = intent.getStringExtra("resetData");

        if(rdata != null){
            data[0] = rdata;
        }else{
            //获取上次点击时间
            SharedPreferences sp = getSharedPreferences("Eps",Context.MODE_PRIVATE);
            lastDateStr = sp.getString(DATE_SP_KEY,"");
        }

        //加载时的提示
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);

        Thread t = new Thread(this);
        t.start();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==7){
                    listEA =(List<HashMap<String,String>>) msg.obj;
                    adapterX = new SimpleAdapter(EpslistActivity.this,listEA,
                            R.layout.getlistview,
                            new String[]{"ItemId","ItemDate","ItemName"},
                            new int[]{R.id.listid,R.id.listdate,R.id.listname}
                    );
                    setListAdapter(adapterX);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("run","EpslistAcitivity结束！ firstusing"+firstUsing);
        if(firstUsing == 1) {
            EpsManager manager = new EpsManager(this);
            for (EpsItem item : manager.listAll()) {
                if (item.getExpressSituation().equals("是")) {
                    manager.deleteEps(String.valueOf(String.valueOf(item.getId())));
                }
            }
        }
    }

    @Override
    public void run() {
        String weekdate;
        Date wk =null;
        Date lastDate =null;
        Date epd =null;
        Calendar calendar = Calendar.getInstance();
        Date totay = new Date();
        List<HashMap<String,String>> mylist = new ArrayList<HashMap<String,String>>();
        if(lastDateStr == null || lastDateStr.length()==0 ) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //计算一星期前的日期
        calendar.setTime(totay);
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        weekdate = fs.format(calendar.getTime());

        //获取短信中的快递讯息
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "body", "date", "type"}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                String body ;
                String date ;
                List<EpsItem> epsList = new ArrayList<EpsItem>();

                while (cursor.moveToNext()) {

                    body = cursor.getString(2);
                    date = cursor.getString(3);
                    try {
                        epd = fs.parse(fs.format(Long.parseLong(date)));
                        wk = fs.parse(weekdate);
                        if(lastDateStr != null && lastDateStr.length()>0 ){
                           lastDate = fs.parse(lastDateStr);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//因快递有时效限制，因而只加入最近一星期的短信，同时，读取上次操作时间判断是否是首次操作，以精确地添加最新的短信。
                    if (epd.getTime()>= wk.getTime()){
                        if(lastDate==null) {
                            firstUsing = 1;
                            addEplist(epsList, date, body);
                        }else if(epd.getTime()> lastDate.getTime()){

                                addEplist(epsList, date, body);
                        }
                }
                }
                EpsManager manager = new EpsManager(this);
               if(firstUsing == 1 ){
                   manager.deleteAll();
               }
                manager.addAll(epsList);
            }
        //记录更新日期
        //更新记录日期
        String lastDateStr = fs.format(totay);

        SharedPreferences sp = getSharedPreferences("Eps",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DATE_SP_KEY,lastDateStr);
        editor.commit();
        Log.i("run","更新日期结束: "+ lastDateStr);

        //返回数据
        EpsManager manager = new EpsManager(this);
        for(EpsItem item : manager.listAll() ){
                if (item.getExpressSituation().equals("是") ) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemId", String.valueOf(item.getId()));
                    map.put("ItemDate", item.getExpressDate());
                    map.put("ItemName", item.getExpressName());
                    mylist.add(map);
                }
            }
            if(mylist.size()==0){
               HashMap<String, String> map = new HashMap<String, String>();
               map.put("ItemDate", "没有最新信息");
               mylist.add(map);
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = mylist;
        handler.sendMessage(msg);
    }

    //对短信进行重要信息提取,并添加至Eplist中
    public void addEplist(List<EpsItem> epsList,String date,String body){
        String cutBody="";
        if (body.contains(epName[0])) {
            cutBody = body.substring(body.indexOf("码") + 1, body.indexOf("码") + 8);
            epsList.add(new EpsItem("是", f.format(Long.parseLong(date)), epName[0],cutBody, body));
        }else if(body.contains(epName[1])){
            cutBody = body.substring(body.indexOf("凭") + 2, body.indexOf("凭") + 10);
            epsList.add(new EpsItem("是", f.format(Long.parseLong(date)), epName[1],cutBody, body));
        }else if(body.contains(epName[2])){
            cutBody = body.substring(body.indexOf("号") + 1, body.indexOf("号") + 5);
            epsList.add(new EpsItem("是", f.format(Long.parseLong(date)), epName[2],cutBody, body));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        HashMap<String, String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        if (map.get("ItemId") != null && map.get("ItemId").length()>0) {
            final String delid = map.get("ItemId");
            final EpsManager manager = new EpsManager(this);
            final ContentValues contentValues = new ContentValues();
            contentValues.put("EPSSITUATION","否");

            listEA.remove(position);
            adapterX.notifyDataSetChanged();
            manager.updateEps(contentValues,String.valueOf(delid));
            if(listEA.size()==0){
                HashMap<String, String> map2 = new HashMap<String, String>();
                map.put("ItemId", "");
                map.put("ItemDate", "没有最新信息");
                map.put("ItemName", "");
                listEA.add(map);
                adapterX.notifyDataSetChanged();
            }
        }else{
            builder.setTitle("提示").setMessage("您可以重设以重新选择");
            builder.create().show();
        }
    }
}

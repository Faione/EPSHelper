package com.example.faione.epshelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EpsManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public EpsManager(Context context){
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(EpsItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("epssituation",item.getExpressSituation());
        values.put("epsdate",item.getExpressDate());
        values.put("epsname",item.getExpressName());
        values.put("epsnum",item.getExpressNum());
        values.put("epsbody",item.getExpressBody());
        db.insert(TBNAME,null,values);
        db.close();
    }

    public List<EpsItem>listAll(){
        List<EpsItem> epsList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,null,null,null,null,null);
        if (cursor!=null){
            epsList = new ArrayList<EpsItem>();
            while(cursor.moveToNext()){
                EpsItem item = new EpsItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setExpressSituation(cursor.getString(cursor.getColumnIndex("EPSSITUATION")));
                item.setExpressDate(cursor.getString(cursor.getColumnIndex("EPSDATE")));
                item.setExpressName(cursor.getString(cursor.getColumnIndex("EPSNAME")));
                item.setExpressNum(cursor.getString(cursor.getColumnIndex("EPSNUM")));
                item.setExpressBody(cursor.getString(cursor.getColumnIndex("EPSBODY")));
                epsList.add(item);
            }
            cursor.close();
        }
        db.close();
        return epsList;
    }

    public void addAll (List<EpsItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(EpsItem item : list){
            ContentValues values = new ContentValues();
            values.put("epssituation",item.getExpressSituation());
            values.put("epsdate",item.getExpressDate());
            values.put("epsname",item.getExpressName());
            values.put("epsnum",item.getExpressNum());
            values.put("epsbody",item.getExpressBody());
            db.insert(TBNAME,null,values);
        }
        db.close();
    }

    public void deleteAll (){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }
    public void deleteEps(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"id = ?",new String[]{id});
        db.close();
    }
    public void updateEps( ContentValues values ,String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TBNAME,values,"id = ?",new String[]{id});
        db.close();
    }

}

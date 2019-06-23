package com.example.faione.epshelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddressManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public AddressManager(Context context){
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME2;
    }

    public void add(AddressItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("addressname",item.getAddressname());
        values.put("addressdot",item.getAddressdot());
        values.put("addresstime",item.getAddresstime());
        db.insert(TBNAME,null,values);
        db.close();
    }

    public List<AddressItem>listAll(){
        List<AddressItem> addressList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,null,null,null,null,null);
        if (cursor!=null){
            addressList = new ArrayList<AddressItem>();
            while(cursor.moveToNext()){
                AddressItem item = new AddressItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setAddressname(cursor.getString(cursor.getColumnIndex("ADDRESSNAME")));
                item.setAddressdot(cursor.getString(cursor.getColumnIndex("ADDRESSDOT")));
                item.setAddresstime(cursor.getString(cursor.getColumnIndex("ADDRESSTIME")));
                addressList.add(item);
            }
            cursor.close();
        }
        db.close();
        return addressList;
    }

    public void addAll (List<AddressItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(AddressItem item : list){
            ContentValues values = new ContentValues();
            values.put("addressname",item.getAddressname());
            values.put("addressdot",item.getAddressdot());
            values.put("addresstime",item.getAddresstime());
            db.insert(TBNAME,null,values);
        }
        db.close();
    }

    public void deleteAll (){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }
    public void deleteAds(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"id = ?",new String[]{id});
        db.close();
    }
    public void updateAds( ContentValues values ,String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TBNAME,values,"id = ?",new String[]{id});
        db.close();
    }

}

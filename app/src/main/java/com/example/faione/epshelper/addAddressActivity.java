package com.example.faione.epshelper;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class addAddressActivity extends Activity implements View.OnClickListener {
    EditText inpname;
    EditText inpdot;
    EditText inptime;
    TextView AUtx;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        inpname = findViewById(R.id.Editadname);
        inpdot = findViewById(R.id.Editaddot);
        inptime = findViewById(R.id.Editadtime);
        AUtx =findViewById(R.id.AandUtx);

        id = getIntent().getStringExtra("updateId");
        if (id != null && id.length()>0)
        {AUtx.setText("重新编辑快递点地址");

        } else{AUtx.setText("新增快递点地址");}
        Button btn = findViewById(R.id.btn_submit);
        btn.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (inpname.getText().toString().length() > 0 && inpdot.getText().toString().length() > 0 && inptime.getText().toString().length() > 0) {
           if (id != null && id.length()>0) {
               ContentValues contentValues = new ContentValues();
               contentValues.put("ADDRESSNAME", inpname.getText().toString());
               contentValues.put("ADDRESSDOT", inpdot.getText().toString());
               contentValues.put("ADDRESSTIME", inptime.getText().toString());
               AddressManager manager = new AddressManager(this);
               manager.updateAds(contentValues, id);
           }else{
               AddressManager manager = new AddressManager(this);
               manager.add(new AddressItem(inpname.getText().toString(), inpdot.getText().toString(), inptime.getText().toString()));

           }
        }finish();
    }
}
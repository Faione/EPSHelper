package com.example.faione.epshelper;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdressFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private ListView addresslistView;
    private AddresslistAdapter addresslistAdapter;
    private List<Map<String, Object>> adslist;
    TextView adshowtx;
    View addressview;
    private int deletId;

    public AdressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addressview = inflater.inflate(R.layout.fragment_adress , container, false);
        addresslistView = (ListView)addressview.findViewById(R.id.addresslist);
        adshowtx = (TextView) addressview.findViewById(R.id.ad_show_tx);
        adslist = getData();
        addresslistAdapter = new AddresslistAdapter(getActivity(), adslist);
        addresslistView.setAdapter(addresslistAdapter);
        addresslistView.setOnItemClickListener(this);
        addresslistView.setOnItemLongClickListener(this);
        Log.i("AdressFragment","onCreate");
        return addressview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(addresslistAdapter != null){
            adslist=getData();
            Log.i("ElseFragment","ellist="+adslist.size());
            addresslistAdapter = new AddresslistAdapter(getActivity(), adslist);
            addresslistView.setAdapter(addresslistAdapter);
        }
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        AddressManager manager = new AddressManager(getActivity());
        for (AddressItem item : manager.listAll()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("epsid", item.getId());
                map.put("addressname",item.getAddressname());
                map.put("addressdot",item.getAddressdot());
                map.put("addresstime",item.getAddresstime());
            list.add(map);
            adshowtx.setVisibility(View.GONE);
        } if(list.size()==0){
            adshowtx.setVisibility(View.VISIBLE);
        }
        return list;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getActivity(),addAddressActivity.class);
                startActivityForResult(add,1);
            }
        });
    }

    @Override
    //修改
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("WEkonItemClick", "clicked" + position);
        Map<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) addresslistView.getItemAtPosition(position);
        String updateId =String.valueOf((int) map.get("epsid"));
        Intent Reset = new Intent(getActivity(),addAddressActivity.class);
        Reset.putExtra("updateId",updateId);
        startActivityForResult(Reset,2);
    }
    //删除
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //获得数据库中该组数据的id
        Map<String, Object> map =new HashMap<String, Object>();
        map = (HashMap<String, Object>)addresslistView.getItemAtPosition(position);
            deletId =(int) map.get("epsid");
            builder.setTitle("提示").setMessage("请确认删除该条信息！").setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adslist.remove(position);
                    if (adslist.size() == 0) {
                        adshowtx.setVisibility(View.VISIBLE);
                    }
                    AddressManager manager = new AddressManager(getActivity());
                    manager.deleteAds(String.valueOf(deletId));
                    addresslistAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton("否", null);
            builder.create().show();
        return true;
    }
}

package com.example.faione.epshelper;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class ElseFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private ListView elslistView;
    private  TextView elstv;
    private EleslistviewAdapter eleslistviewAdapter;
    private List<Map<String, Object>> ellist;
    View elsview;
    private int deletId;

    public ElseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        elsview = inflater.inflate(R.layout.fragment_else , container, false);
        elslistView = (ListView)elsview.findViewById(R.id.elslistview);
        elstv = (TextView) elsview.findViewById(R.id.elsText);
        ellist = getData();
        eleslistviewAdapter = new EleslistviewAdapter(getActivity(), ellist);
        elslistView.setAdapter(eleslistviewAdapter);
        elslistView.setOnItemClickListener(this);
        elslistView.setOnItemLongClickListener(this);
        Log.i("ElseFragment","onCreate");
        return elsview;
    }

    @Override
    //CSDN博主Losileeya提供的用于检测Fragment生命周期外，Fragment的显示与隐藏
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) { Log.i("ElseFragment","onhidden");   // 不在最前端显示 相当于调用了onPause();
            return;
        }else{  Log.i("ElseFragment","onShow"); // 在最前端显示 相当于调用了onResume();
            //网络数据刷新
            if(eleslistviewAdapter != null){
                ellist=getData();
                Log.i("ElseFragment","ellist="+ellist.size());
                eleslistviewAdapter = new EleslistviewAdapter(getActivity(), ellist);
                elslistView.setAdapter(eleslistviewAdapter);
                }
        }
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        EpsManager manager = new EpsManager(getActivity());
        for (EpsItem item : manager.listAll()) {
            if( item.getExpressSituation().equals("已收")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("epsid", item.getId());
                map.put("elsedate", item.getExpressDate());
                map.put("elsename", item.getExpressName());
                map.put("elsesituation", item.getExpressSituation());
                list.add(map);
            }
        }
        if(list.size()==0){
            elstv.setText("您还未收取任何一件快递");
        }else{
            elstv.setText("您已收取了"+list.size()+"件快递");
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //获得数据库中该组数据的id
        Map<String, Object> map =new HashMap<String, Object>();
        map = (HashMap<String, Object>)elslistView.getItemAtPosition(position);
        deletId =(int) map.get("epsid");

        builder.setTitle("提示").setMessage("请确认删除该条信息！").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //用户只能在同步短信，以及查看已验收短信时才能从数据库中删除信息
                ellist.remove(position);
                if(ellist.size()==0){
                    elstv.setText("您还未收取任何一件快递");
                }else {
                    elstv.setText("您已收取了"+ellist.size()+"件快递");
                }
                EpsManager manager = new EpsManager(getActivity());
                manager.deleteEps(String.valueOf(deletId));
                eleslistviewAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}

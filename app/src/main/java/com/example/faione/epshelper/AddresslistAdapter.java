package com.example.faione.epshelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class AddresslistAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public AddresslistAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public final class Control{
        public TextView title1;
        public TextView title2;
        public TextView title3;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Control control=null;
        if(convertView==null){
            control=new Control();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.addresslistview, null);
            control.title1=(TextView)convertView.findViewById(R.id.addressname);
            control.title2=(TextView)convertView.findViewById(R.id.addressdot);
            control.title3=(TextView)convertView.findViewById(R.id.addresstime);

            convertView.setTag(control);
        }else{
            control=(Control)convertView.getTag();
        }
        //绑定数据

        control.title1.setText((String)data.get(position).get("addressname"));
        control.title2.setText((String)data.get(position).get("addressdot"));
        control.title3.setText((String)data.get(position).get("addresstime"));

        return convertView;
    }

}


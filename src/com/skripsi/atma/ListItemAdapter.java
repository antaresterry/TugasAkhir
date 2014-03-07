package com.skripsi.atma;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListItemAdapter extends BaseAdapter {
    private static ArrayList<ListItem> searchArrayList;
 
    private LayoutInflater mInflater;
 
    public ListItemAdapter(Context context, ArrayList<ListItem> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return searchArrayList.size();
    }
 
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitemrow, null);
            holder = new ViewHolder();
            holder.txtcol1 = (TextView) convertView.findViewById(R.id.col1);
            holder.txtcol2 = (TextView) convertView.findViewById(R.id.col2);
            holder.txtcol3 = (TextView) convertView.findViewById(R.id.col3);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtcol1.setText(searchArrayList.get(position).getcol1());
        holder.txtcol2.setText(searchArrayList.get(position).getcol2());
        holder.txtcol3.setText(searchArrayList.get(position).getcol3());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtcol1;
        TextView txtcol2;
        TextView txtcol3;
    }
}
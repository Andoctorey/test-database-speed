package by.yegorov.testdb;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapter extends BaseAdapter {

    private static final String TAG = LogAdapter.class.getName();
    private final LayoutInflater layoutInflater;
    private ArrayList<String> logs = new ArrayList<>();

    public LogAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public String getItem(int position) {
        return logs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;
        if (view == null) {
            view = (TextView) layoutInflater.inflate(R.layout.item_log, null);
        }
        view.setText(logs.get(position));
        return view;
    }


    public void addItem(String log) {
        logs.add(log);
        notifyDataSetChanged();
    }
}

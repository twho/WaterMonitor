package com.michaelho.watermonitor.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelho.watermonitor.R;

/**
 * Created by Administrator on 2015/8/12.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private String[] functions;
    private int[] drawables;

    public DrawerListAdapter(Context context, String[] functions, int[] drawables) {
        this.context = context;
        this.functions = functions;
        this.drawables = drawables;
    }

    @Override
    public int getCount() {
        return functions.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        layoutInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drawer_list_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.tvFunction = (TextView) convertView
                    .findViewById(R.id.drawer_list_item_tv);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.drawer_list_item_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFunction.setText(functions[position]);
        viewHolder.tvFunction.setTextColor(context.getResources().getColor(R.color.white));
        Drawable d = context.getResources().getDrawable(drawables[position]);
        viewHolder.imageView.setImageDrawable(d);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvFunction;
    }
}

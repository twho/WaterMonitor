package com.michaelho.watermonitor.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.constants.RoomConstants;
import com.michaelho.watermonitor.constants.RoomDetailsConstants;
import com.michaelho.watermonitor.objects.Room;
import com.michaelho.watermonitor.util.ImageUtilities;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/13.
 */
public class RoomListAdapter extends BaseAdapter implements RoomConstants, RoomDetailsConstants{

    private Context context;
    private ArrayList<HashMap<String, Object>> list;
    private int floor;
    private LayoutInflater layoutInflater;
    private sqlOpenHelperRoomData sqliteRoomList;
    private Bitmap bmp;

    public RoomListAdapter(Context context, ArrayList<HashMap<String, Object>> list, int floor) {
        this.context = context;
        this.list = list;
        this.floor = floor;
        sqliteRoomList = new sqlOpenHelperRoomData(context);
    }

    @Override
    public int getCount() {
        if(floor == 1){
            return 3;
        }else{
            return 4;
        }
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
            convertView = layoutInflater.inflate(R.layout.fragment_overview_list_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.fragment_overview_list_item_tv1);
            viewHolder.tvToday = (TextView) convertView
                    .findViewById(R.id.fragment_overview_list_item_tv2);
            viewHolder.tvTodayFee = (TextView) convertView
                    .findViewById(R.id.fragment_overview_list_item_tv3);
            viewHolder.tvCompare = (TextView) convertView
                    .findViewById(R.id.fragment_overview_list_item_tv4);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.fragment_overview_list_item_iv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Room room = sqliteRoomList.getFullDetail(position + 1);
        if(floor == 1){
            bmp =  ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), firstFloorIMGList[position]),
                    (int) (context.getResources().getDimension(R.dimen.img_width)));
            viewHolder.tvName.setText(firstFloorList[position]);
        }else{
            bmp =  ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), secondFloorIMGList[position]),
                    (int) (context.getResources().getDimension(R.dimen.img_width)));
            viewHolder.tvName.setText(secondFloorList[position]);
        }
        viewHolder.imageView.setImageBitmap(bmp);
        viewHolder.tvToday.setText("Today's consumption(kw): " + (int) ((Math.random() * 99 + 101)) * 15 + "");
        viewHolder.tvTodayFee.setText("Estimated price: NT. " + (int) ((Math.random() * 99 + 101)) * 5 + "");
        viewHolder.tvCompare.setText("Last 7 days: " + (int) ((Math.random() * 100)) + "" + " %");
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvToday;
        TextView tvTodayFee;
        TextView tvCompare;
    }

}

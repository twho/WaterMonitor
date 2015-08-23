package com.michaelho.watermonitor.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.objects.SingleMessage;
import com.michaelho.watermonitor.util.ImageUtilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/17.
 */
public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> list;
    private LayoutInflater layoutInflater;
    private sqlOpenHelperMessage sqliteMessageList;

    public MessageListAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        sqliteMessageList = new sqlOpenHelperMessage(context);
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = layoutInflater.inflate(R.layout.fragment_message_list_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.fragment_message_list_item_tv1);
            viewHolder.tvDetail = (TextView) convertView
                    .findViewById(R.id.fragment_message_list_item_tv3);
            viewHolder.ivSender = (ImageView) convertView
                    .findViewById(R.id.fragment_message_list_item_iv1);
            viewHolder.ivIfRead = (ImageView) convertView
                    .findViewById(R.id.fragment_message_list_item_iv2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(sqliteMessageList.getAllMessageDetail() != null) {
            SingleMessage singleMessage = sqliteMessageList.getFullDetail(list.size()-position);
            viewHolder.tvTitle.setText(singleMessage.getTitle());
            setIfRead(viewHolder.ivIfRead, singleMessage.getIfRead());

            setSenderImg(viewHolder.ivSender, singleMessage.getSender());
            if (singleMessage.getDetail().length() > 25) {
                viewHolder.tvDetail.setText(singleMessage.getDetail().substring(0, 25)+"...");
            } else {
                viewHolder.tvDetail.setText(singleMessage.getDetail());
            }
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivSender;
        ImageView ivIfRead;
        TextView tvTitle;
        TextView tvDetail;
    }

    //Sender string = "1111" or "2111" or "2333"
    private void setSenderImg(ImageView iv, String sender){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        if("1".equalsIgnoreCase(sender.substring(0,1))){
            switch (Integer.valueOf(sender.substring(1,4))){
                case 111:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_wc);
                    break;
                case 222:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_shower);
                    break;
                case 333:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_kit);
                    break;
            }
        }else if("2".equalsIgnoreCase(sender.substring(0,1))){
            switch (Integer.valueOf(sender.substring(1,4))){
                case 111:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_wc);
                    break;
                case 222:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_shower);
                    break;
                case 333:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_kit);
                    break;
                case 444:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_garden);
                    break;
            }
        }
        iv.setImageBitmap(
                ImageUtilities.getRoundedCroppedBitmap(bmp, (int) (context.getResources().getDimension(R.dimen.img_width))));

    }

    private void setIfRead(ImageView iv, String ifRead){
        if(ifRead.equals("0")){
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_message_read);
            iv.setImageBitmap(
                    ImageUtilities.getRoundedCroppedBitmap(bmp, (int) (context.getResources().getDimension(R.dimen.img_width))));
        }else{
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_message_mail);
            iv.setImageBitmap(
                    ImageUtilities.getRoundedCroppedBitmap(bmp, (int) (context.getResources().getDimension(R.dimen.img_width))));
        }
    }
}

package com.michaelho.watermonitor.objects;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.util.ImageUtilities;

/**
 * Created by Administrator on 2015/8/15.
 */
public class WaterSource {
    private View view;
    private Context context;
    private String roomName;
    private String lastTime;
    private String data1;
    private String data2;
    private Bitmap bmp;
    private String waterIfUse;

    public WaterSource(final Context context, final String roomName, final String lastTime, final Bitmap bmp, final String data1, final String data2, final String waterIfUse) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.fragment_map_watersource, null);
        this.context = context;
        this.roomName = roomName;
        this.lastTime = lastTime;
        this.data1 = data1;
        this.data2 = data2;
        this.bmp = bmp;
        this.waterIfUse = waterIfUse;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagDetail();
            }
        });
        ImageView iv = (ImageView) view.findViewById(R.id.fragment_map_watersource_iv);
        if("1".equalsIgnoreCase(waterIfUse)){
            iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.water_in_use));
        }else{
            iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.water_not_use));
        }
    }

    private void showTagDetail(){
        final Dialog dialog = new Dialog(context);
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.fragment_map_watersource_detail, null);
        TextView tvTitle = (TextView) v.findViewById(R.id.fragment_map_watersource_detail_tv1);
        TextView tvLastTime = (TextView) v.findViewById(R.id.fragment_map_watersource_detail_tv2);
        TextView tvData1 = (TextView) v.findViewById(R.id.fragment_map_watersource_detail_tv3);
        TextView tvData2 = (TextView) v.findViewById(R.id.fragment_map_watersource_detail_tv4);
        ImageView ivRoom = (ImageView) v.findViewById(R.id.fragment_map_watersource_detail_iv);
        ImageButton ibDismiss = (ImageButton) v.findViewById(R.id.map_fragment_dialog_iB1);
        ibDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ImageButton ibGoDetail = (ImageButton) v.findViewById(R.id.map_fragment_dialog_iB2);
        tvTitle.setText(getRoomName());
        tvLastTime.setText("Recent use: "+getLastTime());
        tvData1.setText("Last hour: "+getData1() + " L");
        tvData2.setText("Last day: " + getData2() + " L");
        ivRoom.setImageBitmap(ImageUtilities.getRoundedCroppedBitmap(getImage(), (int) (context.getResources().getDimension(R.dimen.img_width))));
        dialog.setContentView(v);
        dialog.show();
    }

    public View getView(){
        return view;
    }

    public void setView(View view){
        this.view = view;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Bitmap getImage() {
        return bmp;
    }

    public void setImage(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

}

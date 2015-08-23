package com.michaelho.watermonitor.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.michaelho.watermonitor.MainActivity;
import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.constants.RoomDetailsConstants;
import com.michaelho.watermonitor.objects.WaterSource;
import com.michaelho.watermonitor.tools.AlertDialogManager;
import com.michaelho.watermonitor.util.ImageUtilities;
import com.michaelho.watermonitor.util.TimeUtilities;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/13.
 */
public class MapFragment extends Fragment implements RoomDetailsConstants {

    private static Context context;
    private ImageUtilities iu;
    private AlertDialogManager adm;
    private static String FIRSTFLOOR = "1F First Floor";
    private static String SECONDFLOOR = "2F Second Floor";
    //Fragment View
    View view;
    private ImageButton btn_refresh;
    private ImageButton btn_first, btn_second;
    private ArrayList<HashMap<String, Object>> roomList;
    private ImageView ivMap;
    private TextView title1, title2, tv;
    private DecimalFormat format;
    private Bitmap bmp;
    private RelativeLayout rl;
    private RelativeLayout.LayoutParams params;
    private String xPos, yPos;

    //clickListener
    private MapClickListener mapClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapClickListener = new MapClickListener();
        context = getActivity();
//        new TaskGetEvent().execute(COMMAND_GET_EVENTS);
        init();
        return view;
    }

    private void init() {
        findViews();
//        sqlEvents = new sqlOpenHelper_events(context);
        adm = new AlertDialogManager();
        iu = new ImageUtilities();
        format = new DecimalFormat("#.#");
        params = new RelativeLayout.LayoutParams(MainActivity.screenWidth / 8, MainActivity.screenWidth / 8);
//        eventList = new ArrayList<HashMap<String, Object>>();
//        eventList = sqlEvents.getAllEvents();
        bmp = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.home_map_first)).getBitmap();
        ivMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("POSITION", "圖片大小：" + ivMap.getWidth() + "   " + ivMap.getHeight());
                Log.d("POSITION", "現在位置：" + event.getX() + "   " + event.getY());
                Log.d("POSITION", "螢幕大小：" + MainActivity.screenWidth + "   " + MainActivity.screenHeight);
                return false;
            }
        });
        btn_first.setOnClickListener(mapClickListener);
        btn_second.setOnClickListener(mapClickListener);
        btn_refresh.setOnClickListener(mapClickListener);
        setSpot(FIRSTFLOOR);
    }

    private void findViews() {
        ivMap = (ImageView) view.findViewById(R.id.fragment_map_iv);
        title1 = (TextView) view.findViewById(R.id.fragment_map_tvTitle);
        title2 = (TextView) view.findViewById(R.id.fragment_map_tvTitle2);
        tv = (TextView) view.findViewById(R.id.fragment_map_tv);
        btn_refresh = (ImageButton) view.findViewById(R.id.fragment_map_imageButton1);
        btn_first = (ImageButton) view.findViewById(R.id.fragment_map_firstFloor);
        btn_second = (ImageButton) view.findViewById(R.id.fragment_map_secondFloor);
        rl = (RelativeLayout) view.findViewById(R.id.fragment_map_rl);
    }

    private void setSpot(String floor) {
        if (true) {
            if ("1F First Floor".equalsIgnoreCase(floor)) {
                for (int k = 0; k < 3; k++) {
                    Float x_pos = Float.valueOf(firstFloorListPosX[k]);
                    Float y_pos = Float.valueOf(firstFloorListPosY[k]);
                    RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(MainActivity.screenWidth / 5, MainActivity.screenWidth / 5);
                    parameter.leftMargin = (int) (x_pos * 1) - parameter.width / 2;
                    parameter.topMargin = (int) (y_pos * 1) - parameter.height;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), firstFloorIMGList[k]);
                    WaterSource waterSource = new WaterSource(context, firstFloorList[k], TimeUtilities.getTimeyyyy_MM_dd_HH_mm(), bmp, "1.257", "8.45", "0");
                    rl.addView(waterSource.getView(), parameter);
                }
            } else {
                for (int k = 0; k < 4; k++) {
                    Float x_pos = Float.valueOf(secondFloorListPosX[k]);
                    Float y_pos = Float.valueOf(secondFloorListPosY[k]);
                    RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(MainActivity.screenWidth / 5, MainActivity.screenWidth / 5);
                    parameter.leftMargin = (int) (x_pos * 1) - parameter.width / 2;
                    parameter.topMargin = (int) (y_pos * 1) - parameter.height;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), secondFloorIMGList[k]);
                    WaterSource waterSource = new WaterSource(context, secondFloorList[k], TimeUtilities.getTimeyyyy_MM_dd_HH_mm(), bmp, "2.767", "9.89", "0");
                    rl.addView(waterSource.getView(), parameter);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (sqlEvents == null) {
//            sqlEvents = new sqlOpenHelper_events(context);
//        }
        if (context == null) {
            context = getActivity();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (sqlEvents != null) {
//            sqlEvents.close();
//            sqlEvents = null;
//        }
    }
    private void refreshViews(RelativeLayout rl, ImageView iv, TextView tv1, TextView tv2, String floor){
        rl.removeAllViews();
        rl.addView(iv);
        rl.addView(tv1);
        rl.addView(tv2);
        tv.setText(floor);
    }

    public class MapClickListener implements View.OnClickListener {
        Context context = getActivity();

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_map_imageButton1:
                    break;
                case R.id.fragment_map_firstFloor:
                    ivMap.setImageDrawable(getResources().getDrawable(R.drawable.home_map_first));
                    refreshViews(rl, ivMap, title1, title2, FIRSTFLOOR);
                    setSpot(FIRSTFLOOR);
                    break;
                case R.id.fragment_map_secondFloor:
                    ivMap.setImageDrawable(getResources().getDrawable(R.drawable.home_map_second));
                    refreshViews(rl, ivMap, title1, title2, SECONDFLOOR);
                    setSpot(SECONDFLOOR);
                    break;
            }
        }
    }
}

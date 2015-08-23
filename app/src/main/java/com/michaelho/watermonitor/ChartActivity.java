package com.michaelho.watermonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelho.watermonitor.constants.RoomDetailsConstants;
import com.michaelho.watermonitor.fragment.OverviewFragment;
import com.michaelho.watermonitor.tools.AlertDialogManager;
import com.michaelho.watermonitor.util.AChartUtilities;
import com.michaelho.watermonitor.util.ImageUtilities;

/**
 * Created by Administrator on 2015/8/20.
 */
public class ChartActivity extends Activity implements RoomDetailsConstants{

    //tools
    private AlertDialogManager adm;
    private AChartUtilities aChartUtilities;

    //UI
    private View view;
    private LinearLayout ll1, ll2, ll3;
    private ImageButton btn_back;
    private View barView1, barView2, barView3;
    private ImageView iv;
    private TextView tv;

    //info
    private int room;
    private int floor;

    //Stats
    private String[][] DialyStat = {{"1", "2"}, {"2", "19"}, {"3", "17"},
            {"4", "16"}, {"5", "15"}, {"6", "11"}, {"7", "8"}, {"8", "8"}, {"9", "8"}, {"10", "8"}, {"11", "8"}, {"12", "8"}, {"13", "8"}, {"14", "8"}, {"15", "8"},
            {"16", "11"}, {"17", "8"}, {"18", "8"}, {"19", "8"}, {"20", "8"}, {"21", "11"}, {"22", "8"}, {"23", "23"}, {"24", "8"}};
    private String[][] WeeklyStat = {{"1", "54"}, {"2", "42"}, {"3", "78"},
            {"4", "99"}, {"5", "92"}, {"6", "89"}, {"7", "65"}};
    private String[][] MonthlyStat = {{"1", "54"}, {"2", "42"}, {"3", "78"},
            {"4", "99"}, {"5", "92"}, {"6", "89"}, {"7", "65"}, {"8", "78"}, {"9", "85"}, {"10", "87"}, {"11", "81"}, {"12", "92"}, {"13", "108"}, {"14", "118"}, {"15", "77"},
            {"16", "110"}, {"17", "78"}, {"18", "66"}, {"19", "58"}, {"20", "57"}, {"21", "65"}, {"22", "8"}, {"23", "8"}, {"24", "54"}, {"25", "42"}, {"26", "78"},
            {"27", "99"}, {"28", "92"}, {"29", "89"}, {"30", "65"}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        adm = new AlertDialogManager();
        init();
    }

    private void init() {
        Bundle b = getIntent().getExtras();
        room = b.getInt("ROOM");
        floor = b.getInt("FLOOR");
        adm = new AlertDialogManager();
        aChartUtilities = new AChartUtilities(this);
        findViews();

//        sqlRoomList = new sqlOpenHelperRoomData(getActivity());
    }

    private void findViews() {
        iv = (ImageView) this.findViewById(R.id.fragment_chart_dashboard_iv);
        tv = (TextView) this.findViewById(R.id.fragment_chart_dashboard_title);
        Bitmap bmp;
        if(floor == 1){
            bmp = ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(this.getResources(), firstFloorIMGList[room]),
                    (int) (getResources().getDimension(R.dimen.img_width)));
            tv.setText(firstFloorList[room]);
        }else{
            bmp = ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(this.getResources(), secondFloorIMGList[room]),
                    (int) (getResources().getDimension(R.dimen.img_width)));
            tv.setText(secondFloorList[room]);
        }
        iv.setImageBitmap(bmp);
        ll1 = (LinearLayout) this.findViewById(R.id.fragment_chart_dashboard_ll1);
        ll2 = (LinearLayout) this.findViewById(R.id.fragment_chart_dashboard_ll2);
        ll3 = (LinearLayout) this.findViewById(R.id.fragment_chart_dashboard_ll3);
        try{
            barView1 = aChartUtilities.getBarChart("Today's Water Use", "ErrCode", "Used Water(L)", DialyStat);
            ll1.removeAllViews();
            ll1.addView(barView1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 600));

        }catch(Exception e){

        }
        try{
            barView2 = aChartUtilities.getBarChart("Weekly Water Use", "ErrCode", "Used Water(L)", WeeklyStat);
            ll2.removeAllViews();
            ll2.addView(barView2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 600));

        }catch(Exception e){

        }
        try{
            barView3 = aChartUtilities.getBarChart("Monthly Water Use", "ErrCode", "Used Water(L)", MonthlyStat);
            ll3.removeAllViews();
            ll3.addView(barView3, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 600));

        }catch(Exception e){

        }
        btn_back = (ImageButton) this.findViewById(R.id.fragment_chart_iB1);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

package com.michaelho.watermonitor.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.michaelho.watermonitor.ChartActivity;
import com.michaelho.watermonitor.MainActivity;
import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.constants.RoomConstants;
import com.michaelho.watermonitor.constants.RoomDetailsConstants;
import com.michaelho.watermonitor.constants.ServerSQLcommands;
import com.michaelho.watermonitor.objects.Room;
import com.michaelho.watermonitor.tools.AlertDialogManager;
import com.michaelho.watermonitor.tools.RoomListAdapter;
import com.michaelho.watermonitor.tools.sqlOpenHelperRoomData;
import com.michaelho.watermonitor.util.AChartUtilities;
import com.michaelho.watermonitor.util.ImageUtilities;
import com.michaelho.watermonitor.util.PHPUtilities;
import com.michaelho.watermonitor.util.TimeUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/13.
 */
public class OverviewFragment extends Fragment implements RoomConstants, ServerSQLcommands, RoomDetailsConstants{

    View view;
    private static Context context;
    private AChartUtilities aChartUtilities;
    private AlertDialogManager adm;
    private ArrayList<HashMap<String, Object>> listFirstFloor = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> listSecondFloor = new ArrayList<HashMap<String, Object>>();
    private RoomListAdapter roomListAdapter;
    private ListView roomsListView1, roomsListView2;
    private sqlOpenHelperRoomData sqlRoomList;

    //Dialog
    private Spinner sp;
    private ImageView imageView;
    private TextView tvTitle;
    private ImageButton btn_cancel, btn_detail;
    private String spinnerSelect;
    private LinearLayout ll;
    private Dialog dialog;
    private View barView;
    private int base, room;
    private String[][] DialyStat={{"1","20"},{"2","19"},{"3","17"},
            {"4","16"},{"5","15"},{"6","11"},{"7","8"},{"8","8"},{"9","8"},{"10","8"},{"11","8"},{"12","8"},{"13","8"},{"14","8"},{"15","8"},
            {"16","11"},{"17","8"},{"18","8"},{"19","8"},{"20","8"},{"21","8"},{"22","8"},{"23","8"},{"24","8"}};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        init();
        return view;
    }

    private void init() {
        new TaskGetRoomStats().execute(MainActivity.MacAddr);
        context = getActivity();
        adm = new AlertDialogManager();
        aChartUtilities = new AChartUtilities(getActivity());
        findViews();
        sqlRoomList = new sqlOpenHelperRoomData(getActivity());
        roomsListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                showDetailDialog(getActivity(), position);
            }
        });
        roomsListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                showDetailDialog(getActivity(), position);
            }
        });
    }

    private void findViews(){
        roomsListView1 = (ListView) view.findViewById(R.id.fragment_overview_listView1);
        roomsListView2 = (ListView) view.findViewById(R.id.fragment_overview_listView2);
    }

    private void setListView(int floor){
        if (floor == 1){
            listFirstFloor = sqlRoomList.getRoomListByFloor("1");
            if(listFirstFloor != null) {
                roomListAdapter = new RoomListAdapter(getActivity(), listFirstFloor, 1);
                roomsListView1.setAdapter(roomListAdapter);
            }
        }else{
            listSecondFloor = sqlRoomList.getRoomListByFloor("2");
            if(listSecondFloor != null){
                roomListAdapter = new RoomListAdapter(getActivity(), listSecondFloor, 2);
                roomsListView2.setAdapter(roomListAdapter);
            }
        }
        roomsListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailDialog(getActivity(), position, 1);
            }
        });
        roomsListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailDialog(getActivity(), position, 2);
            }
        });
    }

    public void showDetailDialog(Context context, int position, int floor) {
        room = position;
        base = floor;
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.fragment_overview_dialog, null);
        ll = (LinearLayout) dialogView.findViewById(R.id.fragment_home_dashboard_ll);
        btn_cancel = (ImageButton) dialogView.findViewById(R.id.overview_fragment_dialog_iB1);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_detail = (ImageButton) dialogView.findViewById(R.id.overview_fragment_dialog_iB2);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChartActivity.class);
                Bundle b = new Bundle();
                b.putInt("ROOM", room);
                b.putInt("FLOOR", base);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        try{
            barView = aChartUtilities.getBarChart(spinnerSelect, "ErrCode", "Used Water(L)", DialyStat);
            ll.removeAllViews();
            ll.addView(barView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 800));

        }catch(Exception e){

        }
        imageView = (ImageView) dialogView.findViewById(R.id.fragment_home_dashboard_iv);
        tvTitle = (TextView) dialogView.findViewById(R.id.fragment_home_dashboard_title);
        Bitmap bmp;
        if(floor == 1){
            bmp = ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), firstFloorIMGList[position]),
                    (int) (getResources().getDimension(R.dimen.img_width)));
            tvTitle.setText(firstFloorList[position]);
        }else{
            bmp = ImageUtilities.getRoundedCroppedBitmap(BitmapFactory.decodeResource(context.getResources(), secondFloorIMGList[position]),
                    (int) (getResources().getDimension(R.dimen.img_width)));
            tvTitle.setText(secondFloorList[position]);
        }
        imageView.setImageBitmap(bmp);


        dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(context == null){
            context = getActivity();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (sqlBuildingDetail != null) {
//            sqlBuildingDetail.close();
//            sqlBuildingDetail = null;
//        }
    }

    private class TaskGetRoomStats extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return new PHPUtilities().getRoomStats(params);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("OverviewFragment", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                sqlRoomList = new sqlOpenHelperRoomData(getActivity());
                sqlRoomList.deletAll();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    String name = jsonData.getString("name");
                    String data = jsonData.getString("water_data");
                    String floor = jsonData.getString("floor");
                    String date = jsonData.getString("time_date");
                    String time = jsonData.getString("time");
                    Room room = new Room(name, data, floor, date, time);
                    sqlRoomList.insertDB(room);
                }
            } catch (Exception e) {
                Log.d("LabPartnerFragment", e.toString());
                new AlertDialogManager().showAlertDialog(context,
                        "Error", "The ERROR might be caused by: " + "\n"
                                + "1.The server is offline." + "\n"
                                + "2.Your device is offline.");
            }
            setListView(1);
            setListView(2);
            pd.dismiss();
        }
    }
}

package com.michaelho.watermonitor;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.michaelho.watermonitor.fragment.MapFragment;
import com.michaelho.watermonitor.fragment.MessageFragment;
import com.michaelho.watermonitor.fragment.OverviewFragment;
import com.michaelho.watermonitor.fragment.SettingsFragment;
import com.michaelho.watermonitor.tools.AlertDialogManager;
import com.michaelho.watermonitor.tools.DrawerListAdapter;
import com.michaelho.watermonitor.util.PHPUtilities;


public class MainActivity extends ActionBarActivity {

    //Preference
    public static final String PREF = "REG_PREF";
    public static final String PREF_NAME = "REG_PREF_NAME";
    public static final String PREF_EMAIL = "REG_PREF_EMAIL";
    public static final String PREF_REGID = "REG_PREF_REGID";
    //View
    final String[] listData ={"Overview", "Map View", "Message", "Settings"};
    final int[] drawables = {R.mipmap.img_view, R.mipmap.img_map, R.mipmap.img_mailbox, R.mipmap.img_settings};
    private ImageButton btn_func, btn_search;
    private DrawerListAdapter drawerListAdapter;
    private LinearLayout ll;
    private EditText ed1;
    private TextView tv;
    private AlertDialogManager adm;

    //screen info
    public static int screenWidth;
    public static int screenHeight;
    //personal info
    public static String myName;
    public static String MacAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adm = new AlertDialogManager();
        initMac();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        new TaskCheckMacExist().execute(MacAddr);
    }

    private void initMac() {
        BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        MacAddr = myBluetoothAdapter.getAddress();
        if ("".equalsIgnoreCase(MacAddr)) {
            WifiManager wifiMan = (WifiManager) this
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            MacAddr = wifiInf.getMacAddress();
        }
        if ("".equalsIgnoreCase(MacAddr)) {
            adm.showMessageDialog(MainActivity.this, "Error",
                    "No mac address detected.");
        }
    }

    private void init(){
        //get screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        //set drawer
        drawerListAdapter = new DrawerListAdapter(getApplicationContext(), listData, drawables);
        final DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.activity_main_drawer_layout);
        final ListView navList = (ListView) this.findViewById(R.id.activity_main_drawer);
        btn_func = (ImageButton) this.findViewById(R.id.activity_main_imageButton1);
        tv = (TextView) this.findViewById(R.id.activity_main_title);
        tv.setText(listData[0]);
        ed1 = (EditText) this.findViewById(R.id.activity_main_drawer2_ed1);
        btn_func.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        btn_search = (ImageButton) this.findViewById(R.id.activity_main_imageButton2);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        ll = (LinearLayout) this.findViewById(R.id.activity_main_linear_layout);
        navList.setAdapter(drawerListAdapter);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){
                drawerLayout.setDrawerListener( new DrawerLayout.SimpleDrawerListener(){
                    Fragment fragment = null;

                    @Override
                    public void onDrawerClosed(View drawerView){
                        switch(pos){
                            case 0:
                                fragment = new OverviewFragment();
                                ll.setVisibility(View.VISIBLE);
                                tv.setText(listData[0]);
                                ed1.setEnabled(true);
                                btn_search.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                fragment = new MapFragment();
                                ll.setVisibility(View.VISIBLE);
                                tv.setText(listData[1]);
                                ed1.setEnabled(false);
                                btn_search.setVisibility(View.GONE);
                                break;
                            case 2:
                                fragment = new MessageFragment();
                                ll.setVisibility(View.VISIBLE);
                                tv.setText(listData[2]);
                                ed1.setEnabled(false);
                                btn_search.setVisibility(View.GONE);
                                break;
                            case 3:
                                fragment = new SettingsFragment();
                                ll.setVisibility(View.VISIBLE);
                                tv.setText(listData[3]);
                                ed1.setEnabled(false);
                                btn_search.setVisibility(View.GONE);
                                break;
                        }
                        super.onDrawerClosed(drawerView);
                        try {
                            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.activity_main_container,fragment);
                            transaction.commit();

                        } catch (Exception e) {
                            Log.e("TAG", e.toString());
                        }
                    }
                });
                drawerLayout.closeDrawer(navList);
            }
        });
        android.support.v4.app.FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.activity_main_container,new OverviewFragment());
        tx.commit();
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences(MainActivity.PREF,
                Context.MODE_PRIVATE).getString(MainActivity.PREF_NAME, "");
    }

    private class TaskCheckMacExist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = new PHPUtilities().checkMacExist(params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("macOk")) {
                startActivity(new Intent(MainActivity.this,
                        RegisterActivity.class));
                MainActivity.this.finish();
            }
        }

    }

    ;
}

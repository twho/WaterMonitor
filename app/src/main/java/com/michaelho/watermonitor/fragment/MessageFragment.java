package com.michaelho.watermonitor.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.constants.RoomConstants;
import com.michaelho.watermonitor.constants.RoomDetailsConstants;
import com.michaelho.watermonitor.objects.SingleMessage;
import com.michaelho.watermonitor.tools.MessageListAdapter;
import com.michaelho.watermonitor.tools.sqlOpenHelperMessage;
import com.michaelho.watermonitor.util.ImageUtilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/13.
 */
public class MessageFragment extends Fragment implements RoomDetailsConstants{

    private View view;
    private static Context context;
    private ListView lv;
    private MessageListAdapter messageListAdatpter;
    private sqlOpenHelperMessage sqlMessage;
    private ArrayList<HashMap<String, Object>> messageList;

    //Dialog
    private Dialog dialog;
    private DialogClickListener dialogClickListener;
    private TextView tvTitle, tvSender, tvReceiver, tvTime, tvDetail;
    private ImageView ivSender;
    private ImageButton ibClose, ibDel;
    private int posNow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        init();
        return view;
    }

    private void init(){
        context = getActivity();
        dialogClickListener = new DialogClickListener();
        lv = (ListView) view.findViewById(R.id.fragment_message_listView);
        sqlMessage = new sqlOpenHelperMessage(context);
        setListview();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sqlMessage == null) {
            sqlMessage = new sqlOpenHelperMessage(getActivity());
        }
        if (context == null){
            context = getActivity();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sqlMessage != null) {
            sqlMessage.close();
            sqlMessage = null;
        }
    }

    private void setListview(){
        messageList = sqlMessage.getAllMessageDetail();
        messageListAdatpter = new MessageListAdapter(context, messageList);
        lv.setAdapter(messageListAdatpter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setMailDialog(messageList.size()-position-1);
            }
        });
    }

    private void setMailDialog(int position){
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.fragment_message_dialog, null);
        findDialogViews(dialogView);
        posNow = position+1;
        SingleMessage singleMessage = sqlMessage.getFullDetail(position+1);
        String sender = singleMessage.getSender();
        tvTitle.setText(singleMessage.getTitle());

        setSenderImg(ivSender, tvSender, sender);
        tvReceiver.setText("Receiver: Me");
        tvTime.setText("Received time: "+singleMessage.getTime());
        tvDetail.setText(singleMessage.getDetail());
        sqlMessage.UpdateIfRead(singleMessage.getDetail());
        dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void setSenderImg(ImageView iv, TextView tvSender, String sender){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        if("1".equalsIgnoreCase(sender.substring(0,1))){
            switch (Integer.valueOf(sender.substring(1,4))){
                case 111:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_wc);
                    tvSender.setText(firstFloorList[0]);
                    break;
                case 222:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_shower);
                    tvSender.setText(firstFloorList[1]);
                    break;
                case 333:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_floor_kit);
                    tvSender.setText(firstFloorList[2]);
                    break;
            }
        }else if("2".equalsIgnoreCase(sender.substring(0,1))){
            switch (Integer.valueOf(sender.substring(1,4))){
                case 111:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_wc);
                    tvSender.setText(secondFloorList[0]);
                    break;
                case 222:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_shower);
                    tvSender.setText(secondFloorList[1]);
                    break;
                case 333:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_kit);
                    tvSender.setText(secondFloorList[2]);
                    break;
                case 444:
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.second_floor_garden);
                    tvSender.setText(secondFloorList[3]);
                    break;
            }
        }
        iv.setImageBitmap(
                ImageUtilities.getRoundedCroppedBitmap(bmp, (int) (context.getResources().getDimension(R.dimen.img_width))));

    }

    private void findDialogViews(View view){
        ivSender = (ImageView) view.findViewById(R.id.fragment_message_inbox_dialog_iv);
        tvTitle = (TextView) view.findViewById(R.id.fragment_message_inbox_dialog_title);
        tvSender = (TextView) view.findViewById(R.id.fragment_message_inbox_dialog_sender);
        tvReceiver = (TextView) view.findViewById(R.id.fragment_message_inbox_dialog_tv1);
        tvTime = (TextView) view.findViewById(R.id.fragment_message_inbox_dialog_tv2);
        tvDetail = (TextView) view.findViewById(R.id.fragment_message_inbox_dialog_container);
        ibClose = (ImageButton) view.findViewById(R.id.message_fragment_dialog_iB1);
        ibClose.setOnClickListener(dialogClickListener);
        ibDel = (ImageButton) view.findViewById(R.id.message_fragment_dialog_iB2);
        ibDel.setOnClickListener(dialogClickListener);
    }

    public class DialogClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.message_fragment_dialog_iB1:
                    dialog.dismiss();
                    break;
                case R.id.message_fragment_dialog_iB2:
                    dialog.dismiss();
                    sqlMessage.deleteDataById(posNow+"");
                    setListview();
                    break;
            }
        }
    }
}

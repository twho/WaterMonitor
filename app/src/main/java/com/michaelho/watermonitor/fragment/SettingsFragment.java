package com.michaelho.watermonitor.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.michaelho.watermonitor.MainActivity;
import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.tools.AlertDialogManager;
import com.michaelho.watermonitor.util.ImageUtilities;
import com.michaelho.watermonitor.util.PHPUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/13.
 */
public class SettingsFragment extends Fragment {

    // photo upload
    public static final int MODE_IMAGE_GALLERY = 3;
    public static final int REQUEST_CODE_IMAGE = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static ImageView personalPor;
    private static String USERNAME;
    public ImageView ivNow;
    public String porBuffer;
    private View rootView;
    // settings fragment UI
    private EditText edName, edMail;
    private LinearLayout ll1, ll2;
    // tools
    private AlertDialogManager adm;
    private ImageUtilities iu;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container,
                false);
        init();
        return rootView;
    }

    private void init() {
        USERNAME = MainActivity.getUserName(getActivity());
        adm = new AlertDialogManager();
        iu = new ImageUtilities();
        ll1 = (LinearLayout) rootView.findViewById(R.id.settings_fragment_ll1);
        ll2 = (LinearLayout) rootView.findViewById(R.id.settings_fragment_ll2);
        edName = (EditText) rootView.findViewById(R.id.settings_fragment_ed1);
        edMail = (EditText) rootView.findViewById(R.id.settings_fragment_ed2);
        personalPor = (ImageView) rootView
                .findViewById(R.id.settings_personal_portrait);
        new TaskGetMyPortrait().execute(MainActivity.MacAddr);
        // setSettingPortrait();
        edName.setEnabled(false);
        edMail.setEnabled(false);
        personalPor.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        getActivity());
                dialog.setTitle("Choose by : ")
                        .setPositiveButton("From Camera",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(takeIntent, REQUEST_CODE_CAMERA);
                                    }
                                })
                        .setNegativeButton("From My Photo",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent takeIntent = new Intent();
                                        takeIntent.setType("image/*");
                                        takeIntent
                                                .setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent
                                                        .createChooser(takeIntent,
                                                                "data source: "),
                                                REQUEST_CODE_IMAGE);
                                    }
                                }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                ContentResolver resolver = getActivity().getContentResolver();
                Uri uri = data.getData();
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
                    String path = iu.getPath(getActivity(), uri);
                    new TaskPostPortrait().execute(MainActivity.MacAddr, iu.base64EncodeToString(path));
                    setPortrait(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                new TaskPostPortrait().execute(MainActivity.MacAddr, iu.base64EncodeToString(bmp));
                setPortrait(bmp);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setPortrait(Bitmap bmp) {
        personalPor.setImageBitmap(iu.getRoundedCroppedBitmap(bmp, (int) (getActivity().getResources().getDimension(R.dimen.img_width))));
    }

    // if success: IBinsertIBimageIBok
    // if not: IBnotIBreg
    private class TaskPostPortrait extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            adm.showProgressDialog("Uploading", "Please wait...", dialog);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = new PHPUtilities().postPortrait(params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("result1", result);
            if (!"".equalsIgnoreCase(result)) {
                if (result.length() > 11) {
                    if ("IB".equalsIgnoreCase(result.substring(0, 2))
                            && result.split("IB")[3].equalsIgnoreCase("ok")) {
                        dialog.dismiss();
                        adm.showMessageDialog(getActivity(), "Upload finished",
                                "Your portrait has been uploaded.");
                    }
                }
            } else {
                adm.showMessageDialog(getActivity(), "Error", "Caused by: "
                        + "\n" + "1.No Internet connected." + "\n"
                        + "2.You have not registered yet.");
            }
        }
    }

    private class TaskGetMyPortrait extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(MainActivity.screenWidth / 7, MainActivity.screenWidth / 7);
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            parameter.rightMargin = 20;
            adm.showProgressDialog("Loading", "Please Wait...", dialog);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = new PHPUtilities().getMyPortrait(params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("DEBUG", result);
            try {
                ll1.removeAllViews();
                ll2.removeAllViews();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    if (!"".equalsIgnoreCase(jsonData.getString("image"))) {
                        if (USERNAME.equalsIgnoreCase(jsonData.getString("name"))) {
                            String my_name = jsonData.getString("name");
                            String my_email = jsonData.getString("email");
                            String image = jsonData.getString("image");
                            edName.setText(my_name);
                            edMail.setText(my_email);
                            personalPor.setImageBitmap(iu.getRoundedCroppedBitmap(ImageUtilities.decodeBase64(image),
                                    (int) (getActivity().getResources().getDimension(R.dimen.img_width))));
                            ImageView iv1 = new ImageView(getActivity());
                            iv1.setImageBitmap(iu.getRoundedCroppedBitmap(ImageUtilities.decodeBase64(image),
                                    (int) (getActivity().getResources().getDimension(R.dimen.img_width))));
                            ll1.addView(iv1,parameter);
                        } else {
                            String image = jsonData.getString("image");
                            ImageView iv2 = new ImageView(getActivity());
                            iv2.setImageBitmap(iu.getRoundedCroppedBitmap(ImageUtilities.decodeBase64(image),
                                    (int) (getActivity().getResources().getDimension(R.dimen.img_width))));
                            ll1.addView(iv2,parameter);
                        }


                        dialog.dismiss();
                    } else {
                        adm.showMessageDialog(getActivity(), "Error",
                                "You haven't set your personal portrait.");
                    }

                }
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
                new AlertDialogManager().showMessageDialog(getActivity(),
                        "Error", "The ERROR might be caused by: " + "\n"
                                + "1.The server is offline." + "\n"
                                + "2.Your device is offline.");
            }
        }
    }
}

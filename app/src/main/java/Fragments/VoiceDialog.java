package Fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import net.babiran.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import Handlers.DatabaseHandler;
import tools.AppConfig;
import tools.AudioRecorder;
import tools.MultipartRequest;

/**
 * Created by Mohammad on 6/16/2017.
 */

public class VoiceDialog extends DialogFragment {

    View v ;
    ImageView ok,play,exit ;

    File file ;
    String path ;

    public boolean IsPlay = false ;

    AudioRecorder audioRecorder ;

    public VoiceDialog(File file,String path){
        this.file = file ;
        this.path = path ;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.voice_dialog, container, false);
        getDialog().setTitle("VoiceDialog");


        ok = (ImageView) v.findViewById(R.id.ok);
        exit = (ImageView) v.findViewById(R.id.exit);
        play = (ImageView) v.findViewById(R.id.play);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceRequest(file);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("path in dialog",path);
                try {
                     audioRecorder = new AudioRecorder(path);
                    if(!IsPlay){
                        audioRecorder.playarcoding(path);
                        play.setImageResource(R.drawable.pauseicon);
                        IsPlay = true ;
                    }
                    else if(IsPlay){
                        audioRecorder.mp.stop();
                        play.setImageResource(R.drawable.playicon);
                        IsPlay = false ;
                    }

                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return v ;
    }
    public void VoiceRequest(File file){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        DatabaseHandler db = new DatabaseHandler(getActivity());
        String id = "-1";

        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("در حال ارسال سفارش ...");
        d.setIndeterminate(true);
        d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        d.setCancelable(false);
        d.show();
        // Add request params excluding files below

        if (db.getRowCount() > 0) {

            HashMap<String, String> userDetailsHashMap = db.getUserDetails();
            id = userDetailsHashMap.get("id");
        }
        HashMap<String, File> fileParams = new HashMap<>();
        fileParams.put("voice", file);

        MultipartRequest mMultipartRequest = new MultipartRequest(AppConfig.BASE_URL +"api/voiceOrder/upload/"+id,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        // error handling
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        d.dismiss();
                        Log.d("response in voice",response+"");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("success").equals("1")){
                                Toast.makeText(getActivity(),"سفارش صوتی شما ثبت شد",Toast.LENGTH_LONG).show();
                                getDialog().dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, fileParams
        );
        mMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(mMultipartRequest);
    }


    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        int w = (int) (width * 0.7);
        getDialog().getWindow().setLayout(w,w);
        getDialog().getWindow().setGravity(Gravity.CENTER);

    }



}

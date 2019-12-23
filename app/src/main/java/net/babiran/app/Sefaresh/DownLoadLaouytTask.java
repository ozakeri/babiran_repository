package net.babiran.app.Sefaresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.net.URL;



/**
 * Created by D on 4/15/2018.
 */

public class DownLoadLaouytTask extends AsyncTask<String,Void,Bitmap> {
    RelativeLayout relativeLayout;
    Context context;


    /*
    * USE   -===>>>      new DownLoadLaouytTask( holder.icon).execute("" String "");
    * */


    public DownLoadLaouytTask(RelativeLayout relativeLayout, Context context){

        this.relativeLayout = relativeLayout;
        this.context=context;
    }

    /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
     */
    protected Bitmap doInBackground(String...urls)
    {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result)
    {

        BitmapDrawable background = new BitmapDrawable(context.getResources(), BlurBuilder.blur(context,result));

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            relativeLayout.setBackgroundDrawable(background );
        } else {
            relativeLayout.setBackground(background);
        }


    }
}
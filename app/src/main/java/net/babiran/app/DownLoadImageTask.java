package net.babiran.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by D on 4/15/2018.
 */

public class DownLoadImageTask  extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;
    Context context;


    /*
    * USE   -===>>>      new DownLoadImageTask( holder.icon).execute("" String "");
    * */


    public DownLoadImageTask(ImageView imageView, Context context){

        this.imageView = imageView;
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
        BitmapDrawable background = new BitmapDrawable(context.getResources(), result);
        imageView.setBackgroundDrawable(background);

    }
}
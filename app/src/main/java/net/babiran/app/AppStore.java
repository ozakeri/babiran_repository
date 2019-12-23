package net.babiran.app;

import android.content.Context;
import android.content.SharedPreferences;



import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by D on 3/31/2018.
 */

public class AppStore
{
Context context;
    public AppStore(Context context) {
        this.context=context;
    }

    public static final  String BASE_URL="http://beautifyapp.ir/Jib/FilePhp/";
  //  public static final  String BASE_URL=  "http://wiateam.com/amin/Jib/FilePhp/";
    public static String URL_BACHGROUD="";
    public static final  String BASE_URL_Sms_Virfy="https://api.kavenegar.com/v1/3370344F39726D326238416A2F7169584B4A745257773D3D/verify/";

    public static   String TXT_SHOW_PRICE="";
    public static   String TXT_SHOW_DATE="";
    public static   String TXT_SHOW_DEC="";
    public static   String TXT_SHOW_NAME_BANKE="";
    public static   String TXT_SHOW_TAGS="";
    public static   String TXT_SHOW_Map="";
    public static   String TXT_SHOW="";
    /*
    SAVE DATA
     *  */
    public static final  String SAVE_DATA="SAVE_DATA";

    ///   KEYS
    public static final  String FONTS="FONTsSS";
    public static final  String COLOR="COLOsRS";


    public static final  String Keys_PHONE="Keys_PHONE";
    public static final  String Font_booj="iransans.ttf";
    ////////////////////////////

    public static final  String FAV="ZXSA";


    ///////////////////////////////////////////
    /*
    * Method SAVE DATA
    * */
    public String LoadMyShereKES( String Key)
    {
        SharedPreferences load =context. getSharedPreferences(AppStore.SAVE_DATA,0);
        String Result = load.getString(Key,"");
        return  Result;
    }
    public void SAVESHAREPREFRENCE(String VAluse,String KEYS)
    {
        SharedPreferences save = context.getSharedPreferences(AppStore.SAVE_DATA,MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.putString(KEYS,VAluse);
        editor.apply();



    }
    public String DeleteFirstEdn(String l)
    {
        String    strw = l.replaceAll("^.{" + 1 + "}", "");
        return strw.substring(0, strw.length() - 1);
    }
    public int LoadMyShereINT(Context context, String Key)
    {
        SharedPreferences load =context. getSharedPreferences(AppStore.SAVE_DATA,0);
        int Result = load.getInt(Key,12);
        return  Result;
    }
    public void SAVESHAREPREFRENCEINT(int VAluse,String KEYS,Context context)
    {
        SharedPreferences save = context.getSharedPreferences(AppStore.SAVE_DATA,MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.putInt(KEYS,VAluse);
        editor.apply();



    }

}

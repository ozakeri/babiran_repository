package net.babiran.app.Rss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.babiran.app.AppController;
import net.babiran.app.AppStore;
import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Servic.GETINGBlog;
import net.babiran.app.Servic.GetComm;
import net.babiran.app.Servic.GetSucc;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import tools.GlobalValues;
import tools.Util;
import ui_elements.MyButton;
import ui_elements.MyEditText;
import ui_elements.MyTextView;

public class ShowRssActivity extends AppCompatActivity {
    private AlertDialog prograsDialog;
    ImageView img_html_show;
    MyTextView txt, txt_html_ffff;
    MyTextView titl, txt_newsDate;
    String SSS;
    String Url, desc, Titlea, imgUrl = null;
    String RRR;
    private Toolbar toolbar;
    Typeface font;
    LinearLayout ln;
    int id_blog, LIKE;
    RecyclerView recyclerView;
    TextView txtLike, txt_titleComment;
    ImageView imgComment, imglike;
    boolean LikeDisLike = false;
    ImageView imgzoom, imgzoomOut;
    LinearLayout likeLn, lnComment, layout_img_html_show;
    private GlobalValues globalValues = new GlobalValues();
    private boolean isPush = false;
    private RelativeLayout relativeLayout;

    ImageView SaveAImg, Sahe;
    List<String> listC = new ArrayList<>();

    int size = 13;

    private AppController appController = AppController.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rss);

        Bundle pudhBndle = getIntent().getExtras();
        if (pudhBndle != null) {
            isPush = pudhBndle.getBoolean("isPush");
        }

        txt_html_ffff = findViewById(R.id.txt_html_ffff);
        imgzoom = findViewById(R.id.dlfngkjdfj);
        imgzoomOut = findViewById(R.id.dlfngkjkjdfj);
        img_html_show = findViewById(R.id.img_html_show);
        txt = findViewById(R.id.txt_html_show);
        titl = findViewById(R.id.txt_html_tit);
        txt_newsDate = findViewById(R.id.txt_newsDate);
        SaveAImg = findViewById(R.id.img_like_);
        Sahe = findViewById(R.id.img_share);
        relativeLayout = findViewById(R.id.jjjjjjjjjj);
        txt_titleComment = findViewById(R.id.txt_titleComment);
        txtLike = findViewById(R.id.count_like);
        imgComment = findViewById(R.id.img_comment);
        imglike = findViewById(R.id.img_like);
        likeLn = findViewById(R.id.dfdfdfdfd);
        lnComment = findViewById(R.id.dffdfdfdfd);
        ln = findViewById(R.id.ln_ln_ln);
        recyclerView = findViewById(R.id.recccccc);
        layout_img_html_show = findViewById(R.id.layout_img_html_show);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgUrl = bundle.getString("imgUrl");
            Url = bundle.getString("link");
            desc = bundle.getString("desc");
            Titlea = bundle.getString("title");

            titl.setText(Titlea);
            txt.setText(desc);
            Linkify.addLinks(txt, Linkify.ALL);
            System.out.println("Link====222" + Url);
            System.out.println("tit====222" + Titlea);
            System.out.println("description====" + desc);
            System.out.println("imgUrl====" + imgUrl);

            if (Url != null && Titlea != null && desc != null) {
                txt_newsDate.setVisibility(View.GONE);
                ln.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                txt_titleComment.setVisibility(View.GONE);
                Picasso.with(ShowRssActivity.this).load(imgUrl).into(img_html_show);
                return;
            }
        }

        Log.e("ID", AppConfig.id + "");

        font = Typeface.createFromAsset(getAssets(), "IRANSansMobile_Light.ttf");
        prograsDialog = new SpotsDialog(ShowRssActivity.this);
        prograsDialog.show();
        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPush) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
////////////////////////////////////////////

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     ListedGetLike();
            }
        });

        titl.setText(Titlea);
        if (SSS != null) {
            txt.setText(SSS);
        } else {
            txt.setText(desc);
        }
        txt.setTypeface(font);
        titl.setTypeface(font);

        if (!TextUtils.isEmpty(ListtoListActivity.ID_ME)) {
            ln.setVisibility(View.VISIBLE);

            if (appController.getResponse() != null) {
                List<GETINGBlog> s = appController.getResponse().body();
                for (int i = 0; i < s.size(); i++) {
                    prograsDialog.dismiss();

                    System.out.println("Url1111=======" + s.get(i).getId());
                    System.out.println("Url1111=======" + ListtoListActivity.ID_ME);

                    if (s.get(i).getId() == Integer.parseInt(ListtoListActivity.ID_ME)) {
                        //txt_html_ffff.setVisibility(View.VISIBLE);
                        //txt_html_ffff.setText(s.get(i).getCreatedAtInt());
                        txt_newsDate.setText(" تاریخ خبر " + Util.convertEnToPe(s.get(i).getCreatedAtInt()));
                        System.out.println("---------------" + s.get(i).getCreatedAtInt());
                        txtLike.setText(s.get(i).getLike() + 1 + "");
                        titl.setText(s.get(i).getSubject());

                        Pattern p = Pattern.compile("\\d+");
                        String SSS1 = null;
                        if (s.get(i).getBody() != null) {

                           /* Pattern pattern = Pattern.compile("123456789");
                            Linkify.TransformFilter username = new Linkify.TransformFilter() {
                                @Override
                                public String transformUrl(Matcher match, String url) {
                                    return "123456789";
                                }
                            };*/

                            // txt.setText("امید ذاکری  یک پیغامی 123456789 برای شما ارسال کرده است");
                            // Linkify.addLinks(txt, pattern, "myScheme://?username=", null, username);
                            //System.out.println("username====" + username);

                            txt.setText(s.get(i).getBody());
                            Matcher m = p.matcher(s.get(i).getBody());
                            String val1 = s.get(i).getBody();
                            while (m.find()) {
                                if (m.group().length() > 7) {
                                    Pattern pattern1 = Pattern.compile(m.group());
                                    Linkify.TransformFilter username1 = new Linkify.TransformFilter() {
                                        @Override
                                        public String transformUrl(Matcher match, String url) {
                                            return m.group();
                                        }
                                    };
                                    Linkify.addLinks(txt, pattern1, "tel:", null, username1);
                                    ;
                                }
                            }

                            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:123456789"));
                            startActivity(callIntent);*/
                        }

                        //txt.setText(SSS1);
                        Picasso.with(ShowRssActivity.this).load(s.get(i).getImageLink()).into(img_html_show);
                        imgUrl = s.get(i).getImageLink();
                        id_blog = s.get(i).getId();
                        ListedGetLike();
                        ListedGetCommet();

                        LISTFILL();

                    }
                }
            } else {
                Listed();
            }


            img_html_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowRssActivity.this, FullScreenActivity.class);
                    intent.putExtra("imgUrl", imgUrl);
                    startActivity(intent);
                }
            });

            lnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Di();

                }
            });

            likeLn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int ip = Integer.parseInt(txtLike.getText().toString());


                    if (LikeDisLike) //like nashode
                    {
                        LikeDisLike = false;
                        int p = ip - 1;
                        txtLike.setText(p + "");
                        imglike.setImageResource(R.drawable.ic_un_like);
                        ListedSetLike(0);

                    } else {
                        LikeDisLike = true;
                        int p = ip + 1;
                        txtLike.setText(p + "");
                        imglike.setImageResource(R.drawable.ic_lklik);
                        ListedSetLike(1);
                    }

                }
            });
        } else {

            if (Url != null) {
                new Title().execute();
            }

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ShowRssActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//                String id = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
//                        .itemView.findViewById(R.id.kjdefngkjf)).getText().toString();
//
//                Doilg(id);
//
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position)
//            {
//
//            }
//        }));

        AppStore appStore = new AppStore(this);
        if (appStore.LoadMyShereINT(ShowRssActivity.this, AppStore.COLOR) == 12) {

        } else {
            txt.setTextColor(appStore.LoadMyShereINT(ShowRssActivity.this, AppStore.COLOR));
        }

        txt.setTextSize(appStore.LoadMyShereINT(ShowRssActivity.this, AppStore.FONTS));

        imgzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int s = size;
                s = s + 1;
                if (s > 20) {
                    s = 20;
                }
                size = s;
                txt.setTextSize(s);
            }
        });
        imgzoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int s = size;
                s = s - 1;
                if (s > 20) {
                    s = 20;
                }
                size = s;
                txt.setTextSize(s);
                // startActivity(new Intent(ShowRssActivity.this,SettingActivity.class));
            }
        });

        Sahe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shree(titl.getText().toString(), txt.getText().toString());
            }
        });
        SaveAImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveID(id_blog + "");
            }
        });


    }

    private void LISTFILL() {
        listC.clear();
        AppStore appStore = new AppStore(this);
        String v = appStore.LoadMyShereKES(AppStore.FAV);

        if (!TextUtils.isEmpty(v)) {
            String[] a = v.split(",");
            Collections.addAll(listC, a);

            if (Ch(id_blog + "", listC)) {
                SaveAImg.setBackgroundResource(R.drawable.ic_list_);
            } else {
                SaveAImg.setBackgroundResource(R.drawable.ic_list_un);
            }

        } else {
            SaveAImg.setBackgroundResource(R.drawable.ic_list_un);
        }


    }

    private void Listed() {

        //String p = getIntent().getExtras().getString("id");
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<GETINGBlog> call = n.getBlogById(Integer.parseInt(ListtoListActivity.ID_ME));
            System.out.println("ID_ME====" + Integer.parseInt(ListtoListActivity.ID_ME));

            call.enqueue(new Callback<GETINGBlog>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<GETINGBlog> call, @NonNull retrofit2.Response<GETINGBlog> response) {
                    prograsDialog.dismiss();
                    GETINGBlog s = response.body();
                    System.out.println("response=======" + response.body());

                    if (s != null) {
                        txtLike.setText(s.getLike() + 1 + "");
                        titl.setText(s.getSubject());
                        txt.setText(s.getBody());
                        Picasso.with(ShowRssActivity.this).load(s.getImageLink()).into(img_html_show);
                        imgUrl = s.getImageLink();
                        txt_newsDate.setText(" تاریخ خبر " + Util.convertEnToPe(s.getCreatedAtInt()));
                        System.out.println("img====" + s.getImageLink());

                        id_blog = s.getId();
                        ListedGetLike();
                        ListedGetCommet();

                        LISTFILL();
                    }

                }

                @Override
                public void onFailure(Call<GETINGBlog> call, Throwable t) {

                }

            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }

    private void Di() {
        final Dialog dialog = new Dialog(this);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coustom_dilog_comment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();


        MyButton telegram = (MyButton) dialog.findViewById(R.id.eafbghm);

        final MyEditText txt_comment = dialog.findViewById(R.id.txt_comment);

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(txt_comment.getText().toString())) {
                    ListedSetCommet(txt_comment.getText().toString(), dialog);
                } else {
                    Toast.makeText(ShowRssActivity.this, "فیلد نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    //////////////////////////////////////////////////////////////
    private void ListedSetLike(int like) {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            System.out.println("AppConfig.id======" + AppConfig.id);
            Call<GetSucc> call = n.setLike(id_blog, Integer.parseInt(AppConfig.id), like);

            call.enqueue(new Callback<GetSucc>() {
                @Override
                public void onResponse(@NonNull Call<GetSucc> call, @NonNull retrofit2.Response<GetSucc> response) {

                    if (response.body().getSuccess() == 0) {
                        LIKE = 0;
                        //   imglike.setImageResource(R.drawable.ic_like);
                    } else if (response.body().getSuccess() == 1) {
                        LIKE = 1;
                        //      imglike.setImageResource(R.drawable.ic_un_like);
                    }

                }

                @Override
                public void onFailure(Call<GetSucc> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }

    private void ListedGetLike() {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<GetSucc> call = n.checkLike(id_blog, Integer.parseInt(AppConfig.id));

            call.enqueue(new Callback<GetSucc>() {
                @Override
                public void onResponse(@NonNull Call<GetSucc> call, @NonNull retrofit2.Response<GetSucc> response) {


                    if (response.body() != null) {
                        if (response.body().getLike() == 1) {
                            LIKE = 1;
                            LikeDisLike = true;
                            //   imglike.setImageResource(R.drawable.ic_like);
                        } else if (response.body().getLike() == 0) {
                            LIKE = 0;
                            LikeDisLike = false;
                            //     imglike.setImageResource(R.drawable.ic_un_like);
                        }
                    } else {
                        // LIKE=1;
                    }

                    if (LikeDisLike) {
                        imglike.setImageResource(R.drawable.ic_lklik);
                    } else {
                        imglike.setImageResource(R.drawable.ic_un_like);
                    }
                }

                @Override
                public void onFailure(Call<GetSucc> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                    LIKE = 1;
                    //imglike.setImageResource(R.drawable.ic_un_like);
                    LikeDisLike = false;
                }
            });
        } catch (Exception ex) {
            LIKE = 1;
            Log.e("response 3 :", ex.getMessage());
            imglike.setImageResource(R.drawable.ic_un_like);
        }


        //    prograsDialog.dismiss();
    }

    private void ListedSetCommet(String msg, final Dialog dialog) {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<GetSucc> call = n.add_comment(id_blog, Integer.parseInt(AppConfig.id), msg);

            call.enqueue(new Callback<GetSucc>() {
                @Override
                public void onResponse(@NonNull Call<GetSucc> call, @NonNull retrofit2.Response<GetSucc> response) {

                    dialog.dismiss();
                    if (response.body() != null){
                        if (response.body().getSuccess() == 0) {
                            //imglike.setImageResource(R.drawable.ic_like);
                            Toast.makeText(ShowRssActivity.this, "خطا در ارسال", Toast.LENGTH_SHORT).show();
                        } else if (response.body().getSuccess() == 1) {
                            Toast.makeText(ShowRssActivity.this, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetSucc> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                    dialog.dismiss();
                    Toast.makeText(ShowRssActivity.this, "با موفقیت ثبت نشد", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
            dialog.dismiss();
            Toast.makeText(ShowRssActivity.this, "با موفقیت ثبت نشد", Toast.LENGTH_SHORT).show();
        }


        //    prograsDialog.dismiss();
    }

    private void ListedGetCommet() {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GetComm>> call = n.getcomments(id_blog);

            call.enqueue(new Callback<List<GetComm>>() {
                @Override
                public void onResponse(@NonNull Call<List<GetComm>> call, @NonNull retrofit2.Response<List<GetComm>> response) {

                    List<GetComm> lists = response.body();
                    List<String> l = new ArrayList<>();

                    l.clear();
                    for (int i = 0; i < lists.size(); i++) {
                        l.add(lists.get(i).getComment() + "##" + lists.get(i).getCreatedAt() + "##" + lists.get(i).getRate() + "##" + lists.get(i).getId());
                    }

                    if (l.size() > 0) {
                        AdapterUserComm mAdapter = new AdapterUserComm(ShowRssActivity.this, l, id_blog);

                        recyclerView.setAdapter(mAdapter);
                    }
                    ;


                }

                @Override
                public void onFailure(Call<List<GetComm>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());

                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }


    private void Doilg(String id) {
        final String[] st = {""};
        final boolean like = false;
        final Dialog dialog = new Dialog(ShowRssActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coustom_dialog_vote);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


        final ImageView imgLike = (ImageView) dialog.findViewById(R.id.jhdbfhrf);

        final ImageView imgDisLike = (ImageView) dialog.findViewById(R.id.grg);

        MyButton btn = (MyButton) dialog.findViewById(R.id.jhdbrghfhrf);
        final ImageView clos = (ImageView) dialog.findViewById(R.id.sfdgadgdfgfgbadefb);


        clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!like) {
                    st[0] = "down";
                    imgDisLike.setImageResource(R.drawable.dilike);
                    imgLike.setImageResource(R.drawable.like);

                }
            }
        });
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!like) {
                    st[0] = "up";
                    imgLike.setImageResource(R.drawable.like);
                    imgDisLike.setImageResource(R.drawable.dilike);

                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(st[0]))
                    VoteCommet(dialog, st[0]);
            }
        });


    }

    private void VoteCommet(final Dialog dialog, String d) {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<GetSucc> call = n.setVote(id_blog, Integer.parseInt(AppConfig.id), d);

            call.enqueue(new Callback<GetSucc>() {
                @Override
                public void onResponse(@NonNull Call<GetSucc> call, @NonNull retrofit2.Response<GetSucc> response) {

                    if (response.code() == 200) {
                        dialog.dismiss();
                        Toast.makeText(ShowRssActivity.this, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        ListedGetCommet();
                    }

                }

                @Override
                public void onFailure(Call<GetSucc> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());

                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }

    //// ///////////////////////////////       HTML             ////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class Title extends AsyncTask<Void, Void, Void> {
        String title;
        Elements pngs;
        Elements png;

        @Override
        protected Void doInBackground(Void... params) {
            // Connect to the web site
            System.out.println("Url=======" + Url);
            Document doc = null;
            try {
                doc = Jsoup.connect(Url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pngs = doc.select("p[id=ctl00_ctl00_ContentPlaceHolder_ContentPlaceHolder_NewsContent4_BodyLabel]");//div[class=body]
            png = doc.select("div[class=MainImageBox]");

            //   Log.e("DOC 1  :   ",doc.text());
            Log.e("DOC 2  :   ", pngs.text());
            Log.e("DOC  3 :   ", png.text());

            SSS = pngs.text();


            for (Element img : png.select("img")) {
                Log.e("DOC  4 :   ", img.attr("src"));
                RRR = img.attr("src");
                Log.e("DOC  4 :   ", RRR);

            }

            //System.out.println("RRR=====" + RRR);
            //   Picasso.with(ShowRssActivity.this).load(RRR).into(img_html_show);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Picasso.with(ShowRssActivity.this).load(RRR).into(img_html_show);
            System.out.println("img==RRR==" + RRR);
            txt.setText(pngs.text());
            prograsDialog.dismiss();
            SaveAImg.setVisibility(View.GONE);
            //Log.e("DOC  4 :   ",result.toString());
            // Set title into TextView
            //  textView.setText("SXD"+SSS);
        }

    }


    private void Shree(String tit, String akhbar) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "\n\n\n" + tit + "\n\n\n\n";
            sAux = sAux + "\n\n\n" + akhbar + "\n\n\n\n";
            sAux = sAux + "http://babiran.net/website\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void SaveID(String cod) {

        List<String> listC = new ArrayList<>();
        List<String> d = new ArrayList<>();
        AppStore appStore = new AppStore(ShowRssActivity.this);
        String SSS = appStore.LoadMyShereKES(AppStore.FAV);
        Log.e("AAAAAAAAAS", SSS);
        String r = SSS + "," + cod;


        if (TextUtils.isEmpty(SSS)) {
            d.add(cod);


            appStore.SAVESHAREPREFRENCE(appStore.DeleteFirstEdn(d.toString()), AppStore.FAV);
            LISTFILL();
        } else {
            String[] a = SSS.split(",");
            Collections.addAll(listC, a);

            if (Ch(cod, listC)) {
                Log.e("DD", "has");
                Toast.makeText(ShowRssActivity.this, "از لیست حذف شد", Toast.LENGTH_SHORT).show();
                listC.remove(cod);
                if (listC.size() > 0) {
                    appStore.SAVESHAREPREFRENCE(appStore.DeleteFirstEdn(listC.toString()).trim(), AppStore.FAV);
                } else {
                    appStore.SAVESHAREPREFRENCE("", AppStore.FAV);
                }

                //ref
                LISTFILL();

            } else {
                Log.e("DD", "nis");


                String[] ad = SSS.split(",");
                List<String> cd = new ArrayList<>();
                Collections.addAll(cd, ad);
                Log.e("D", appStore.DeleteFirstEdn(cd.toString().trim()));
                appStore.SAVESHAREPREFRENCE(r.trim(), AppStore.FAV);
                Toast.makeText(ShowRssActivity.this, " به لیست اضافه شد", Toast.LENGTH_SHORT).show();
                //ref
                LISTFILL();
            }


        }


    }

    private boolean Ch(String t, List<String> listC) {
        boolean f = false;

        for (String s : listC) {
            if (s.contains(t)) {
                f = true;
            }
        }

        return f;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPush) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private class Task implements Runnable {
        @Override
        public void run() {

        }
    }
}

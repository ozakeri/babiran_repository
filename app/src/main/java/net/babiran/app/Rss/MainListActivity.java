package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;

public class MainListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    List<RssList> Listed = new ArrayList<>();
    List<String> name = new ArrayList<>();
    List<String> Link = new ArrayList<>();
    AdapterUserListMain mAdapter;

    // private AlertDialog prograsDialog;
    private LinearLayout lnNews, lnNewsMy, layout_search;
    private ImageView imNews, imNewsMy, closeImage, btn_search;
    private EditText search_bar;
    private RecyclerView recyclerView, recyclerViewMy, recycler_view_search;
    private LinearLayoutManager linearLayoutManager;
    boolean b1 = false, b2 = false;
    private ProgressBar progress_bar;
    List<String> listC = new ArrayList<>();
    private Timer timer = new Timer();
    List<String> list = new ArrayList<>();
    AdapterUserListMainMy mAd;
    private RequestQueue queue;
    public static final String TAG = "TAG";
    private AdapterUserListToTo adapterUserListToTo;
    private CoordinatorLayout coordinator;
    private CardView blogCardView, newsCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //     getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        INIT();
        b1 = true;
        list.clear();
        Listed();

        coordinator.setVisibility(View.VISIBLE);
        layout_search.setVisibility(View.GONE);
        //////My
        lnNewsMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (b1) {

                } else {
                    b1 = true;
                    imNewsMy.setVisibility(View.VISIBLE);
                    Listed();
                }*/


            }
        });
        imNewsMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1 = false;
                imNewsMy.setVisibility(View.INVISIBLE);
                list.clear();
                recyclerViewMy.setVisibility(View.VISIBLE);
            }
        });


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // user typed: start the timer
                progress_bar.setVisibility(View.VISIBLE);

                if (arg0.length() == 0) {
                    progress_bar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recycler_view_search.setVisibility(View.GONE);
                } else if (arg0.length() >= 2) {
                    progress_bar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    recycler_view_search.setVisibility(View.GONE);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            searchBlog(arg0.toString());
                        }
                    }, 1000); // 600ms delay before the timer executes the „run“ method from TimerTask
                }

            }
        });


        //////////

        blogCardView.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
        newsCardView.setBackgroundColor(getResources().getColor(R.color.white));

        blogCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1 = true;
                b2 = false;
                imNewsMy.setVisibility(View.VISIBLE);
                imNews.setVisibility(View.GONE);
                blogCardView.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
                newsCardView.setBackgroundColor(getResources().getColor(R.color.white));
                Listed();
            }
        });

        newsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b2 = true;
                b1 = false;
                imNewsMy.setVisibility(View.GONE);
                imNews.setVisibility(View.VISIBLE);
                Listed2();
                blogCardView.setBackgroundColor(getResources().getColor(R.color.white));
                newsCardView.setBackgroundColor(getResources().getColor(R.color.gray_lighter));

            }
        });

        lnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (b2) {

                } else {
                    b2 = true;
                    imNews.setVisibility(View.VISIBLE);
                    Listed2();
                }*/

            }
        });
        imNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2 = false;
                imNews.setVisibility(View.INVISIBLE);
                Listed.clear();
                Link.clear();
                name.clear();
                recyclerView.setVisibility(View.GONE);
            }
        });


        ///////////////////////////////////////////////////////////////////////////////

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainListActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                if (b1) {

                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();

                    String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_des)).getText().toString();

                    Intent intent = new Intent(MainListActivity.this, ListActivity.class);
                    intent.putExtra("id", Link);
                    intent.putExtra("title", title);
                    startActivity(intent);

                  /*  String Link1 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();
                    Intent intent1 = new Intent(MainListActivity.this, ListActivity.class);
                    intent.putExtra("id", Link1);
                    startActivity(intent1);
                    System.out.println("b1======" + Link1);*/
                    //b1 = false;
                } else {
                    String Link2 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();
                    System.out.println("b1======" + Link2);
                    Intent intent2 = new Intent(MainListActivity.this, ListRssActivity.class);
                    intent2.putExtra("link", Link2);
                    startActivity(intent2);
                   // b2 = false;
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordinator.setVisibility(View.VISIBLE);
                layout_search.setVisibility(View.GONE);
                recycler_view_search.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordinator.setVisibility(View.GONE);
                layout_search.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recycler_view_search.setVisibility(View.VISIBLE);
            }
        });

    }

    private void INIT() {
        // prograsDialog = new SpotsDialog(MainListActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec_main);
        recycler_view_search = (RecyclerView) findViewById(R.id.recycler_view_search);

        recyclerViewMy = (RecyclerView) findViewById(R.id.rec_main_news_my);
        coordinator = findViewById(R.id.coordinator);
        closeImage = findViewById(R.id.closeImage);
        btn_search = findViewById(R.id.btn_search);
        blogCardView = findViewById(R.id.blogCardView);
        newsCardView = findViewById(R.id.newsCardView);

        imNews = (ImageView) findViewById(R.id.img_main_rss_news_clos);
        lnNews = (LinearLayout) findViewById(R.id.ln_main_rss_news);

        imNewsMy = (ImageView) findViewById(R.id.img_main_rss_news_clos_my);
        lnNewsMy = (LinearLayout) findViewById(R.id.ln_main_rss_news_my);
        search_bar = findViewById(R.id.search_bar);
        layout_search = findViewById(R.id.layout_search);

        progress_bar = findViewById(R.id.progress_bar);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recycler_view_search.setLayoutManager(new LinearLayoutManager(this));

    }

    private void Listed() {
        list.clear();
        recyclerViewMy.setVisibility(View.VISIBLE);
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GETING>> call = n.getCategories();

            call.enqueue(new Callback<List<GETING>>() {
                @Override
                public void onResponse(@NonNull Call<List<GETING>> call, @NonNull retrofit2.Response<List<GETING>> response) {

                    List<GETING> s = response.body();

                    for (int i = 0; i < s.size(); i++) {


                        if (s.get(i).getParentId() == 0) {
                            AppConfig.GETT = s;
                            list.add(s.get(i).getName() + "##" + s.get(i).getId() + "##" + s.get(i).getIcon());

                            System.out.println("icon====" + s.get(i).getIcon());

                        }
                    }
                    if (list.size() > 0) {
                        progress_bar.setVisibility(View.GONE);
                        mAd = new AdapterUserListMainMy(MainListActivity.this, list);
                        recyclerView.setAdapter(mAd);
                    }


                }

                @Override
                public void onFailure(Call<List<GETING>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }

    private void Listed2() {
        Listed.clear();
        Link.clear();
        name.clear();
        recyclerView.setVisibility(View.VISIBLE);
  /*      Link.add("http://www.irna.ir/fa/rss.aspx?kind=-1");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=5");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=20");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=32");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=41");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=180");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=14");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=1");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=54");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=212");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=145");*/

        Link.add("https://www.khabaronline.ir/rss/pl/17");
        Link.add("https://divar.ir/s/tehran/jobs");

        name.add("عناوین کل اخبار");
        name.add("آگهی استخدام");
   /*     name.add("سیاسی");
        name.add("اقتصادی");
        name.add("اجتماعی");
        name.add("فرهنگی");
        name.add("علمی");
        name.add("ورزشی");
        name.add("بین الملل");
        name.add("استانها");
        name.add("حوادث");
        name.add("صفحه اول");*/

        int lio = name.size();
        for (int i = 0; i <lio ; i++) {
            Listed.add(new RssList(name.get(i), Link.get(i)));
        }


        mAdapter = new AdapterUserListMain(MainListActivity.this, Listed);

        recyclerView.setAdapter(mAdapter);
        //    prograsDialog.dismiss();
    }

    public void searchBlog(String key) {

        queue = Volley.newRequestQueue(MainListActivity.this);
        String url = AppConfig.BASE_URL + "api/blog/searchBlogs";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        progress_bar.setVisibility(View.GONE);
                        recycler_view_search.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = new JSONArray(response);
                        List<BLOGME> listBlog = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            //if (Ch(s.get(i).getId() + "", listC)) {
                            listBlog.add(new BLOGME(c.getString("id"), c.getString("titr"), c.getString("image_link"), c.getString("category_id"), c.getString("created_at_int")));
                            //}
                        }

                        if (listBlog.size() > 0) {
                            adapterUserListToTo = new AdapterUserListToTo(MainListActivity.this, listBlog);
                            recycler_view_search.setAdapter(adapterUserListToTo);
                            adapterUserListToTo.notifyDataSetChanged();
                        } else {

                            recycler_view_search.setVisibility(View.GONE);
                            AlertDialog alertDialog = new AlertDialog.Builder(MainListActivity.this).create();
                            alertDialog.setTitle("موردی یافت نشد");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                            alertDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("offset", String.valueOf(offset));
                //params.put("limit", String.valueOf(limit));
                params.put("q", key);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

}

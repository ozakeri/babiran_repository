package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Rss.AdapterUserListMain;
import net.babiran.app.Rss.AdapterUserListMainMy;
import net.babiran.app.Rss.AdapterUserListToTo;
import net.babiran.app.Rss.BLOGME;
import net.babiran.app.Rss.ListActivity;
import net.babiran.app.Rss.ListRssActivity;
import net.babiran.app.Rss.RecyclerItemClickListener;
import net.babiran.app.Rss.RssList;
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

public class BlogFrgment extends Fragment {

    private Toolbar toolbar;
    List<RssList> Listed = new ArrayList<>();
    List<String> name = new ArrayList<>();
    List<String> Link = new ArrayList<>();
    AdapterUserListMain mAdapter;

    // private AlertDialog prograsDialog;
    private LinearLayout lnNews, lnNewsMy, layout_search;
    private ImageView imNews, imNewsMy;
    private FloatingActionButton floatingActionButton;
    private EditText search_bar;
    private RecyclerView recyclerView, recycler_view_search;
    private RelativeLayout layout_recycler_view_search;
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
    private CardView blogCardView, newsCardView;


    public BlogFrgment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_list, container, false);

        AppConfig.frag = BlogFrgment.this;

        INIT(view);
        b1 = true;
        list.clear();
        Listed();

        //layout_search.setVisibility(View.GONE);
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

        blogCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main_selected));
        newsCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main));

        blogCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_bar.setText("");
                b1 = true;
                b2 = false;
                imNewsMy.setVisibility(View.VISIBLE);
                imNews.setVisibility(View.GONE);
                blogCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main_selected));
                newsCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main));
                Listed();
            }
        });

        newsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_bar.setText("");
                b2 = true;
                b1 = false;
                imNewsMy.setVisibility(View.GONE);
                imNews.setVisibility(View.VISIBLE);
                Listed2();
                blogCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main));
                newsCardView.setBackground(getResources().getDrawable(R.drawable.background_button_main_selected));

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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                System.out.println("bbbbbbbbbbbb"+b1);
                System.out.println("bbbbbbbbbbbb"+b2);

                if (b1) {
                    System.out.println("111111111111111111");
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();

                    String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_des)).getText().toString();

                    Intent intent = new Intent(getActivity(), ListActivity.class);
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
                    System.out.println("22222222222222222");
                    String Link2 = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();
                    Intent intent2 = new Intent(getActivity(), ListRssActivity.class);
                    intent2.putExtra("link", Link2);
                    startActivity(intent2);
                    // b2 = false;
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //layout_search.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recycler_view_search.setVisibility(View.VISIBLE);
            }
        });


        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.layout_search.setVisibility(View.GONE);
        MainActivity.btnBack.setVisibility(View.INVISIBLE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();


        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    //MainActivity.layout_search.setVisibility(View.VISIBLE);
                    //MainActivity.blogContainer.setVisibility(View.INVISIBLE);
                    if (MainActivity.productlist.getVisibility() == View.VISIBLE) {
                        //MainActivity.productlist.setVisibility(View.INVISIBLE);
                        FragmentManager fm = getFragmentManager();
                        if (fm != null) {
                            ProductListFragment fragm = (ProductListFragment) fm.findFragmentById(R.id.ProductListcontainer);
                            if (fragm != null) {
                                fragm.backpress();
                            }
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppConfig.act);
                        builder.setTitle("می خواهید خارج شوید؟");
                        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (AppConfig.checkReciveSms == true) {
                                    AppConfig.checkReciveSms = false;
                                }
                                if (AppConfig.btnSubmitOk == true) {
                                    AppConfig.btnSubmitOk = false;
                                }

                                AppConfig.act.finish();


                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }


                    return true;
                }
                return false;
            }
        });
    }

    private void INIT(View view) {
        // prograsDialog = new SpotsDialog(MainListActivity.this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec_main);
        recycler_view_search = (RecyclerView) view.findViewById(R.id.recycler_view_search);

        blogCardView = view.findViewById(R.id.blogCardView);
        newsCardView = view.findViewById(R.id.newsCardView);

        imNews = (ImageView) view.findViewById(R.id.img_main_rss_news_clos);
        lnNews = (LinearLayout) view.findViewById(R.id.ln_main_rss_news);

        imNewsMy = (ImageView) view.findViewById(R.id.img_main_rss_news_clos_my);
        lnNewsMy = (LinearLayout) view.findViewById(R.id.ln_main_rss_news_my);
        search_bar = view.findViewById(R.id.search_bar);
        layout_search = view.findViewById(R.id.layout_search);

        progress_bar = view.findViewById(R.id.progress_bar);


        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recycler_view_search.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void Listed() {
        list.clear();
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GETING>> call = n.getCategories();

            call.enqueue(new Callback<List<GETING>>() {
                @Override
                public void onResponse(@NonNull Call<List<GETING>> call, @NonNull retrofit2.Response<List<GETING>> response) {

                    List<GETING> s = response.body();
                    System.out.println("response.body()======" + response.body());

                    for (int i = 0; i < s.size(); i++) {


                        if (s.get(i).getParentId() == 0) {
                            AppConfig.GETT = s;
                            list.add(s.get(i).getName() + "##" + s.get(i).getId() + "##" + s.get(i).getIcon());

                        }
                    }
                    if (list.size() > 0) {
                        progress_bar.setVisibility(View.GONE);
                        mAd = new AdapterUserListMainMy(getActivity(), list);
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

        Link.add("https://www.khabaronline.ir/rss");

        name.add("عناوین کل اخبار (khabaronline.ir)");
   /*name.add("سیاسی");
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
        for (int i = 0; i < lio; i++) {
            Listed.add(new RssList(name.get(i), Link.get(i)));
        }


        mAdapter = new AdapterUserListMain(getActivity(), Listed);

        recyclerView.setAdapter(mAdapter);
        //    prograsDialog.dismiss();
    }

    public void searchBlog(String key) {

        queue = Volley.newRequestQueue(getActivity());
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
                            adapterUserListToTo = new AdapterUserListToTo(getActivity(), listBlog);
                            recycler_view_search.setAdapter(adapterUserListToTo);
                            adapterUserListToTo.notifyDataSetChanged();
                        } else {

                            recycler_view_search.setVisibility(View.GONE);
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
}

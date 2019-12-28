package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyModelQu;
import net.babiran.app.Servic.MyServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;

public class MainListActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    List<RssList > Listed = new ArrayList<>();
    List<String> name = new ArrayList<>();
    List<String> Link = new ArrayList<>();
    AdapterUserListMain mAdapter;

   // private AlertDialog prograsDialog;
    private LinearLayout lnNews,lnNewsMy;
    private ImageView imNews,imNewsMy;
    private RecyclerView recyclerView,recyclerViewMy;
    private LinearLayoutManager linearLayoutManager;
    boolean b1=false,b2=false;
    private ProgressBar progress_bar;

    List<String> list = new ArrayList<>();
    AdapterUserListMainMy mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        list.clear();
        Listed();

        //////My
        lnNewsMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(b1)
                {

                }
                else
                {
                    b1=true;
                    imNewsMy.setVisibility(View.VISIBLE);
                    Listed();
                }



            }
        });
        imNewsMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                b1=false;
                imNewsMy.setVisibility(View.INVISIBLE);
                list.clear();
                recyclerViewMy.setVisibility(View.GONE);
            }
        });




        //////////


        lnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(b2)
                {

                }
                else
                {
                    b2=true;
                    imNews.setVisibility(View.VISIBLE);
                    Listed2();
                }

            }
        });
        imNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                b2=false;
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
            public void onItemClick(View view, int position)
            {


                String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();

                String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss_mainn_des)).getText().toString();

                Intent intent =new Intent(MainListActivity.this,ListActivity.class);
                intent.putExtra("id",Link);
                intent.putExtra("title",title);
                startActivity(intent);

               /* if(b1)
                {
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();

                    Intent intent =new Intent(MainListActivity.this,ListActivity.class);
                    intent.putExtra("id",Link);
                    startActivity(intent);
                    b1=false;
                }
                else
                {
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_mainn_link)).getText().toString();

                    Intent intent =new Intent(MainListActivity.this,ListRssActivity.class);
                    intent.putExtra("link",Link);

                    startActivity(intent);
                }*/

            }

            @Override
            public void onLongItemClick(View view, int position)
            {

            }
        }));


    }

    private void INIT()
    {
       // prograsDialog = new SpotsDialog(MainListActivity.this);
        recyclerView=(RecyclerView)findViewById(R.id.rec_main);

        recyclerViewMy=(RecyclerView)findViewById(R.id.rec_main_news_my);

        imNews=(ImageView) findViewById(R.id.img_main_rss_news_clos);
        lnNews=(LinearLayout) findViewById(R.id.ln_main_rss_news);

        imNewsMy=(ImageView) findViewById(R.id.img_main_rss_news_clos_my);
        lnNewsMy=(LinearLayout) findViewById(R.id.ln_main_rss_news_my);

        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    private void Listed()
    {
        recyclerViewMy.setVisibility(View.VISIBLE);
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GETING>> call = n.getCategories();

            call.enqueue(new Callback<List<GETING>>()
            {
                @Override
                public void onResponse(@NonNull Call<List<GETING>> call, @NonNull retrofit2.Response<List<GETING>> response)
                {

                    List<GETING> s = response.body();

                    for(int i = 0 ; i< s.size() ; i++)
                    {
                        if(s.get(i).getParentId()==0)
                        {
                            AppConfig.GETT =s;
                            list.add(s.get(i).getName()+"##"+s.get(i).getId());

                        }
                    }
                    if(list.size()>0)
                    {
                        progress_bar.setVisibility(View.GONE);
                        mAd = new AdapterUserListMainMy(MainListActivity.this, list);
                        recyclerView.setAdapter(mAd);
                    }


                }
                @Override
                public void onFailure(Call<List<GETING>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage()+"\n"+t.toString());
                }
            });
        }catch (Exception ex)
        {
            Log.e("response 3 :", ex.getMessage());
        }


    //    prograsDialog.dismiss();
    }

    private void Listed2()
    {
        recyclerView.setVisibility(View.VISIBLE);
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=-1");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=5");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=20");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=32");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=41");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=180");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=14");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=1");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=54");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=212");
        Link.add("http://www.irna.ir/fa/rss.aspx?kind=145");

        name.add("عناوین کل اخبار");
        name.add("سیاسی");
        name.add("اقتصادی");
        name.add("اجتماعی");
        name.add("فرهنگی");
        name.add("علمی");
        name.add("ورزشی");
        name.add("بین الملل");
        name.add("استانها");
        name.add("حوادث");
        name.add("صفحه اول");

        int lio=name.size();
        for (int i=0  ;  i<lio  ; i++)
        {
            Listed.add(new RssList(name.get(i),Link.get(i)));
        }


        mAdapter = new AdapterUserListMain(MainListActivity.this, Listed);

        recyclerView.setAdapter(mAdapter);
        //    prograsDialog.dismiss();
    }

}

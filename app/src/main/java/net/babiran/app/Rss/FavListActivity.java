package net.babiran.app.Rss;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.AppStore;
import net.babiran.app.R;
import net.babiran.app.Servic.GETINGBlog;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FavListActivity extends AppCompatActivity {

    TextView textView;
    List<BLOGME> list = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterUserListToTo adapterUserListToTo;
    List<String>listC=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        textView = (TextView)findViewById(R.id.jhsbdvj);

        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();

            }
        });

        /////////
        AppStore appStore = new AppStore(FavListActivity.this);
        String    SSS=  appStore.LoadMyShereKES(AppStore.FAV);
        String[] a = SSS.split(",");
        Collections.addAll(listC, a);
        /////////


        recyclerView=(RecyclerView)findViewById(R.id.dgdgggfgf);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        Listed();



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(FavListActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {

                ListtoListActivity.ID_ME=list.get(position).id;

                Intent intent =new Intent(FavListActivity.this,ShowRssActivity.class);
                intent.putExtra("WWWW",list.get(position).id);
                startActivity(intent);



            }

            @Override
            public void onLongItemClick(View view, int position)
            {

            }
        }));


    }

    private void Listed()
    {

        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GETINGBlog>> call = n.getBlogs();

            call.enqueue(new Callback<List<GETINGBlog>>()
            {
                @Override
                public void onResponse(@NonNull Call<List<GETINGBlog>> call, @NonNull retrofit2.Response<List<GETINGBlog>> response)
                {

                    List<GETINGBlog> s = response.body();
                    for(int i = 0 ; i< s.size() ; i++)
                    {


                            if (Ch(s.get(i).getId()+"",listC))
                            {
                                list.add(new BLOGME(String.valueOf(s.get(i).getId()),s.get(i).getTitr(),s.get(i).getImageLink(),String.valueOf(s.get(i).getCategoryId())));

                            }

                    }


                    if (list.size()>0){
                        textView.setVisibility(View.GONE);
                        adapterUserListToTo = new AdapterUserListToTo(FavListActivity.this, list);

                        recyclerView.setAdapter(adapterUserListToTo);
                    }


                }
                @Override
                public void onFailure(Call<List<GETINGBlog>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage()+"\n"+t.toString());
                }
            });
        }catch (Exception ex)
        {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }


    private boolean Ch(String t,List<String> listC)
    {
        boolean f = false;

        for(String s : listC)
        {
            if(s.contains(t)){
                f=true;
            }
        }

        return f;
    }
}

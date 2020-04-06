package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.AppController;
import net.babiran.app.R;
import net.babiran.app.Servic.GETINGBlog;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class ListtoListActivity extends AppCompatActivity {
    private Toolbar toolbar;

    List<BLOGME> list = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterUserListToTo adapterUserListToTo;
    public static String ID_ME = "";
    String title = null;
    String id = null;
    private AlertDialog prograsDialog;
    private AppController appController = AppController.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listto_list);

        prograsDialog = new SpotsDialog(ListtoListActivity.this);
        prograsDialog.show();

        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
////////////////////////////////////////////

        if (getIntent().getExtras() != null){
            id = getIntent().getExtras().getString("id");
            title = getIntent().getExtras().getString("title");
        }

        recyclerView = (RecyclerView) findViewById(R.id.dgdgg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        TextView label_title = (TextView) findViewById(R.id.label_title);
        label_title.setText(title);

        Listed();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ListtoListActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ListtoListActivity.ID_ME = list.get(position).id;
                Intent intent = new Intent(ListtoListActivity.this, ShowRssActivity.class);
                startActivity(intent);

                System.out.println("ID_ME====" + ListtoListActivity.ID_ME);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }


    private void Listed() {
        //String p = getIntent().getExtras().getString("id");
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<GETINGBlog>> call = n.getBlogs();

            call.enqueue(new Callback<List<GETINGBlog>>() {
                @Override
                public void onResponse(@NonNull Call<List<GETINGBlog>> call, @NonNull retrofit2.Response<List<GETINGBlog>> response) {

                    prograsDialog.dismiss();
                    List<GETINGBlog> s = response.body();
                    appController.setResponse(response);
                    for (int i = 0; i < s.size(); i++) {

                        Log.e("FFD", "" + Integer.parseInt(id));
                        Log.e("FFD", "" + s.get(i).getCategoryId());

                        if (s.get(i).getCategoryId() == Integer.parseInt(id)) {
                            list.add(new BLOGME(String.valueOf(s.get(i).getId()), s.get(i).getTitr(), s.get(i).getImageLink(), String.valueOf(s.get(i).getCategoryId())));
                        }
                    }


                    if (list.size() > 0) {
                        adapterUserListToTo = new AdapterUserListToTo(ListtoListActivity.this, list);

                        recyclerView.setAdapter(adapterUserListToTo);
                    }

                }

                @Override
                public void onFailure(Call<List<GETINGBlog>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }
}

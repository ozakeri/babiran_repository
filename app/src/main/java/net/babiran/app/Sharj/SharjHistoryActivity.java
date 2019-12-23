package net.babiran.app.Sharj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyModelQu;
import net.babiran.app.Servic.MyServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Handlers.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;

public class SharjHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list = new ArrayList<>();
    private AdapterHistorySharj mAdapter;
    DatabaseHandler db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharj);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseHandler(getApplicationContext());
        HashMap<String, String> userDetailsHashMap = db.getUserDetails();
        id = Integer.parseInt(userDetailsHashMap.get("id"));
        Log.e("ID", "" + id);

        INIT();
    }

    private void INIT() {
        recyclerView = (RecyclerView) findViewById(R.id.wfefwsfswf);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        SenDToServer();


//
    }


    private void SenDToServer() {


        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<List<MyModelQu>> call = n.HistorySharj(id);

            call.enqueue(new Callback<List<MyModelQu>>() {
                @Override
                public void onResponse(@NonNull Call<List<MyModelQu>> call, @NonNull retrofit2.Response<List<MyModelQu>> response) {

                    List<MyModelQu> s = response.body();
                    for (int i = 0; i < s.size(); i++) {
                        list.add(s.get(i).getMobile() + "##" + s.get(i).getOperatorId() + "##" + s.get(i).getPrice() + "##" + s.get(i).getCreatedAt() + "##" + s.get(i).getRefId());
                    }
                    mAdapter = new AdapterHistorySharj(SharjHistoryActivity.this, list);
                    recyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onFailure(Call<List<MyModelQu>> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


    }
}

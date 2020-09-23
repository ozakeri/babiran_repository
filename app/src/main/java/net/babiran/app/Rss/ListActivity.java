package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import tools.AppConfig;

public class ListActivity extends AppCompatActivity {
    private AlertDialog prograsDialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterUserList mAdapter;
    private String id = null;
    private String title = null;
    private AdapterUserListMy mAdaptermy;
    private TextView label_newsTitle;
    List<String> list = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
            title = getIntent().getExtras().getString("title");
        }

        INIT();
        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        label_newsTitle.setText(title);
    }

    private void INIT() {
        prograsDialog = new SpotsDialog(ListActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        linearLayoutManager = new LinearLayoutManager(this);
        label_newsTitle = (TextView) findViewById(R.id.label_newsTitle);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (id != null) {
            Listed();
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ListActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();

                String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss)).getText().toString();
                String chi = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.tfghjkhjkhjkk)).getText().toString();
                if (!TextUtils.isEmpty(getIntent().getExtras().getString("id"))) {
                    if (chi.equals("true")) {
                        Intent intent = new Intent(ListActivity.this, ListRssActivity.class);
                        intent.putExtra("id", Link);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ListActivity.this, ListtoListActivity.class);
                        intent.putExtra("id", Link);
                        intent.putExtra("title", name);
                        startActivity(intent);
                    }
//                    Intent intent =new Intent(ListActivity.this,ListRssActivity.class);
//                        intent.putExtra("id",Link);
//                        intent.putExtra("name",name);
//                        startActivity(intent);

                }


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }

    private void Listed() {
        List<GETING> s = AppConfig.GETT;
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).getParentId() == Integer.parseInt(id)) {
                AppConfig.GETT = s;
                list.add(s.get(i).getName() + "##" + s.get(i).getId() + "##" + s.get(i).getHasChild()+ "##" + s.get(i).getIcon());
            }
        }

        prograsDialog.dismiss();
        mAdaptermy = new AdapterUserListMy(ListActivity.this, list);
        recyclerView.setAdapter(mAdaptermy);

        //    prograsDialog.dismiss();
    }


}

package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import tools.AppConfig;

public class LastListActivity extends AppCompatActivity {
    private AlertDialog prograsDialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterUserList mAdapter;
    private String id;
    private AdapterUserListMy mAdaptermy;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_list);
        id=getIntent().getExtras().getString("id");
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

    }

    private void INIT()
    {
        prograsDialog = new SpotsDialog(LastListActivity.this);
        recyclerView=(RecyclerView)findViewById(R.id.rec);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Listed();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(LastListActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();

                String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.txt_rc_rss)).getText().toString();
                String chi = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.tfghjkhjkhjkk)).getText().toString();
                if(!TextUtils.isEmpty(getIntent().getExtras().getString("id")))
                {

                        Intent intent =new Intent(LastListActivity.this,ListtoListActivity.class);
                        intent.putExtra("id",Link);
                        startActivity(intent);

//                    Intent intent =new Intent(ListActivity.this,ListRssActivity.class);
//                        intent.putExtra("id",Link);
//                        intent.putExtra("name",name);
//                        startActivity(intent);

                }


            }

            @Override
            public void onLongItemClick(View view, int position)
            {

            }
        }));


    }

    private void Listed()
    {
        List<GETING> s = AppConfig.GETT;
        for (int i = 0 ; i<s.size() ; i++)
        {

            if(s.get(i).getParentId()==Integer.parseInt(id))
            {
                AppConfig.GETT =s;
                list.add(s.get(i).getName()+"##"+s.get(i).getId()+"##"+s.get(i).getHasChild());
            }
        }
        prograsDialog.dismiss();
        mAdaptermy = new AdapterUserListMy(LastListActivity.this, list);
        recyclerView.setAdapter(mAdaptermy);


        //    prograsDialog.dismiss();
    }








}

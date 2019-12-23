package net.babiran.app.Sefaresh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.R;
import net.babiran.app.Rss.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Category;
import Models.CategoryInfo;
import tools.AppConfig;
import ui_elements.MyTextView;

import static tools.AppConfig.categories;
import static tools.AppConfig.restaurants_info;

public class ListFoodActivity extends AppCompatActivity {
    private AdapterfoolplaceList adapterfoolplaceList;
    private MyTextView txtLbl;
    String idSender;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Category> categoryList = new ArrayList<>();
    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private ImageView imgBack;
    private String startTime = null;
    private String endTime = null;
    private String[] arrOfStr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);

        INIT();


    }

    private void INIT() {

        imgBack = (ImageView) findViewById(R.id.btn_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AppConfig.BACK_TO_LIOST = "1";
            }
        });
        idSender = getIntent().getExtras().getString("IDD");
        txtLbl = (MyTextView) findViewById(R.id.rlghkjegkerg);

        if (idSender.equals("1197")) {
            txtLbl.setText("لیست فست فــود");
        } else {
            txtLbl.setText("لیست  رستوران ها");
        }


        for (int i = 0; i < categories.length(); i++) {
            try {
                JSONObject c = categories.getJSONObject(i);

                if (idSender.equals(c.getString("parent_id"))) {
                    Category category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                            , c.getString("icon"), c.getString("slide_image"));
                    categoryList.add(category);
                }


            } catch (Exception e) {

            }

        }


        for (int i = 0; i < restaurants_info.length(); i++) {
            try {
                JSONObject c = restaurants_info.getJSONObject(i);

                // CategoryInfo category =  new CategoryInfo(c.getString("id"),c.getString("category_id"),c.getString("work_time") ,c.getString("service_area"),c.getString("minimum_order"),c.getString("image"));
                CategoryInfo category = new CategoryInfo(c.getString("id"), c.getString("category_id"), c.getString("work_time"), c.getString("service_area"), c.getString("minimum_order"), c.getString("image"));

                categoryInfoList.add(category);


            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }


        getNewPro();
        recyclerView = (RecyclerView) findViewById(R.id.wergwrtygrty);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        adapterfoolplaceList = new AdapterfoolplaceList(ListFoodActivity.this, categoryList, categoryInfoList);

        recyclerView.setAdapter(adapterfoolplaceList);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ListFoodActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.erbdvbds)).getText().toString();
                String pic = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.adfbgdfhgrtyhrutj)).getText().toString();

                String time = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                        .itemView.findViewById(R.id.sdfgbwtghkjd)).getText().toString();

                try {
                    arrOfStr = time.split("الی");
                    startTime = arrOfStr[0];
                    endTime = arrOfStr[1];
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent intent = new Intent(ListFoodActivity.this, ShowActivity.class);
                intent.putExtra("IDDD", id);
                intent.putExtra("IsdfDDD", pic);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("title", "توضیحات فست فود ");
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }


    public void getNewPro() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("لطفا کمی صبر نمایید");
        progress.setMessage("در حال دریافت اطلاعات از سرور..");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = AppConfig.BASE_URL + "api/product/getNewProducts/" + 0 + "/" + "202";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {
                            AppConfig.NewPro = response;

                            progress.dismiss();


                        } catch (Exception e) {

                            AppConfig.error(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(0));
                params.put("limit", String.valueOf("202"));

                return params;
            }

        };

        queue.add(getRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AppConfig.BACK_TO_LIOST = "1";

    }
}
